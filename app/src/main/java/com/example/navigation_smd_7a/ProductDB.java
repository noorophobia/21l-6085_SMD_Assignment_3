package com.example.navigation_smd_7a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ProductDB {
    public final String DATABASE_NAME = "products_db";
    public final String DATABASE_TABLE_NAME = "products";
    public final String KEY_ID = "id";
    public final String KEY_TITLE = "title";
    public final String KEY_DATE = "date";
    public final String KEY_PRICE = "price";
    public final String KEY_STATUS = "status";

    private final int DB_VERSION = 1;
    Context context;
    DBHelper dbHelper;

    ProductDB(Context context) {
        this.context = context;
    }

    public void open() {
        dbHelper = new DBHelper(context, DATABASE_NAME, null, DB_VERSION);
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(String title, String date, int price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_TITLE, title);
        cv.put(KEY_DATE, date);
        cv.put(KEY_PRICE, price);
        cv.put(KEY_STATUS, "new"); // Default status is set to "new"

        return db.insert(DATABASE_TABLE_NAME, null, cv);
    }

    public int remove(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(DATABASE_TABLE_NAME, KEY_ID + "=?", new String[]{id + ""});
    }

    public int updatePrice(int id, int price) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_PRICE, price);
        return db.update(DATABASE_TABLE_NAME, cv, KEY_ID + "=?", new String[]{id + ""});
    }
     public int updateStatus(int id, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_STATUS, status); // Set the new status
        return db.update(DATABASE_TABLE_NAME, cv, KEY_ID + "=?", new String[]{id + ""});
    }
    public int update(int id, Product p) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, id);
        cv.put(KEY_TITLE, p.getTitle());
        cv.put(KEY_DATE, p.getDate());
        cv.put(KEY_PRICE, p.getPrice());
        cv.put(KEY_STATUS, p.getStatus()); // If you have a setter/getter for status in Product
        return db.update(DATABASE_TABLE_NAME, cv, KEY_ID + "=?", new String[]{id + ""});
    }

    public ArrayList<Product> fetchScheduledProducts() {
        SQLiteDatabase readDb = dbHelper.getReadableDatabase();
        ArrayList<Product> scheduledProducts = new ArrayList<>();
        String[] columns = new String[]{KEY_ID, KEY_TITLE, KEY_DATE, KEY_PRICE, KEY_STATUS};
        Cursor cursor = readDb.rawQuery("SELECT * FROM products WHERE status = ?", new String[]{"scheduled"});

        if (cursor != null) {
            int id_index = cursor.getColumnIndex(KEY_ID);
            int title_index = cursor.getColumnIndex(KEY_TITLE);
            int date_index = cursor.getColumnIndex(KEY_DATE);
            int price_index = cursor.getColumnIndex(KEY_PRICE);
            int status_index = cursor.getColumnIndex(KEY_STATUS); // Get index for status

            while (cursor.moveToNext()) {
                Product p = new Product(cursor.getInt(id_index), cursor.getString(title_index), cursor.getString(date_index),
                        cursor.getInt(price_index), cursor.getString(status_index)); // Include status
                scheduledProducts.add(p);
            }
            cursor.close();
        }
        return scheduledProducts;
    }
    public ArrayList<Product> fetchDeliveredProducts() {
        SQLiteDatabase readDb = dbHelper.getReadableDatabase();
        ArrayList<Product> deliveredproducts = new ArrayList<>();
        String[] columns = new String[]{KEY_ID, KEY_TITLE, KEY_DATE, KEY_PRICE, KEY_STATUS};
        Cursor cursor = readDb.rawQuery("SELECT * FROM products WHERE status = ?", new String[]{"delivered"});

         if (cursor != null) {
            int id_index = cursor.getColumnIndex(KEY_ID);
            int title_index = cursor.getColumnIndex(KEY_TITLE);
            int date_index = cursor.getColumnIndex(KEY_DATE);
            int price_index = cursor.getColumnIndex(KEY_PRICE);
            int status_index = cursor.getColumnIndex(KEY_STATUS); // Get index for status

            while (cursor.moveToNext()) {
                Product p = new Product(cursor.getInt(id_index), cursor.getString(title_index), cursor.getString(date_index),
                        cursor.getInt(price_index), cursor.getString(status_index)); // Include status
                deliveredproducts.add(p);
            }
            cursor.close();
        }
        return deliveredproducts;
    }
        public ArrayList<Product> fetchProducts() {
            SQLiteDatabase readDb = dbHelper.getReadableDatabase();
            ArrayList<Product> deliveredproducts = new ArrayList<>();
            String[] columns = new String[]{KEY_ID, KEY_TITLE, KEY_DATE, KEY_PRICE, KEY_STATUS};
            Cursor cursor = readDb.rawQuery("SELECT * FROM products WHERE status = ?", new String[]{"new"});

            if (cursor != null) {
                int id_index = cursor.getColumnIndex(KEY_ID);
                int title_index = cursor.getColumnIndex(KEY_TITLE);
                int date_index = cursor.getColumnIndex(KEY_DATE);
                int price_index = cursor.getColumnIndex(KEY_PRICE);
                int status_index = cursor.getColumnIndex(KEY_STATUS); // Get index for status

                while (cursor.moveToNext()) {
                    Product p = new Product(cursor.getInt(id_index), cursor.getString(title_index), cursor.getString(date_index),
                            cursor.getInt(price_index), cursor.getString(status_index)); // Include status
                    deliveredproducts.add(p);
                }
                cursor.close();
            }
            return deliveredproducts;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String query = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_NAME + "("
                    + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_TITLE + " TEXT NOT NULL, "
                    + KEY_DATE + " TEXT NOT NULL, "
                    + KEY_PRICE + " INTEGER, "
                    + KEY_STATUS + " TEXT NOT NULL DEFAULT 'new' " + // Ensure the default value for status is set
                    ");";
            sqLiteDatabase.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            // Backup your data here
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }
}
