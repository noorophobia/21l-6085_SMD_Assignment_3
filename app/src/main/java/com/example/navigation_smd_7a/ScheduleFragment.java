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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment implements ProductAdapter.OnProductScheduledListener {


    ListView lvScheduledOrdersList;
    Context context;


    public ScheduleFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }



    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvScheduledOrdersList = view.findViewById(R.id.lvScheduledOrdersList);
        refreshScheduledOrderList();
    }

    public void refreshScheduledOrderList() {
         ProductDB productDB = new ProductDB(context);
        productDB.open();

        MyApplication.scheduledProducts= productDB.fetchScheduledProducts(); // Method to fetch delivered products
        productDB.close();

        //  DeliveredOrderAdapter adapter = new DeliveredOrderAdapter(context, R.layout.delivered_order_item_design, deliveredProducts);
        ProductAdapter adapter = new ProductAdapter(context, R.layout.product_item_design,    MyApplication.scheduledProducts,null);

        lvScheduledOrdersList.setAdapter(adapter);
    }
    @Override
    public void onResume() {
        super.onResume();

        refreshScheduledOrderList(); // Refresh the list every time the fragment is visible
    }

    @Override
    public void onProductScheduled() {
        refreshScheduledOrderList(); // Refresh the list every time the fragment is visible

    }
}