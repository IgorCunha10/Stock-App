package com.stela.stockapp.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.repository.ProductsRepository;
import com.stela.stockapp.domain.ReaderManager;
import com.stela.stockapp.data.repository.TagRepository;
import com.stela.stockapp.domain.model.Tag;
import com.stela.stockapp.ui.product.ProductCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReaderViewModel extends ViewModel {

    private final ReaderManager readerManager;
    private final ProductsRepository productsRepository;
    private String scannedTag;
    private final TagRepository tagRepository;
    private final MutableLiveData<List<Product>> productsLiveData = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public LiveData<List<Product>> getProductsLiveData() {
        return productsLiveData;
    }

    private final MutableLiveData<List<Tag>> tagsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> connectedLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Product> productLiveData = new MutableLiveData<>();

    private final Map<String, Long> lastReadMap = new LinkedHashMap<String, Long>(16,
            0.75f, true) {
        private static final int MAX_LAST_READ_ENTRIES = 1000;

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Long> eldest) {
            return size() > MAX_LAST_READ_ENTRIES;
        }
    };
    private final Map<String, Product> productCache = new HashMap<>();
    private static final long READ_COOLDOWN_MS = 300;

    public ReaderViewModel(ReaderManager readerManager,
                           TagRepository tagRepository,
                           ProductsRepository productsRepository) {

        this.readerManager = readerManager;
        this.tagRepository = tagRepository;
        this.productsRepository = productsRepository;

        readerManager.setOnTagRead(epcBean -> {
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
        readerManager.connect((success, message) -> connectedLiveData.postValue(success));
    }

    public void startScan() {
        try {
            readerManager.startScan();
        } catch (Exception e) {
            errorLiveData.postValue("Erro ao iniciar scan");
        }
    }

    public void stopScan() {
        try {
            readerManager.stopScan();
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
//
        String epc = tag.getEpc();

        if (productCache.containsKey(epc)) {
            productLiveData.postValue(productCache.get(epc));
            addOrUpdateTag(tag);
            return;
        }

        productsRepository.getProductByTag(epc, new ProductCallback() {
            @Override
            public void onSuccess(Product product) {
                productCache.put(epc, product);
                productLiveData.postValue(product);
                addOrUpdateTag(tag);
            }

            @Override
            public void onError(String message) {
                errorLiveData.postValue(message);
                addOrUpdateTag(tag);
            }
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

    public void loadProducts() {
        tagRepository.getAllProducts().observeForever(products -> {
            if (products == null) return;

            executor.execute(() -> {
                productCache.clear();
                for (Product p : products) {
                    productCache.put(p.getTagId(), p);
                }
                productsLiveData.postValue(products);
            });
        });
    }
}
