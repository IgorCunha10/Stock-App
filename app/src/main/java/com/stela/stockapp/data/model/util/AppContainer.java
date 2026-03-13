package com.stela.stockapp.data.model.util;

import android.content.Context;

import com.stela.stockapp.data.local.AppDataBase;
import com.stela.stockapp.data.repository.ProductsRepository;

public class AppContainer {

    private final AppDataBase database;
    public final ProductsRepository productsRepository;

    public AppContainer(Context context) {
        database = AppDataBase.getInstance(context.getApplicationContext());
        productsRepository = new ProductsRepository(database);
    }

}
