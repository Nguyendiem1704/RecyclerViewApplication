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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import model.CartManager;
import model.Product;

public class DetailActivity extends AppCompatActivity {

    private ImageView productDetailImage;
    private TextView productDetailName;
    private Button btnAddToCart;
    private TextView tvStorePhone;
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
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        product = (Product) getIntent().getSerializableExtra("product");
        if (product != null) {
            getSupportActionBar().setTitle(product.name);
        }

        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        productDetailImage = findViewById(R.id.productDetailImage);
        productDetailName = findViewById(R.id.productDetailName);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        tvStorePhone = findViewById(R.id.tvStorePhone);

        Product product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            productDetailName.setText(product.name);
            productDetailImage.setImageResource(product.iconResId);
        }

        btnAddToCart.setOnClickListener(v -> {
            if (product != null) {
                CartManager.getInstance().addProduct(product, this);
                showAddToCartNotification(product.name);
            }
        });

        tvStorePhone.setOnClickListener(v -> {
            String phoneNumber = tvStorePhone.getText().toString();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        });

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Cart Notifications";
            String description = "Notifications to remind user about products in cart";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showAddToCartNotification(String productName) {
        Intent intent = new Intent(this, CartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_cart)
                .setContentTitle("Shopping Cart")
                .setContentText("You have added 1 item to your cart: " + productName)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
                return;
            }
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
