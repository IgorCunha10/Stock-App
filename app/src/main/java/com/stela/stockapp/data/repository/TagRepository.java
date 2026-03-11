package com.stela.stockapp.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.stela.stockapp.data.local.AppDataBase;
import com.stela.stockapp.data.local.ProductsDao;
import com.stela.stockapp.data.local.TagsDao;
import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.model.tag.TagEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

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

    public LiveData<List<Product>> getAllProducts() {
        MutableLiveData<List<Product>> liveData = new MutableLiveData<>();
        executor.execute(() -> {
            List<Product> products = productDao.getAllProducts();
            liveData.postValue(products);
        });

        return liveData;
    }


    public LiveData<List<TagEntity>> getAllTags(){
        return allTags;
    }

    public LiveData<Product> getProductByTag(String tag) {
       MutableLiveData<Product> liveData = new MutableLiveData<>();
       executor.execute(() -> {
           Product product = tagsDao.findProductByTag(tag);
           liveData.postValue(product);
       });
       return liveData;
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

    public void findById(String tagId, Consumer<TagEntity> callback) {
        executor.execute(() -> {
            TagEntity tag = tagsDao.findById(tagId);
            callback.accept(tag);
        });
    }

}
