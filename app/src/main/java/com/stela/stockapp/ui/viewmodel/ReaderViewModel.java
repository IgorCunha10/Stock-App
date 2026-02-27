package com.stela.stockapp.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.stela.stockapp.data.repository.ReaderRepository;
import com.stela.stockapp.domain.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReaderViewModel extends ViewModel {

    private final ReaderRepository repository;

    private final MutableLiveData<List<Tag>> tagsLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> connectedLiveData = new MutableLiveData<>(false);

    private final Map<String, Long> lastReadMap = new HashMap<>();
    private static final long READ_COOLDOWN_MS = 300;

    public ReaderViewModel(ReaderRepository repository) {
        this.repository = repository;

        repository.setOnTagRead(epcBean -> {
            Tag tag = new Tag(epcBean);

            if (canProcess(tag.getEpc())) {
                addOrUpdateTag(tag);
            }
        });
    }

    public LiveData<List<Tag>> getTags() {
        return tagsLiveData;
    }

    public LiveData<Boolean> isConnected() {
        return connectedLiveData;
    }

    public void connect() {
        repository.connect((success, message) -> {
            connectedLiveData.postValue(success);
        });
    }

    public void startScan() {
        repository.startScan();
    }

    public void stopScan() {
        repository.stopScan();
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

    private void addOrUpdateTag(Tag newTag) {
        List<Tag> current = tagsLiveData.getValue();
        if (current == null) current = new ArrayList<>();

        boolean found = false;

        for (Tag tag : current) {
            if (tag.getEpc().equals(newTag.getEpc())) {
                tag.setReadCount(tag.getReadCount() + 1);
                tag.setRssi(newTag.getRssi());
                found = true;
                break;
            }
        }

        if (!found) {
            newTag.setReadCount(1);
            current.add(newTag);
        }

        tagsLiveData.postValue(current);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.release();
    }
}