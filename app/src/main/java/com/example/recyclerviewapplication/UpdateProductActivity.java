package com.example.recyclerviewapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import database.ProductDAO;
import model.Product;

public class UpdateProductActivity extends AppCompatActivity {

    private Spinner spinnerProducts;
    private EditText edtProductId, edtName, edtDescription, edtImage, edtPrice, edtStock;
    private Button btnUpdate;

    private ProductDAO productDAO;
    private List<Product> productList;
    private Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        productDAO = new ProductDAO(this);

        spinnerProducts = findViewById(R.id.spinnerProducts);
        edtProductId = findViewById(R.id.edtProductId);
        edtName = findViewById(R.id.edtName);
        edtDescription = findViewById(R.id.edtDescription);
        edtImage = findViewById(R.id.edtImage);
        edtPrice = findViewById(R.id.edtPrice);
        edtStock = findViewById(R.id.edtStock);
        btnUpdate = findViewById(R.id.btnUpdate);

        loadProductsIntoSpinner();

        spinnerProducts.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                selectedProduct = productList.get(position);
                populateProductDetails(selectedProduct);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Do nothing
            }
        });

        btnUpdate.setOnClickListener(v -> updateProduct());
    }

    private void loadProductsIntoSpinner() {
        productList = productDAO.getAllProducts();

        ArrayAdapter<Product> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, productList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerProducts.setAdapter(adapter);
    }

    private void populateProductDetails(Product product) {
        edtProductId.setText(String.valueOf(product.getId()));
        edtName.setText(product.getName());
        edtDescription.setText(product.getDescript());
        edtImage.setText(product.getImg());
        edtPrice.setText(String.valueOf(product.getPrice()));
        edtStock.setText(String.valueOf(product.getStock()));
    }

    private void updateProduct() {
        if (selectedProduct == null) {
            Toast.makeText(this, "No product selected!", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedProduct.setName(edtName.getText().toString().trim());
        selectedProduct.setDescript(edtDescription.getText().toString().trim());
        selectedProduct.setImg(edtImage.getText().toString().trim());
        selectedProduct.setPrice(Double.parseDouble(edtPrice.getText().toString().trim()));
        selectedProduct.setStock(Integer.parseInt(edtStock.getText().toString().trim()));

        boolean success = productDAO.updateProduct(selectedProduct);

        if (success) {
            Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
            loadProductsIntoSpinner();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}
