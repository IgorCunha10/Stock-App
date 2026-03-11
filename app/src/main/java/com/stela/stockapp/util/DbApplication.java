package com.stela.stockapp.util;

import android.app.Application;

public class DbApplication extends Application {

    public AppContainer container;

    @Override
    public void onCreate() {
        super.onCreate();

        container = new AppContainer(this);

    }
}
