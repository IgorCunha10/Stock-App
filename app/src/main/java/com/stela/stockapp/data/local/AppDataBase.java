package com.stela.stockapp.data.local;

import static com.stela.stockapp.data.model.history.ProductHistory.MIGRATION_2_3;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.stela.stockapp.data.model.history.ProductHistory;
import com.stela.stockapp.data.model.product.Product;

@Database(entities = {Product.class, ProductHistory.class}, version = 3)
public abstract class AppDataBase extends RoomDatabase {

    public abstract ProductsDao productsDao();

    public abstract HistoryDao historyDao();

    private static AppDataBase instance;


    public static synchronized AppDataBase getInstance(Context context) {

        if (instance == null) {
            synchronized (AppDataBase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDataBase.class,
                                    "estoque_db"
                            ).addMigrations(MIGRATION_2_3)

                            .build();
                }
            }
        }
        return instance;
    }


}
