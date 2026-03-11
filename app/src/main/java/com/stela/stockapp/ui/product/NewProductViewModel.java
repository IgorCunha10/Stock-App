package com.stela.stockapp.ui.product;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.stela.stockapp.data.local.AppDataBase;
import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.model.tag.TagEntity;
import com.stela.stockapp.data.repository.ProductsRepository;
import com.stela.stockapp.data.model.util.DbApplication;

public class NewProductViewModel extends AndroidViewModel {

    private final ProductsRepository repository;
    private final MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();

    public NewProductViewModel(@NonNull Application application) {
        super(application);

        this.repository = new ProductsRepository(AppDataBase.getInstance(application));
        //
//        DbApplication app = (DbApplication) application;
//        repository = app.container.productsRepository;

    }


    public LiveData<Boolean> getSaveSuccess(){
        return saveSuccess;
    }

    public LiveData<Product> getProduct(int id){
        return repository.findById(id);
    }

    public void saveProduct(Product product, boolean isEdit) {

        if(isEdit) {
            repository.updateProduct(product);
        } else {
            TagEntity tagEntity = new TagEntity();
            tagEntity.setId(product.getTagId());
            repository.insertProductWithTag(product, tagEntity);
        }

        saveSuccess.setValue(true);

    }



    }


