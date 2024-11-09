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
import android.widget.Toast;

import java.util.ArrayList;


public class DeliveredFragment extends Fragment implements ProductAdapter.OnProductScheduledListener{


    public interface OnOrderDeliveredListener {
        void onOrderDelivered();
    }

    ListView lvDeliveredOrdersList;
    Context context;

    // Parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public DeliveredFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    // Factory method to create a new instance of this fragment
    public static DeliveredFragment newInstance(String param1, String param2) {
        DeliveredFragment fragment = new DeliveredFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delivered, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvDeliveredOrdersList = view.findViewById(R.id.lvDeliveredOrdersList);
        refreshDeliveredOrderList();
    }

    public void refreshDeliveredOrderList() {
          ProductDB productDB = new ProductDB(context);
        productDB.open();

        MyApplication.deliveredProducts= productDB.fetchDeliveredProducts(); // Method to fetch delivered products
        productDB.close();

      //  DeliveredOrderAdapter adapter = new DeliveredOrderAdapter(context, R.layout.delivered_order_item_design, deliveredProducts);
        ProductAdapter adapter = new ProductAdapter(context, R.layout.product_item_design, MyApplication.deliveredProducts,null);

        lvDeliveredOrdersList.setAdapter(adapter);
    }
    @Override
    public void onResume() {
        super.onResume();

        refreshDeliveredOrderList(); // Refresh the list every time the fragment is visible
    }
    @Override
    public void onProductScheduled() {
        refreshDeliveredOrderList(); // Refresh the list every time the fragment is visible

    }
}
