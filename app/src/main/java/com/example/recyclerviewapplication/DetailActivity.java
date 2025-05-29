package com.example.recyclerviewapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import model.Product;

public class DetailActivity extends AppCompatActivity {

    private ImageView productDetailImage;
    private TextView productDetailName;
    private Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        productDetailImage = findViewById(R.id.productDetailImage);
        productDetailName = findViewById(R.id.productDetailName);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        Product product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            productDetailName.setText(product.name);
            productDetailImage.setImageResource(product.iconResId);
        }

        btnAddToCart.setOnClickListener(v -> {
            Toast.makeText(this, "Added to cart: " + product.name, Toast.LENGTH_SHORT).show();
        });
    }
}
