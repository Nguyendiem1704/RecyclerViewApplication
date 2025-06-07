package model;

import com.example.recyclerviewapplication.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    public String name;
    public int iconResId;
    public Product(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public static List<Product> getSampleProducts() {
        List<Product> list = new ArrayList<>();
        list.add(new Product("Pharmacy", R.drawable.ic_pharmacy));
        list.add(new Product("Clothing", R.drawable.ic_clothing));
        list.add(new Product("Shoes", R.drawable.ic_shoes));
        list.add(new Product("Accessories", R.drawable.ic_accessories));
        list.add(new Product("Baby", R.drawable.ic_baby));
        list.add(new Product("Home", R.drawable.ic_home));
        return list;
    }
}
