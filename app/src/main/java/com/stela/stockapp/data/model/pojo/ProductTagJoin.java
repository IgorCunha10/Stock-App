package com.stela.stockapp.data.model.pojo;

import androidx.room.ColumnInfo;

public class ProductTagJoin {

    @ColumnInfo(name = "product_id")
    private int productId;
    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "product_description")
    private String description;

    @ColumnInfo(name = "product_price")
    private double price;

    @ColumnInfo(name = "tag_id")
    private String tagId;

    @ColumnInfo(name = "tag_validity")
    private long validityStamp;

    @ColumnInfo(name = "tag_fabrication")
    private long fabricationStamp;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public long getFabricationStamp() {
        return fabricationStamp;
    }

    public void setFabricationStamp(long fabricationStamp) {
        this.fabricationStamp = fabricationStamp;
    }

    public long getValidityStamp() {
        return validityStamp;
    }

    public void setValidityStamp(long validityStamp) {
        this.validityStamp = validityStamp;
    }
}
