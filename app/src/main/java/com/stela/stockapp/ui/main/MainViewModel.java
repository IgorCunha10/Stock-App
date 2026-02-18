package com.stela.stockapp.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.repository.ProductsRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final ProductsRepository repository;
    private final LiveData<List<Product>> allProducts;

    public MainViewModel(@NonNull Application application) {
        super(application);

        repository = ProductsRepository.getInstance(application);
        allProducts = repository.getAll();

    }

    public LiveData<List<Product>> getAllProducts() {
        return allProducts;
    }

    public void addProduct(Product product) {
        repository.addProduct(product);
    }

    public void deleteProduct(Product product) {
        repository.deleteProduct(product);
    }

}
