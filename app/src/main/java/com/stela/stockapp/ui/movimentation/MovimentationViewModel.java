package com.stela.stockapp.ui.movimentation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.stela.stockapp.data.model.history.ProductHistory;
import com.stela.stockapp.data.repository.HistoryRepository;

import java.util.List;

public class MovimentationViewModel extends AndroidViewModel {

    private final HistoryRepository repository;
    private final LiveData<List<ProductHistory>> allHistory;



    public MovimentationViewModel(@NonNull Application application) {
        super(application);

        repository = HistoryRepository.getInstance(application);
        allHistory = repository.getAllHistory();

    }

    public LiveData<List<ProductHistory>> getAllHistory() {
        return allHistory;
    }

    public void insertHistory(ProductHistory history) {
        repository.insertHistory(history);
    }

}
