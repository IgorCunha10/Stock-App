package com.stela.stockapp.data.javadb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.stela.stockapp.model.Product;

@Database(entities = {Product.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract ProductsDao productsDao();

    private static AppDataBase instance;

    public static synchronized AppDataBase getInstance(Context context) {

        if(instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDataBase.class,
                    "stock database"
            ).build();
        }
        return instance;
    }


}
