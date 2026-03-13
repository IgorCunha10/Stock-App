package com.stela.stockapp.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.stela.stockapp.R;
import com.stela.stockapp.data.model.product.Product;
import com.stela.stockapp.ui.reader.ReaderActivity;

public class NewProductActivity extends AppCompatActivity {

    private Product actualProduct;
    private EditText edtName, edtDescription, edtPrice;
    private TextView pageName, txtSelectedTag;
    private Button saveButton, scanTagBtn;
    private boolean isEdit = false;
    private NewProductViewModel viewModel;
    private ActivityResultLauncher<Intent> scanLauncher;
    private String selectedTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_product);

        initView();
        initScanLauncher();
        initViewModel();
        handleIntentData();
        initListeners();
    }

    private void initView() {
        edtName = findViewById(R.id.edtName);
        edtDescription = findViewById(R.id.edtDescription);
        edtPrice = findViewById(R.id.edtPrice);
        pageName = findViewById(R.id.pageName);
        saveButton = findViewById(R.id.btnSave);
        scanTagBtn = findViewById(R.id.scanTagBtn);
        txtSelectedTag = findViewById(R.id.txtSelectedTag);
    }

    private void initScanLauncher() {
        scanLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedTag = result.getData()
                                .getStringExtra(ReaderActivity.EXTRA_TAG);

                        updateTagUI();
                    }
                }
        );
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this)
                .get(NewProductViewModel.class);

        viewModel.getSaveSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this,
                        isEdit ? "Product updated successfully"
                                : "Product created successfully",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        if (intent == null) return;

        if (intent.hasExtra("product_id")) {
            isEdit = true;
            int productId = intent.getIntExtra("product_id", -1);

            if (productId != -1) {
                viewModel.getProduct(productId).observe(this, product -> {
                    if (product != null) {
                        actualProduct = product;
                        loadData();
                        configEditScreen();
                    }
                });
            }
        }

        if (intent.hasExtra(ReaderActivity.EXTRA_TAG)) {
            selectedTag = intent.getStringExtra(ReaderActivity.EXTRA_TAG);
            updateTagUI();
        }
    }

    private void updateTagUI() {
        if (selectedTag != null && !selectedTag.isBlank()) {
            txtSelectedTag.setText("Tag: " + selectedTag);
        }
    }

    private void loadData() {
        edtName.setText(actualProduct.getProductName());
        edtDescription.setText(actualProduct.getProductDescription());
        edtPrice.setText(String.valueOf(actualProduct.getProductPrice()));

        //selectedTag = actualProduct.getProductTag();
        updateTagUI();
    }

    private void configEditScreen() {
        pageName.setText("Edit Product");
        saveButton.setText("Save Changes");
    }

    private void initListeners() {

        saveButton.setOnClickListener(v -> {

            String name = edtName.getText().toString();
            String description = edtDescription.getText().toString();
            String price = edtPrice.getText().toString();


            if (isEdit) {
                viewModel.updateProduct(actualProduct, name, selectedTag, price);

            } else {
                viewModel.createProduct(name, description, selectedTag, price);

            }
        });

        scanTagBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    NewProductActivity.this,
                    ReaderActivity.class
            );

            intent.putExtra(
                    ReaderActivity.EXTRA_MODE,
                    ReaderActivity.MODE_SELECT
            );

            scanLauncher.launch(intent);
        });
    }
}