package model;

import com.example.recyclerviewapplication.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {

    private int id;
    private String name;
    private String descript;
    private String img;
    private double price;
    private int stock;
    public int iconResId;

    public Product() {
    }

    public Product(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public Product(int id, String name, String descript, String img, double price, int stock) {
        this.id = id;
        this.name = name;
        this.descript = descript;
        this.img = img;
        this.price = price;
        this.stock = stock;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    @Override
    public String toString() {
        return name;
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
