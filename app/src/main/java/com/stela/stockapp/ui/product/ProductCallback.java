package com.stela.stockapp.ui.product;

import com.stela.stockapp.data.model.product.Product;

public interface ProductCallback {
    void onSuccess(Product product);
    void onError(String message);
}
