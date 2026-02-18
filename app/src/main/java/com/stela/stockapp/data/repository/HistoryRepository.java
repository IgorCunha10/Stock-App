package com.stela.stockapp.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.stela.stockapp.data.local.AppDataBase;
import com.stela.stockapp.data.local.HistoryDao;
import com.stela.stockapp.data.model.history.ProductHistory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryRepository {
    private static HistoryRepository instance;
    private final HistoryDao historyDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static synchronized HistoryRepository getInstance(Context context) {
        if (instance == null) {
            instance = new HistoryRepository(context);
        }
        return instance;
    }

    private HistoryRepository(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        historyDao = db.historyDao();
    }

    public LiveData<List<ProductHistory>> getAllHistory() {
        return historyDao.getAllHistory();
    }

    public void insertHistory(ProductHistory history) {
        executor.execute(() -> historyDao.insert(history));
    }



}
