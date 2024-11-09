package com.example.navigation_smd_7a;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NewOrderFragment.OnProductAddedListener {

    TabLayout tabLayout;
    ViewPager2 vp2;
    ViewPagerAdapter adapter;
    int countScheduled = 0;
    int countDelivered = 0;
    boolean flag = false;

    FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adapter = new ViewPagerAdapter(this);
        vp2 = findViewById(R.id.viewpager2);
        vp2.setAdapter(adapter);
        tabLayout = findViewById(R.id.tabLayout);
        fab_add = findViewById(R.id.fab_add);

        fab_add.setOnClickListener(view -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            dialogBuilder.setTitle("Product");

            View dialogView = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.add_new_product_dialog_design, null, false);
            dialogBuilder.setView(dialogView);

            EditText etTitle = dialogView.findViewById(R.id.etTitle);
            EditText etDate = dialogView.findViewById(R.id.etDate);
            EditText etPrice = dialogView.findViewById(R.id.etPrice);

            AlertDialog dialog = dialogBuilder.create();
            dialog.setCancelable(false); // Prevent closing on outside tap
            dialog.show();

            // Handle Save button click manually
            dialogView.findViewById(R.id.btnSave).setOnClickListener(v -> {
                String title = etTitle.getText().toString().trim();
                String date = etDate.getText().toString().trim();
                String price = etPrice.getText().toString().trim();

                // Clear previous errors
                etTitle.setError(null);
                etDate.setError(null);
                etPrice.setError(null);

                boolean isValid = true;

                // Validation checks
                if (title.isEmpty()) {
                    etTitle.setError("Title cannot be empty");
                    isValid = false;
                }
                if (date.isEmpty()) {
                    etDate.setError("Date cannot be empty");
                    isValid = false;
                } else {
                    // Validate and parse the date
                    String validDate = DateValidator.validateAndParseDate(date);
                    Toast.makeText(getApplicationContext(), "Invalid date. Setting to current date: " + validDate, Toast.LENGTH_SHORT).show();

                    if (validDate == null) {
                        // If the date is invalid, set the current date
                        validDate = DateValidator.getCurrentDate();

                        etDate.setText(validDate);
                        Toast.makeText(getApplicationContext(), "Invalid date. Setting to current date: " + validDate, Toast.LENGTH_SHORT).show();
                    } else {
                        etDate.setText(validDate);  // Valid date, update the EditText
                    }
                    date = validDate;
                }
                if (price.isEmpty()) {
                    etPrice.setError("Price cannot be empty");
                    isValid = false;
                }

                if (isValid) {
                    Product product = new Product();
                    product.setTitle(title);
                    product.setDate(date);
                    product.setPrice(Integer.parseInt(price));
                    product.setStatus("new");

                    MyApplication app = (MyApplication) getApplication();
                    app.addNewProduct(product);

                    ProductDB productDB = new ProductDB(MainActivity.this);
                    productDB.open();
                    productDB.insert(title, date, Integer.parseInt(price));
                    productDB.close();

                    Toast.makeText(MainActivity.this, "Product Added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss(); // Dismiss the dialog if all fields are valid
                    onProductAdded();
                }
            });

            // Handle Cancel button click
            dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        });


         // Tab Layout and ViewPager setup
        TabLayoutMediator tabLayoutMediator =
                new TabLayoutMediator(tabLayout, vp2, (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Scheduled");
                            tab.setIcon(R.drawable.schedule_icon);
                            BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                            countScheduled = MyApplication.numberOfScheduledProducts(); // Get the count of scheduled products
                            badgeDrawable.setNumber(countScheduled);
                            badgeDrawable.setMaxCharacterCount(2);
                            badgeDrawable.setVisible(true);
                            break;
                        case 1:
                            tab.setText("Delivered");
                            tab.setIcon(R.drawable.delivered_icon);
                            badgeDrawable = tab.getOrCreateBadge();
                             countDelivered = MyApplication.numberOfDeliveredProducts(); // Get the count of delivered products
                            badgeDrawable.setNumber(countDelivered);

                            badgeDrawable.setMaxCharacterCount(2);
                            badgeDrawable.setVisible(true);
                            break;
                        default:
                            tab.setText("New Orders");
                            tab.setIcon(R.drawable.new_orders_icon);
                    }
                });
        tabLayoutMediator.attach();
        vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateTabBadge(position);  // Update badge count on page change
            }
        });


    }
    private void updateTabBadge(int position) {
        switch (position) {
            case 0:
                // Update badge for the Scheduled tab
                countScheduled = MyApplication.numberOfScheduledProducts();
                TabLayout.Tab scheduledTab = tabLayout.getTabAt(0);
                if (scheduledTab != null) {
                    BadgeDrawable scheduledBadge = scheduledTab.getOrCreateBadge();
                    scheduledBadge.setNumber(countScheduled);
                    scheduledBadge.setMaxCharacterCount(2);
                    scheduledBadge.setVisible(true);
                }
                break;
            case 1:
                // Update badge for the Delivered tab
                countDelivered = MyApplication.numberOfDeliveredProducts();
                TabLayout.Tab deliveredTab = tabLayout.getTabAt(1);
                if (deliveredTab != null) {
                    BadgeDrawable deliveredBadge = deliveredTab.getOrCreateBadge();
                    deliveredBadge.setNumber(countDelivered);
                    deliveredBadge.setMaxCharacterCount(2);
                    deliveredBadge.setVisible(true);
                }
                break;
            default:
                 break;
        }}

    @Override
    public void onProductAdded() {
        if (adapter != null) {
            adapter.refreshNewOrderFragment();
        }
    }
}
