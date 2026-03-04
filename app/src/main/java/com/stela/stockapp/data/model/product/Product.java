package com.stela.stockapp.data.model.product;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "products")
public class Product implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int productId;
    public String productName, productDescription;
    public double productPrice;
    private String productTag;

    public Product(String productName,  String productDescription, String productTag,
                   double productPrice) {

        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productTag = productTag;
    }

    public Product() {
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductTag(String productTag) {
        this.productTag = productTag;
    }

    public String getProductTag(){
        return productTag;
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

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getProductPrice() {
        return productPrice;
    }


}
