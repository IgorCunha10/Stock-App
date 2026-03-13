package com.stela.stockapp.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.stela.stockapp.data.model.history.ProductHistory;
import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.data.model.tag.TagEntity;

@Database(
        entities = {Product.class, TagEntity.class, ProductHistory.class},
        version = 1
)
public abstract class AppDataBase extends RoomDatabase {

    public abstract ProductsDao productsDao();

    //public abstract HistoryDao historyDao();
    public abstract TagsDao tagsDao();

    private static AppDataBase instance;

    static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("DROP TABLE IF EXISTS `products`");
            database.execSQL(
                    "CREATE TABLE products (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "product_description TEXT, " +
                            "product_price REAL NOT NULL, " +
                            "product_name TEXT, " +
                            "tag_id TEXT, " +
                            "FOREIGN KEY (tag_id) REFERENCES tags(id) " +
                            "ON UPDATE NO ACTION ON DELETE NO ACTION" +
                            ");"
            );

        }
    };

    public static synchronized AppDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDataBase.class,
                            "estoque_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}