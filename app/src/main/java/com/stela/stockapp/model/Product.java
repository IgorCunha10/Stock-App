package com.stela.stockapp.model;

public class Product {
    public String productName, productDescription;
    public int productQuantity;

    public Product() {

    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
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



}
