package com.stela.stockapp.data.repository;

import android.content.Context;

import com.stela.stockapp.data.local.AppDataBase;
import com.stela.stockapp.data.local.ProductsDao;
import com.stela.stockapp.data.local.TagsDao;
import com.stela.stockapp.data.model.pojo.ProductTagJoin;
import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.model.tag.TagEntity;
import com.stela.stockapp.ui.product.ProductCallback;

import androidx.lifecycle.LiveData;
import androidx.room.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.security.auth.callback.Callback;

public class ProductsRepository {
    private static ProductsRepository instance;
    private final ProductsDao productsDao;
    private final TagsDao tagsDao;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final HashMap<String, Product> cache = new HashMap<>();

    public ProductsRepository(AppDataBase db) {
        productsDao = db.productsDao();
        tagsDao = db.tagsDao();
    }

    public LiveData<Product> findById(int id) {
        return productsDao.findById(id);
    }

    public void getProductByTag(String tag, ProductCallback callback) {
        if (cache.containsKey(tag)) {
            callback.onSuccess(cache.get(tag));
            return;
        }

        new Thread(() -> {
            Product product = productsDao.getProductByTagId(tag);

            if (product != null) {
                cache.put(tag, product);
                callback.onSuccess(product);
            } else {
                callback.onError("Tag não cadastrada");
            }
        }).start();

    }

    public LiveData<List<ProductTagJoin>> getAll() {
        return productsDao.getAllProductsWithTag();
    }

    public void insertProductWithTag(Product product, Runnable callback) {
        executor.execute(() -> {
            TagEntity tagEntity = new TagEntity();
            tagEntity.setId(product.getTagId());

            tagsDao.insert(tagEntity);
            long id = productsDao.insert(product);
            product.setId((int) id);

            new android.os.Handler(android.os.Looper.getMainLooper())
                    .post(callback);
        });
    }

    public void updateProduct(Product product, Runnable callback) {
        executor.execute(() -> {
            productsDao.update(product);

            new android.os.Handler(android.os.Looper.getMainLooper())
                    .post(callback);
        });
    }

    public void deleteProduct(Product product) {
        executor.execute(() -> productsDao.delete(product));
    }

    public void deleteProductById(int id) {
        executor.execute(() -> productsDao.deleteProduct(id));
    }
}