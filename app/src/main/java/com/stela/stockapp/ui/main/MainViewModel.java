package com.stela.stockapp.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.stela.stockapp.data.local.AppDataBase;
import com.stela.stockapp.data.model.pojo.ProductTagJoin;
import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.model.tag.TagEntity;
import com.stela.stockapp.data.repository.ProductsRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final ProductsRepository productRepository;
    private final LiveData<List<ProductTagJoin>> allProducts;

    public MainViewModel(@NonNull Application application) {
        super(application);

        AppDataBase db = AppDataBase.getInstance(application);
        productRepository = new ProductsRepository(db);
        allProducts = productRepository.getAll();
    }

    public LiveData<List<ProductTagJoin>> getAllProducts() {
        return allProducts;
    }

    public void addProduct(Product product) {

        productRepository.insertProductWithTag(product, () ->{});
    }


    public void deleteProduct(Product product) {
        productRepository.deleteProduct(product);
    }

    public void deleteProductById(int id) {
        productRepository.deleteProductById(id);
    }
}