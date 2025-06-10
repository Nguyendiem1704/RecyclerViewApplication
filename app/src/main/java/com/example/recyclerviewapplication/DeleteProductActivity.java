package com.example.recyclerviewapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import database.ProductDAO;

public class DeleteProductActivity extends AppCompatActivity {

    private EditText edtProductId;
    private Button btnDelete;
    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_product);

        productDAO = new ProductDAO(this);

        edtProductId = findViewById(R.id.edtProductId);
        btnDelete = findViewById(R.id.btnDelete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });
    }

    private void deleteProduct() {
        try {
            int productId = Integer.parseInt(edtProductId.getText().toString());
            productDAO.deleteProduct(productId);

            Toast.makeText(this, "Product deleted successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Error deleting product", Toast.LENGTH_SHORT).show();
        }
    }
}
