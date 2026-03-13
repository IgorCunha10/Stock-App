package com.stela.stockapp.data.model.history;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.stela.stockapp.data.model.product.Product;

@Entity(
        tableName = "products_history",
        foreignKeys = @ForeignKey(
                entity = Product.class,
                parentColumns = "id",
                childColumns = "product_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("product_id")}
)
public class ProductHistory {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "product_id")
    public int productId;

    @ColumnInfo(name = "product_action")
    public String productAction;

    @ColumnInfo(name = "moved_quantity")
    public int movedQuantity;

    @ColumnInfo(name = "time_stamp")
    public long timeStamp;

    @ColumnInfo(name = "product_tag")
    public String productTag;

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductAction() {
        return productAction;
    }

    public void setProductAction(String productAction) {
        this.productAction = productAction;
    }

    public int getMovedQuantity() {
        return movedQuantity;
    }

    public void setMovedQuantity(int movedQuantity) {
        this.movedQuantity = movedQuantity;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getProductTag() {
        return productTag;
    }

    public void setProductTag(String productTag) {
        this.productTag = productTag;
    }
}