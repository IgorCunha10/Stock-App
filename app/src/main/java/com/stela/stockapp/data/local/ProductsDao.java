package com.stela.stockapp.data.local;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.stela.stockapp.data.model.product.Product;

import java.util.List;

@Dao
public interface ProductsDao {

    @Query("SELECT * FROM products")
    LiveData<List<Product>> getAll();

    @Insert
    long insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);


}
