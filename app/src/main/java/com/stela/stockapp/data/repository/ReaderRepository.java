package com.stela.stockapp.data.repository;

import android.app.Activity;
import android.util.Log;

import com.grotg.hpp.otglibrary.exception.ReaderException;
import com.grotg.hpp.otglibrary.otgreader.OtgReader;
import com.grotg.hpp.otglibrary.param.EpcBean;
import com.stela.stockapp.data.model.product.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReaderRepository {

    private final OtgReader otgReader;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Map<String, Product> productCache = new HashMap<>();

    public interface TagCallback {
        void onTagRead(EpcBean epcBean);
    }

    public ReaderRepository(Activity activity) {
        otgReader = new OtgReader(activity);
    }

    public void connect(OtgReader.ConnectCallback callback) {
        otgReader.connect(callback);
    }

    public void startScan() {
        try {
            otgReader.ScanTags();
        } catch (ReaderException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopScan() {
        try {
            otgReader.StopScan();
        } catch (ReaderException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOnTagRead(TagCallback callback) {
        otgReader.setreadTagDataCallback(callback::onTagRead);
    }

    public void release() {
        try {
            otgReader.StopScan();
        } catch (ReaderException ignored) {
        }
    }

    public void loadProductOnCache(List<Product> products) {
        executor.execute(() -> {
            productCache.clear();
            for(Product p : products) {
                productCache.put(p.getTagId().trim().toUpperCase(), p);
            }
        });
    }

    public interface ProductResultCallback {
        void onSuccess(Product product);
        void onError(String message);
    }

    public void findProductByEpc(String epc, ProductResultCallback callback) {
        executor.execute(() -> {
            Product product = productCache.get(epc.trim().toUpperCase());

            new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
            {
                if(product != null) {
                    callback.onSuccess(product);
                } else {
                    callback.onError("Tag não cadastrada.");
                }
            });
        });

    }
}