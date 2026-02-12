package com.stela.stockapp.data;

import android.app.Application;
import android.content.Context;

import com.stela.stockapp.data.javadb.AppDataBase;
import com.stela.stockapp.data.javadb.HistoryDao;
import com.stela.stockapp.data.javadb.ProductsDao;
import com.stela.stockapp.model.history.ProductHistory;
import com.stela.stockapp.model.product.Product;

import androidx.lifecycle.LiveData;

import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductsRepository {

    private static ProductsRepository instance;
    private final ProductsDao productsDao;
    private final HistoryRepository historyRepository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public static synchronized ProductsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ProductsRepository(context);
        }
        return instance;
    }

    private ProductsRepository(Context context) {
        AppDataBase db = AppDataBase.getInstance(context);
        productsDao = db.productsDao();
        historyRepository = HistoryRepository.getInstance(context);
    }

    public LiveData<List<Product>> getAll() {
        return productsDao.getAll();
    }

    private ProductHistory createHistory(Product product, String action, int movedQty) {
        ProductHistory history = new ProductHistory();
        history.productId = product.getProductId();
        history.productName = product.getProductName();
        history.productDescription = product.getProductDescription();
        history.productQuantity = product.getProductQuantity();
        history.productPrice = product.getProductPrice();
        history.action = action;
        history.movedQuantity = movedQty;
        history.timeStamp = System.currentTimeMillis();
        return history;
    }

    public void addProduct(Product product) {
        executor.execute(() -> {
            long id = productsDao.insert(product);
            product.setProductId((int) id);

            ProductHistory history = createHistory(product, "CREATE", 0);
            historyRepository.insertHistory(history);
        });
    }

    public void updateProduct(Product product) {
        executor.execute(() -> {
            productsDao.update(product);

            ProductHistory history = createHistory(product, "EDIT", 0);
            historyRepository.insertHistory(history);
        });
    }

    public void deleteProduct(Product product) {
        executor.execute(() -> {
            productsDao.delete(product);

            ProductHistory history = createHistory(product, "DELETE", 0);
            historyRepository.insertHistory(history);
        });
    }

    public void moveStock(Product product, int qty, boolean isIn) {
        executor.execute(() -> {
            if (isIn) {
                product.setProductQuantity(product.getProductQuantity() + qty);
            } else {
                product.setProductQuantity(product.getProductQuantity() - qty);
            }
            productsDao.update(product);

            ProductHistory history = createHistory(product, isIn ? "IN" : "OUT", qty);
            historyRepository.insertHistory(history);
        });
    }
}

