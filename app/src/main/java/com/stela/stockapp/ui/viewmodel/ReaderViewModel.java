package com.stela.stockapp.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.repository.ReaderRepository;
import com.stela.stockapp.data.repository.TagRepository;
import com.stela.stockapp.domain.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;

public class ReaderViewModel extends ViewModel {

    private final ReaderRepository readerRepository;
    private final TagRepository tagRepository;
    private final MutableLiveData<List<Tag>> tagsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> connectedLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Product> productLiveData = new MutableLiveData<>();
    private final Map<String, Long> lastReadMap = new HashMap<>();
    private final Map<String, Product> productCache = new HashMap<>();
    private static final long READ_COOLDOWN_MS = 300;
    public ReaderViewModel(ReaderRepository readerRepository, TagRepository tagRepository) {
        this.readerRepository = readerRepository;
        this.tagRepository = tagRepository;

        readerRepository.setOnTagRead(epcBean -> {
            Tag tag = new Tag(epcBean);
            checkTag(tag);
        });
    }


    public LiveData<List<Tag>> getTags() {
        return tagsLiveData;
    }

    public LiveData<Boolean> isConnected() {
        return connectedLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public LiveData<Product> getProductLiveData() {
        return productLiveData;
    }


    public void connect() {
        readerRepository.connect((success, message) -> connectedLiveData.postValue(success));
    }

    public void startScan() {
        try {
            readerRepository.startScan();
        } catch (Exception e) {
            errorLiveData.postValue("Erro ao iniciar scan");
        }
    }

    public void stopScan() {
        try {
            readerRepository.stopScan();
        } catch (Exception e) {
            errorLiveData.postValue("Erro ao parar scan");
        }
    }

    public void clearTags() {
        tagsLiveData.postValue(new ArrayList<>());
    }

    private boolean canProcess(String epc) {
        long now = System.currentTimeMillis();
        Long last = lastReadMap.get(epc);
        if (last == null || now - last > READ_COOLDOWN_MS) {
            lastReadMap.put(epc, now);
            return true;
        }
        return false;
    }

    public void checkTag(Tag tag) {
        if (!canProcess(tag.getEpc())) return;

        Executors.newSingleThreadExecutor().execute(() -> {
            String epc = tag.getEpc().trim().toUpperCase();
            Product product = productCache.get(epc);

            if (product != null) {
                productLiveData.postValue(product);
            } else {
                errorLiveData.postValue("Tag não cadastrada");
            }

            addOrUpdateTag(tag);
        });
    }

    private void addOrUpdateTag(Tag newTag) {
        List<Tag> current = tagsLiveData.getValue();
        if (current == null) current = new ArrayList<>();
        else current = new ArrayList<>(current);

        boolean found = false;
        for (int i = 0; i < current.size(); i++) {
            Tag old = current.get(i);
            if (old.getEpc().equals(newTag.getEpc())) {
                Tag updated = new Tag(old.getEpc(), old.getSerialNumber(),
                        old.getReadCount() + 1, newTag.getRssi());
                current.set(i, updated);
                found = true;
                break;
            }
        }
        if (!found) {
            Tag first = new Tag(newTag.getEpc(), newTag.getSerialNumber(), 1, newTag.getRssi());
            current.add(first);
        }
        tagsLiveData.postValue(current);
    }

    public void loadProducts(Runnable onLoaded) {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Product> products = tagRepository.getAllProducts();
            productCache.clear();
            for (Product p : products) {
                productCache.put(p.getTagId().trim().toUpperCase(), p);
            }
            if (onLoaded != null) {
                new Handler(Looper.getMainLooper()).post(onLoaded);
            }
        });
    }
}