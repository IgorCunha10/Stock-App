package com.stela.stockapp.data.repository;

import android.app.Activity;
import android.app.Application;

import androidx.lifecycle.LiveData;

import com.stela.stockapp.data.local.AppDataBase;
import com.stela.stockapp.data.local.ProductsDao;
import com.stela.stockapp.data.local.TagsDao;
import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.model.tag.TagEntity;

import java.lang.reflect.Executable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TagRepository {

    private final TagsDao tagsDao;

    private final LiveData<List<TagEntity>> allTags;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final ProductsDao productDao;


    public TagRepository(AppDataBase db) {

        tagsDao = db.tagsDao();
        productDao = db.productsDao();
        allTags = tagsDao.getAll();
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }



    public LiveData<List<TagEntity>> getAllTags(){
        return allTags;
    }

    public Product getProductByTag(String tag) {
        return tagsDao.findProductByTag(tag);
    }

    public void insert(TagEntity tag) {
        executor.execute(() -> {
            tagsDao.insert(tag);
        });
    }

    public void update(TagEntity tag) {
        executor.execute(() -> {
            tagsDao.update(tag);
        });
    }

    public void delete(TagEntity tag) {
        executor.execute(() -> {
                tagsDao.delete(tag);
        });

    }

    public void deleteAll() {
        executor.execute(tagsDao::deleteAll);
    }

    public TagEntity findById(String tagId) {
        return tagsDao.findById(tagId);
    }


}
