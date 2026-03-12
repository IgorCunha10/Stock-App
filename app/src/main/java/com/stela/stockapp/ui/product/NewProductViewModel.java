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
    private MutableLiveData<String> nameError = new MutableLiveData<>();
    private MutableLiveData<String> priceError = new MutableLiveData<>();
    private MutableLiveData<String> descriptionError = new MutableLiveData<>();
    private MutableLiveData<String> tagError = new MutableLiveData<>();

    public LiveData<String> getNameError() {
        return nameError;
    }

    public LiveData<String> getPriceError() {
        return priceError;
    }

    public LiveData<String> getDescriptionError() {
        return descriptionError;
    }

    public LiveData<String> getTagError() {
        return tagError;
    }


    public NewProductViewModel(@NonNull Application application) {
        super(application);

        this.repository = new ProductsRepository(AppDataBase.getInstance(application));
        //
//        DbApplication app = (DbApplication) application;
//        repository = app.container.productsRepository;

    }


    public LiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    public LiveData<Product> getProduct(int id) {
        return repository.findById(id);
    }

    public boolean validateProduct(String name, String price, String description
    ) {

        boolean valid = true;

        if (name == null || name.trim().isEmpty()) {
            nameError.setValue("Nome Obrigatório");
            valid = false;
        } else {
            nameError.setValue(null);
        }

        if (price == null || price.trim().isEmpty()) {
            priceError.setValue("Preço Obrigatório");
            valid = false;
        } else {
            priceError.setValue(null);
        }

        if (description == null || description.trim().isEmpty()) {
            descriptionError.setValue("Descrição Obrigatória");
            valid = false;
        } else {
            descriptionError.setValue(null);
        }

//        if(tag == null || tag.trim().isEmpty()) {
//            tagError.setValue("Tag obrigatória");
//            valid = false;
//        } else {
//            tagError.setValue(null);
//        }

        return valid;
    }

    public void createProduct(String name, String description, String tag,
                              String price) {

        if (!validateProduct(name, description, price)) return;

        double parsedPrice = Double.parseDouble(price);

        Product product = new Product(description, parsedPrice,
                name, tag);

        saveProduct(product, false);
    }

    public void updateProduct(Product product, String name,
    String description, String price){
        if(!validateProduct(name, description, price)) return;

        double parsedPrice = Double.parseDouble(price);

        product.setProductName(name);
        product.setProductDescription(description);
        product.setProductPrice(parsedPrice);

        saveProduct(product,true);

    }


    public void saveProduct(Product product, boolean isEdit) {

        if (isEdit) {
            repository.updateProduct(product);
        } else {
            TagEntity tagEntity = new TagEntity();
            tagEntity.setId(product.getTagId());
            repository.insertProductWithTag(product);
        }

        saveSuccess.setValue(true);

    }


}


