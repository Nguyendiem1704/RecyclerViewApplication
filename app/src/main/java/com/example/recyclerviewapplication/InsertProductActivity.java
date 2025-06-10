package com.example.recyclerviewapplication;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import database.ProductDAO;
import model.Product;

public class InsertProductActivity extends AppCompatActivity {

    private EditText edtName, edtDescription, edtImage, edtPrice, edtStock;
    private Button btnInsert;
    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);

        productDAO = new ProductDAO(this);

        edtName = findViewById(R.id.edtName);
        edtDescription = findViewById(R.id.edtDescription);
        edtImage = findViewById(R.id.edtImage);
        edtPrice = findViewById(R.id.edtPrice);
        edtStock = findViewById(R.id.edtStock);
        btnInsert = findViewById(R.id.btnInsert);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertProduct();
            }
        });
    }

    private void insertProduct() {
        String name = edtName.getText().toString();
        String desc = edtDescription.getText().toString();
        String img = edtImage.getText().toString();
        double price = Double.parseDouble(edtPrice.getText().toString());
        int stock = Integer.parseInt(edtStock.getText().toString());

        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setDescript(desc);
        newProduct.setImg(img);
        newProduct.setPrice(price);
        newProduct.setStock(stock);

        productDAO.insertProduct(newProduct);

        Toast.makeText(this, "Product inserted", Toast.LENGTH_SHORT).show();
        finish();
    }
}
