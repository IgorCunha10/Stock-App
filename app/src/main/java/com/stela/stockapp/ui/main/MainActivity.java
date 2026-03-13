package com.stela.stockapp.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stela.stockapp.R;
import com.stela.stockapp.data.model.product.ProductDto;
import com.stela.stockapp.data.model.util.Mapper;
import com.stela.stockapp.ui.product.NewProductActivity;
import com.stela.stockapp.ui.product.ProductAdapter;
import com.stela.stockapp.ui.reader.ReaderActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ProductDto> productList;
    private ProductAdapter adapter;
    private FloatingActionButton fabNewProduct;
    private FloatingActionButton fabInfo;
    private FloatingActionButton fabScanner;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_screen);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        initInsets();
        initView();
        initRecycler();
        initListeners();
        initData();
    }

    private void initInsets() {
        ConstraintLayout mainLayout = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bottomInset + 24);
            return insets;
        });
    }

    private void initView() {
        fabNewProduct = findViewById(R.id.fabNewProduct);
        fabInfo = findViewById(R.id.fabInfo);
        recyclerView = findViewById(R.id.recyclerView);
        fabScanner = findViewById(R.id.fabScanner);
    }

    private void initRecycler() {
        productList = new ArrayList<>();
        adapter = new ProductAdapter(productList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        mainViewModel.getAllProducts().observe(this, joins -> {
            List<ProductDto> products = Mapper.toDtoList(joins);
            adapter.setProductList(products);
        });
    }

    private void initListeners() {
        fabNewProduct.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewProductActivity.class);
            startActivity(intent);
        });

        fabScanner.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ReaderActivity.class);
            startActivity(intent);
        });

        adapter.setOnItemActionListener(new ProductAdapter.OnItemActionListener() {
            @Override
            public void onEditClick(ProductDto product) {
                Intent intent = new Intent(MainActivity.this, NewProductActivity.class);
                intent.putExtra("product_id", product.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(ProductDto product) {
                mainViewModel.deleteProductById(product.getId());
            }
        });
    }
}