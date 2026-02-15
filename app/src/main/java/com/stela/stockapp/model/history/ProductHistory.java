package com.stela.stockapp.model.history;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Entity(
        tableName = "products_history"
)
public class ProductHistory {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int productId;

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

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
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

    public String productName;
    public String productDescription;
    public int productQuantity;
    public double productPrice;

    public String action;

    public int movedQuantity;
    public long timeStamp;



    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `products_history` (" +
                            "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "`productId` INTEGER NOT NULL, " +
                            "`productName` TEXT, " +
                            "`productDescription` TEXT, " +
                            "`productQuantity` INTEGER NOT NULL, " +
                            "`productPrice` REAL NOT NULL, " +
                            "`action` TEXT, " +
                            "`movedQuantity` INTEGER NOT NULL, " +
                            "`timeStamp` INTEGER NOT NULL)"
            );
        }
    };


}
