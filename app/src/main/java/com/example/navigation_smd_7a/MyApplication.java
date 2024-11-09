package com.example.navigation_smd_7a;

import android.app.Application;

import java.util.ArrayList;

public class MyApplication extends Application {
    public static ArrayList<Product> newProducts;
    public static ArrayList<Product> scheduledProducts;
    public static ArrayList<Product> deliveredProducts;

    @Override
    public void onCreate() {
        super.onCreate();
        newProducts = new ArrayList<>();
        scheduledProducts = new ArrayList<>();
        deliveredProducts = new ArrayList<>();
    }

    public ArrayList<Product> getNewProducts() {
        return newProducts;
    }
    public static int numberOfDeliveredProducts(){
        return deliveredProducts.size();
    }
    public static int numberOfNewProducts(){
        return newProducts.size();
    }
   public static int numberOfScheduledProducts(){
        return scheduledProducts.size();
   }
    public void addNewProduct(Product product) {
        newProducts.add(product);
    }

    public void removeNewProduct(Product product) {
        newProducts.remove(product);
    }

    public ArrayList<Product> getScheduledProducts() {
        return scheduledProducts;
    }

    public void addScheduledProduct(Product product) {
        scheduledProducts.add(product);
    }

    public void removeScheduledProduct(Product product) {
        scheduledProducts.remove(product);
    }

    public ArrayList<Product> getDeliveredProducts() {
        return deliveredProducts;
    }

    public void addDeliveredProduct(Product product) {
        deliveredProducts.add(product);
    }

    public void removeDeliveredProduct(Product product) {
        deliveredProducts.remove(product);
    }
}
