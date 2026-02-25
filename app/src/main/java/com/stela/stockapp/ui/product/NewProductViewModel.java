package com.stela.stockapp.ui.product;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.repository.ProductsRepository;

public class NewProductViewModel extends AndroidViewModel {

    private final ProductsRepository repository;
    private final MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();

    public NewProductViewModel(@NonNull Application application) {
        super(application);
        repository = ProductsRepository.getInstance(application);

    }

    public LiveData<Boolean> getSaveSuccess(){
        return saveSuccess;
    }

    public void saveProduct(Product product, boolean isEdit) {

        if(isEdit) {
            repository.updateProduct(product);
        } else {
            repository.addProduct(product);
        }

        saveSuccess.setValue(true);

    }

}
