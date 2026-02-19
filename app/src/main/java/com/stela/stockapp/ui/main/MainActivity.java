package com.stela.stockapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.stela.stockapp.R;
import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.ui.movimentation.MovimentationActivity;
import com.stela.stockapp.ui.product.NewProductActivity;
import com.stela.stockapp.ui.product.NewProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> addProductLauncher;
    private RecyclerView recyclerView;
    private List<Product> productList;
    private NewProductAdapter adapter;
    private FloatingActionButton fabNewProduct;
    private FloatingActionButton fabInfo;
    private MainViewModel mainViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_screen);

        mainViewModel = new ViewModelProvider(this)
                .get(MainViewModel.class);

        initInsets();
        initView();
        initRecycler();
        initListeners();
        initData();
        initActivityResults();

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
    }

    private void initRecycler() {
        productList = new ArrayList<>();
        adapter = new NewProductAdapter(this, productList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initData() {

      mainViewModel.getAllProducts().observe(this, products -> {
          adapter.setProductList(products);
      });

    }


    private void initActivityResults() {
        addProductLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.hasExtra("product")) {
                            Product product =
                                    (Product) data.getSerializableExtra("product");

                            mainViewModel.addProduct(product);
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
            Intent intent = new Intent(MainActivity.this, MovimentationActivity.class);
            startActivity(intent);
        });


        adapter.setOnItemActionListener(new NewProductAdapter.OnItemActionListener() {

            @Override
            public void onEditClick(Product product) {
                Intent intent = new Intent(MainActivity.this, NewProductActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(Product product) {
                mainViewModel.deleteProduct(product);
            }
        });
    }


}