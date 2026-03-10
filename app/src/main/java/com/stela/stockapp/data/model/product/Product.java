package com.stela.stockapp.data.model.product;

import androidx.lifecycle.LiveData;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.stela.stockapp.data.model.tag.TagEntity;

import java.io.Serializable;


@Entity(
        tableName = "products",
        foreignKeys = @ForeignKey(
                entity = TagEntity.class,
                parentColumns = "id",
                childColumns = "tag_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("tag_id")}
)
public class Product implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "product_description")
    private String productDescription;

    @ColumnInfo(name = "product_price")
    private double productPrice;

    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "tag_id")
    private String tagId;

    public Product(String productDescription, double productPrice,
                   String productName, String tagId) {
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productName = productName;
        this.tagId = tagId;
    }


    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    /* public void setProductTag(String productTag) {
        this.productTag = productTag;
    }

    public String getProductTag(){
        return productTag;
    } */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setTagId(String tagId){
        this.tagId = tagId;

    }
    public String getTagId(){
        return tagId;
    }





}
