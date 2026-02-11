package com.stela.stockapp.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stela.stockapp.R;
import com.stela.stockapp.model.Product;
import com.stela.stockapp.view.adapter.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> addProductLauncher;
    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductAdapter adapter;
    private FloatingActionButton fabNewProduct;
    private FloatingActionButton fabInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_screen);

        initView();
        initListeners();
        initInsets();
        initRecycler();
        initActivityResults();

}

private void initInsets(){
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
}

private void initRecycler(){
    productList = new ArrayList<>();
    adapter = new ProductAdapter(this, productList);

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
}

private void initActivityResults(){
    addProductLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result  -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.hasExtra("product")) {
                        Product product = (Product) data.getSerializableExtra("product");
                        productList.add(product);
                        adapter.notifyItemInserted(productList.size() - 1);
                    }

                }
            });

}
private void initListeners() {

        fabNewProduct.setOnClickListener(view -> {
        Intent intent = new Intent(MainActivity.this, NewProductActivity.class);
        addProductLauncher.launch(intent);
    });

        fabInfo.setOnClickListener(view -> {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        addProductLauncher.launch(intent);
    });

}

    }