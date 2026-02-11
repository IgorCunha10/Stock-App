package com.stela.stockapp.data;

import android.content.Context;

import com.stela.stockapp.data.javadb.AppDataBase;
import com.stela.stockapp.data.javadb.ProductsDao;
import com.stela.stockapp.model.Product;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import java.util.List;

public class ProductsRepository {

    private static ProductsRepository instance;
    private final ProductsDao productsDao;

    private String text;

    public static synchronized ProductsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ProductsRepository(context);
        }
        return instance;
    }

    public ProductsRepository(Context context) {
        productsDao = AppDataBase.getInstance(context).productsDao();
    }

    public LiveData<List<Product>> getAll() {
        return productsDao.getAll();
    }

    public void addProduct(Product product) {
        productsDao.insert(product);
    }

    public void updateProduct(Product product) {
        productsDao.update(product);
    }

    public void deleteProduct(Product product) {
        productsDao.delete(product);
    }

}
