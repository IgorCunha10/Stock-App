package com.stela.stockapp.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.stela.stockapp.R;
import com.stela.stockapp.data.repository.ProductsRepository;
import com.stela.stockapp.data.model.product.Product;

public class NewProductActivity extends AppCompatActivity {

    private Product actualProduct;
    private EditText edtName, edtDescription, edtId, edtQuantity, edtPrice, edtDate;
    private TextView pageName;
    private Button saveButton;
    private boolean isEdit = false;

    private NewProductViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_product);

        viewModel = new ViewModelProvider(this)
                .get(NewProductViewModel.class);

        viewModel.getSaveSuccess().observe(this, success -> {
            if (success) {
                Toast.makeText(this,
                        isEdit ? "Product updated successfully" : "Product created successfully",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        initView();
        handleIntentDats();
        initListeners();

    }

    private void handleIntentDats() {
        Intent intent = getIntent();
        if (intent.hasExtra("product")) {
            isEdit = true;
            actualProduct = (Product) intent.getSerializableExtra("product");


            loadData();
            configEditScreen();
        }
    }

    private void initView() {
        edtName = findViewById(R.id.edtName);
        edtDescription = findViewById(R.id.edtDescription);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtPrice = findViewById(R.id.edtPrice);
        pageName = findViewById(R.id.pageName);
        saveButton = findViewById(R.id.btnSave);
    }

    private void loadData() {
        edtName.setText(actualProduct.getProductName());
        edtDescription.setText(actualProduct.getProductDescription());
        edtQuantity.setText(String.valueOf(actualProduct.getProductQuantity()));
        edtPrice.setText(String.valueOf(actualProduct.getProductPrice()));

    }

    private void configEditScreen() {
        pageName.setText("Edit Product");
        saveButton.setText("Save Changes");
    }


    private void initListeners() {
        saveButton.setOnClickListener(v -> {
            String name = edtName.getText().toString();
            String description = edtDescription.getText().toString();
            int quantity = 0;
            double price = 0;

            String quantityStr = edtQuantity.getText().toString();
            if (!quantityStr.isBlank()) {
                try {
                    quantity = Integer.parseInt(quantityStr);
                } catch (Exception e) {
                    Toast.makeText(this, "Insert a valid Number", Toast.LENGTH_SHORT).show();
                }
            }

            String priceStr = edtPrice.getText().toString();

            if (!priceStr.isBlank()) {
                try {
                    price = Double.parseDouble(priceStr);
                } catch (Exception e) {
                    Toast.makeText(this, "Insert a valid Price", Toast.LENGTH_SHORT).show();
                }
            }

            if (name.isBlank() || description.isBlank()) {
                Toast.makeText(this, "Complete all informations", Toast.LENGTH_SHORT).show();
            }

            Product product = new Product();
            product.setProductPrice(price);
            product.setProductName(name);
            product.setProductDescription(description);
            product.setProductQuantity(quantity);

            if (isEdit) {
                actualProduct.setProductName(name);
                actualProduct.setProductDescription(description);
                actualProduct.setProductQuantity(quantity);
                actualProduct.setProductPrice(price);

                viewModel.saveProduct(actualProduct, true);
                Toast.makeText(this, "Product updated succesfully", Toast.LENGTH_SHORT).show();

            } else {
                Product newProduct = new Product(
                        name,
                        description,
                        quantity,
                        price
                );

            }

            viewModel.saveProduct(product, isEdit);

            clearForm();

        });


    }

    private void clearForm() {
        edtName.setText("");
        edtDescription.setText("");
    }


}