package com.stela.stockapp.data.model.product;

public class ProductDto {
    private int id;
    private String productDescription, productName;
    private double productPrice;
    private String tagNumber;

    public ProductDto(int id, String tag, String productName,String productDescription,
                      double productPrice) {

        this.id = id;
        this.tagNumber = tag;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
    }

    public int getId(){
        return id;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public String getProductName(){
        return productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

}
