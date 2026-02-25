package com.stela.stockapp.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.stela.stockapp.data.model.history.ProductHistory;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert
    long insert(ProductHistory history);

    @Query("SELECT * FROM products_history ORDER BY timeStamp DESC")
    LiveData<List<ProductHistory>> getAllHistory();


}
