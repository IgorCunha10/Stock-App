package com.stela.stockapp.data.repository;

import android.content.Context;

import com.stela.stockapp.data.local.AppDataBase;
import com.stela.stockapp.data.local.ProductsDao;
import com.stela.stockapp.data.local.TagsDao;
import com.stela.stockapp.data.model.pojo.ProductTagJoin;
import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.model.tag.TagEntity;

import androidx.lifecycle.LiveData;
import androidx.room.Transaction;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductsRepository {
    private static ProductsRepository instance;
    private final ProductsDao productsDao;
    private final TagsDao tagsDao;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();


    public ProductsRepository(AppDataBase db) {
        productsDao = db.productsDao();
        tagsDao = db.tagsDao();
    }

    public LiveData<Product> findById(int id){
        return productsDao.findById(id);
    }

    public LiveData<List<ProductTagJoin>> getAll() {
        return productsDao.getAllProductsWithTag();
    }

    public void insertProductWithTag(Product product, TagEntity tag) {
        executor.execute(() -> {

            tagsDao.insert(tag);

            long id = productsDao.insert(product);
            product.setId((int) id);

        });
    }

    public void updateProduct(Product product) {
        executor.execute(() -> productsDao.update(product));
    }

    public void deleteProduct(Product product) {
        executor.execute(() -> productsDao.delete(product));
    }

    public void deleteProductById(int id) {
        executor.execute(() -> productsDao.deleteProduct(id));
    }}