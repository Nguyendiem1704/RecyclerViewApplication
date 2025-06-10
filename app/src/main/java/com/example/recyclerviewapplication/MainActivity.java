package com.example.recyclerviewapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import adapter.ProductAdapter;
import database.ProductDAO;
import model.CartManager;
import model.Product;
import session.SessionManager;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "cart_channel_id";
    private static final int NOTIFICATION_ID = 1;

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ProductDAO productDAO;

    private BroadcastReceiver cartCountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            invalidateOptionsMenu();
            showAddToCartNotification();
        }
    };

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo SessionManager
        sessionManager = new SessionManager(MainActivity.this);

        // Nếu chưa login thì chuyển về LoginActivity
        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_home);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Hiển thị "Welcome, user"
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        String username = sessionManager.getUsername();
        toolbarTitle.setText("Welcome, " + username + "!");

        // Khởi tạo ProductDAO
        productDAO = new ProductDAO(this);

        // Xử lý PopupMenu (menu 3 chấm)
        ImageView icMenu = findViewById(R.id.ic_menu);
        icMenu.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

            // Hiển thị icon trong menu
            try {
                java.lang.reflect.Field[] fields = popupMenu.getClass().getDeclaredFields();
                for (java.lang.reflect.Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popupMenu);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        java.lang.reflect.Method setForceShowIcon =
                                classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceShowIcon.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Xử lý click các item trong menu
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.menu_insert) {
                    Intent intent = new Intent(MainActivity.this, InsertProductActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.menu_update) {
                    Intent intent = new Intent(MainActivity.this, UpdateProductActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.menu_delete) {
                    Intent intent = new Intent(MainActivity.this, DeleteProductActivity.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.menu_profile) {
                    Toast.makeText(MainActivity.this, "Profile clicked", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.menu_logout) {
                    sessionManager.logout();
                    Intent intentLogout = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intentLogout);
                    finish();
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // Load dữ liệu từ database lần đầu
        loadProductsFromDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(cartCountReceiver, new IntentFilter("UPDATE_CART_COUNT"));
        invalidateOptionsMenu();

        // Load lại sản phẩm mỗi lần quay về
        loadProductsFromDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(cartCountReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_cart);

        FrameLayout cartActionView = (FrameLayout) menuItem.getActionView();
        ImageView icCart = cartActionView.findViewById(R.id.ic_cart);
        TextView tvCartCount = cartActionView.findViewById(R.id.tvCartCount);

        int count = CartManager.getInstance().getCartSize();
        tvCartCount.setText(String.valueOf(count));
        tvCartCount.setVisibility(count > 0 ? TextView.VISIBLE : TextView.GONE);

        icCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        cartActionView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Toast.makeText(this, "Toolbar Search clicked", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_cart) {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddToCartNotification() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Cart Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this, CartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_cart)
                .setContentTitle("Shopping Cart")
                .setContentText("You have added 1 item to your cart")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManager.cancel(NOTIFICATION_ID);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    // Load sản phẩm từ database
    private void loadProductsFromDatabase() {
        List<Product> products = productDAO.getAllProducts();

        adapter = new ProductAdapter(products, product -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
    }
}
