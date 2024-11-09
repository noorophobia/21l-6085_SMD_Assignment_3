package com.example.navigation_smd_7a;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class NewOrderFragment extends Fragment implements ProductAdapter.OnProductScheduledListener{


    public interface OnProductAddedListener {
        void onProductAdded();
    }
    ListView lvNewOrderList;
    Context context;


    public NewOrderFragment() {
     }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
          lvNewOrderList = view.findViewById(R.id.lvNewOrdersList);
        ProductDB productDB = new ProductDB(context);
        productDB.open();
        MyApplication.newProducts = productDB.fetchProducts();
        productDB.close();

        ProductAdapter adapter = new ProductAdapter(context, R.layout.product_item_design,   MyApplication.newProducts, this);
        lvNewOrderList.setAdapter(adapter);
     }
    public void refreshProductList() {
        ProductDB productDB = new ProductDB(context);
        productDB.open();
        ArrayList<Product> products = productDB.fetchProducts(); // Fetch the updated list of products
        productDB.close();

        // Clear the current list in the adapter and add the updated products
        ProductAdapter adapter = (ProductAdapter) lvNewOrderList.getAdapter();
        if (adapter != null) {
            adapter.clear(); // Clear existing items
            adapter.addAll(products); // Add the updated product list
            adapter.notifyDataSetChanged(); // Notify the adapter of the data change
        }
    }

    @Override
    public void onProductScheduled() {
        refreshProductList();
    }

}