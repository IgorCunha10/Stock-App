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
    private TextView pageName;
    private Button saveButton, scanTagBtn;
    private boolean isEdit = false;
    private NewProductViewModel viewModel;

    private ActivityResultLauncher<Intent> scanLauncher;
    private EditText editTextTag;
    private TextView txtSelectedTag;
    private String selectedTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_product);

        scanLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                selectedTag = result.getData().getStringExtra("EXTRA_TAG");

                                if (selectedTag != null) {
                                    txtSelectedTag.setText("Tag: " + selectedTag);
                                }
                            }
                        }
                );



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
        edtPrice = findViewById(R.id.edtPrice);
        pageName = findViewById(R.id.pageName);
        saveButton = findViewById(R.id.btnSave);
        scanTagBtn = findViewById((R.id.scanTagBtn));
        txtSelectedTag = findViewById(R.id.txtSelectedTag);
    }

    private void loadData() {
        edtName.setText(actualProduct.getProductName());
        edtDescription.setText(actualProduct.getProductDescription());
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
            double price = 0;
            if (selectedTag == null || selectedTag.isBlank()) {
                Toast.makeText(this, "Scan a tag first", Toast.LENGTH_SHORT).show();
                return;
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
            product.setProductTag(selectedTag);

            if (isEdit) {
                actualProduct.setProductName(name);
                actualProduct.setProductDescription(description);
                actualProduct.setProductPrice(price);

                viewModel.saveProduct(actualProduct, true);
                Toast.makeText(this, "Product updated succesfully",
                        Toast.LENGTH_SHORT).show();

            } else {
                Product newProduct = new Product(
                        name,
                        description,
                        selectedTag,
                        price
                );

            }

            viewModel.saveProduct(product, isEdit);

            clearForm();

        });

        scanTagBtn.setOnClickListener(view -> {
            Intent intent = new Intent(
                    NewProductActivity.this,
                    ReaderActivity.class
            );

            intent.putExtra("EXTRA_MODE", "MODE_SELECT");
            scanLauncher.launch(intent);
        });

    }

    private void clearForm() {
        edtName.setText("");
        edtDescription.setText("");
    }


}