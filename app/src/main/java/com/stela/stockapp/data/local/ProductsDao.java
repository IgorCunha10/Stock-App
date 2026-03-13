package com.stela.stockapp.data.local;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.stela.stockapp.data.model.pojo.ProductTagJoin;
import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.model.tag.TagEntity;

import java.util.List;

@Dao
public interface ProductsDao {

    @Query("SELECT * FROM products")
    List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    LiveData<Product> findById(int id);


    @Query("SELECT * FROM products WHERE tag_id = :tagId LIMIT 1")
    Product getProductByTagId(String tagId);

    @Query("SELECT products.id AS product_id, " +
            "products.product_name, " +
            "products.product_description, " +
            "products.product_price, " +
            "tags.id AS tag_id, " +
            "tags.tag_validity, " +
            "tags.tag_fabrication " +
            "FROM products " +
            "LEFT JOIN tags ON products.tag_id = tags.id")
    LiveData<List<ProductTagJoin>> getAllProductsWithTag();

    @Query("SELECT products.id AS product_id, " +
            "products.product_name, " +
            "products.product_description, " +
            "products.product_price, " +
            "tags.id AS tag_id, " +
            "tags.tag_validity, " +
            "tags.tag_fabrication " +
            "FROM products " +
            "INNER JOIN tags ON products.tag_id = tags.id " +
            "WHERE products.id = :productId")
    LiveData<List<ProductTagJoin>> getProductsWithTagById(int productId);

    @Query("SELECT products.id AS product_id, " +
            "products.product_name, " +
            "products.product_description, " +
            "products.product_price, " +
            "tags.id AS tag_id, " +
            "tags.tag_validity, " +
            "tags.tag_fabrication " +
            "FROM products " +
            "INNER JOIN tags ON products.tag_id = tags.id " +
            "WHERE tags.id = :tagId")
    LiveData<List<ProductTagJoin>> getProductsByTagId(int tagId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProduct(Product product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTag(TagEntity tag);

    @Update
    void updateProduct(Product product);

    @Query("DELETE from products WHERE id = :productId")
    void deleteProduct(int productId);


    // @Query("SELECT * FROM products")
    //LiveData<List<Product>> getAll();

   /* @Query("SELECT * FROM products WHERE productTag = :tag LIMIT 1")
    LiveData<Product> getProductByTagSync(String tag); */

    @Insert
    long insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);


}
