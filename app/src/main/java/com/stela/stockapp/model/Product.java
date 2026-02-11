package com.stela.stockapp.model;

import java.io.Serializable;

public class Product implements Serializable {
    public String productName, productDescription;
    public int productQuantity, productId, productPrice;

    public Product() {

    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getProductQuantity () {
        return productQuantity;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductPrice() {
        return productPrice;
    }



}
