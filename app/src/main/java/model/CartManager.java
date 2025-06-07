package model;

import android.content.Context;
import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static CartManager instance;
    private List<Product> cartProducts;

    private CartManager() {
        cartProducts = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }
    public void addProduct(Product product, Context context) {
        cartProducts.add(product);
        Intent intent = new Intent("UPDATE_CART_COUNT");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public int getCartSize() {
        return cartProducts.size();
    }

    public List<Product> getCartProducts() {
        return cartProducts;
    }

    public void clearCart() {
        cartProducts.clear();
    }
}
