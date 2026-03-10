package com.stela.stockapp.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.model.tag.TagEntity;

import java.util.List;

@Dao
public interface TagsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(TagEntity tag);

    @Query("SELECT * FROM products WHERE tag_id = :tag LIMIT 1")
    LiveData<Product> getProductByTag(String tag);

    @Query("SELECT * FROM products WHERE tag_id = :tag LIMIT 1")
    Product findProductByTag(String tag);

    @Update
    void update(TagEntity tag);

    @Delete
    void delete(TagEntity tag);

    @Query("DELETE FROM tags")
    void deleteAll();

    @Query("SELECT * FROM tags")
    LiveData<List<TagEntity>> getAll();

    @Query("SELECT * FROM tags WHERE id = :tagId LIMIT 1")
    TagEntity findById(String tagId);
}
