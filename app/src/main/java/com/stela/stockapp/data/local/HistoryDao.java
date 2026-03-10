package com.stela.stockapp.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.stela.stockapp.data.model.history.ProductHistory;
import com.stela.stockapp.data.model.history.ProductHistoryDto;

import java.util.List;

/* @Dao
public interface HistoryDao {

    @Insert
    long insert(ProductHistory history);

    @Query("SELECT * FROM products_history ORDER BY timeStamp DESC")
    LiveData<List<ProductHistory>> getAllHistory();

    @Query("SELECT " +
            "products_history.id AS productHistoryId, " +
            "products_history.productId, " +
            "products_history.productAction, " +
            "products_history.movedQuantity, " +
            "products_history.timeStamp, " +
            "products_history.productTag, " +
            "products.productName, " +
            "products.productDescription, " +
            "products.productPrice " +
            "FROM products_history " +
            "INNER JOIN products " +
            "ON products_history.productId = products.productId " +
            "ORDER BY products_history.timeStamp DESC")
    LiveData<List<ProductHistoryDto>> getAllHistoryWithProductData();




}
 */