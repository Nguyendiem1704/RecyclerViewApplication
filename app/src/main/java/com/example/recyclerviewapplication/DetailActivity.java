package com.example.recyclerviewapplication;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import model.CartManager;
import model.Product;

public class DetailActivity extends AppCompatActivity {

    private ImageView productDetailImage;
    private TextView productDetailName, productDetailDesc, productDetailPrice, productDetailStock;
    private TextView tvStorePhone;
    private Button btnAddToCart;
    private Product product;

    public static final String CHANNEL_ID = "cart_notification_channel";
    public static final int NOTIFICATION_ID = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(v -> finish());

        // Ánh xạ view
        productDetailImage = findViewById(R.id.productDetailImage);
        productDetailName = findViewById(R.id.productDetailName);
        productDetailDesc = findViewById(R.id.productDetailDesc);
        productDetailPrice = findViewById(R.id.productDetailPrice);
        productDetailStock = findViewById(R.id.productDetailStock);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        tvStorePhone = findViewById(R.id.tvStorePhone);

        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            getSupportActionBar().setTitle(product.getName());

            productDetailName.setText(product.getName());
            productDetailDesc.setText(product.getDescript());
            productDetailPrice.setText("Price: $" + product.getPrice());
            productDetailStock.setText("Stock: " + product.getStock());

            // Load image từ drawable
            int resId = getResources().getIdentifier(product.getImg(), "drawable", getPackageName());
            if (resId != 0) {
                productDetailImage.setImageResource(resId);
            } else {
                productDetailImage.setImageResource(R.drawable.placeholder); // fallback nếu không tìm thấy ảnh
            }
        }

        btnAddToCart.setOnClickListener(v -> {
            if (product != null) {
                CartManager.getInstance().addProduct(product, this);
                showAddToCartNotification(product.getName());
            }
        });

        tvStorePhone.setOnClickListener(v -> {
            String phoneNumber = tvStorePhone.getText().toString();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        });

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Cart Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notifications for cart events");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showAddToCartNotification(String productName) {
        Intent intent = new Intent(this, CartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_cart)
                .setContentTitle("Shopping Cart")
                .setContentText("You added 1 item: " + productName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            return;
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
