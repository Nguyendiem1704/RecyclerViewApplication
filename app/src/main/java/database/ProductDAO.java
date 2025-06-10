package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import model.Product;

public class ProductDAO {

    private final DatabaseHelper dbHelper;

    public ProductDAO(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    // Insert
    public void insertProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COLUMN_NAME, product.getName());
        values.put(DatabaseHelper.COLUMN_DESCRIPT, product.getDescript());
        values.put(DatabaseHelper.COLUMN_IMG, product.getImg());
        values.put(DatabaseHelper.COLUMN_PRICE, product.getPrice());
        values.put(DatabaseHelper.COLUMN_STOCK, product.getStock());

        db.insert(DatabaseHelper.TABLE_PRODUCT, null, values);
        db.close();
    }

    // Update
    public boolean updateProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, product.getName());
        values.put(DatabaseHelper.COLUMN_DESCRIPT, product.getDescript());
        values.put(DatabaseHelper.COLUMN_IMG, product.getImg());
        values.put(DatabaseHelper.COLUMN_PRICE, product.getPrice());
        values.put(DatabaseHelper.COLUMN_STOCK, product.getStock());

        int rowsAffected = db.update(
                DatabaseHelper.TABLE_PRODUCT,
                values,
                DatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(product.getId())}
        );

        db.close();
        return rowsAffected > 0;
    }

    // Delete
    public void deleteProduct(int productId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_PRODUCT, DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(productId)});
        db.close();
    }

    // Get all
    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_PRODUCT;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                product.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME)));
                product.setDescript(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPT)));
                product.setImg(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IMG)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE)));
                product.setStock(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STOCK)));

                productList.add(product);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return productList;
    }
}
