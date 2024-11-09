package com.example.navigation_smd_7a;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {
    Context context;
    int resource;
    private OnProductScheduledListener listener;

    public ProductAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects, OnProductScheduledListener listener) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.listener = listener; // Initialize the listener
    }

    public interface OnProductScheduledListener {
        void onProductScheduled();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        TextView tvTitle = v.findViewById(R.id.tvProductTitle);
        ImageView ivEdit = v.findViewById(R.id.ivEdit);
        ImageView ivDelete = v.findViewById(R.id.ivDelete);
         Product p = getItem(position);
        if (p != null) {
            tvTitle.setText("Product :"+p.getTitle() +"\n  Date :"+p.getDate()+ "\n"+ "Price :" + p.getPrice()+"\n");
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (p != null) {
                    if(p.getStatus().equals("new")){
                    moveProductToScheduled(p);
                 }   if((p.getStatus().equals("scheduled"))){
                        moveProductToDelivered(p);

                    }
                    if((p.getStatus().equals("delivered"))){
                        deleteProduct(p);


                    }
                }
            }
        });

        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog(p, position);
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a confirmation dialog
                new AlertDialog.Builder(context)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this product?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Call the deleteProduct method if user confirms
                                deleteProduct(p);
                                Toast.makeText(context, "Product deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null) // Do nothing if user cancels
                        .show();
            }
        });


        return v;
    }
   private void deleteProduct (Product p){
       ProductDB db = new ProductDB(context);
       db.open();
       db.remove(p.getId());
       db.close();
       remove(p);
       notifyDataSetChanged();
   }
    private void showEditDialog(Product product, int position) {
       androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context);
        dialogBuilder.setTitle("Product");

        View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.add_new_product_dialog_design, null, false);
        dialogBuilder.setView(dialogView);

        EditText etTitle = dialogView.findViewById(R.id.etTitle);
        EditText etDate = dialogView.findViewById(R.id.etDate);
        EditText etPrice = dialogView.findViewById(R.id.etPrice);

        etTitle.setText(product.getTitle());
        etDate.setText(product.getDate());
        etPrice.setText(Integer.toString(product.getPrice()));

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

                if (validDate == null) {
                    // If the date is invalid, set the current date
                    validDate = DateValidator.getCurrentDate();

                    etDate.setText(validDate);
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
                 product.setTitle(title);
                product.setDate(date);
                product.setPrice(Integer.parseInt(price));
                product.setStatus(product.getStatus());
                // Notifya the adapter if you're using it to update the UI
                notifyDataSetChanged();

                ProductDB productDB = new ProductDB(context);
                productDB.open();
                productDB.update(product.getId(),product);
                productDB.close();
               notifyDataSetChanged();
                if(product.getStatus().equals("scheduled")){
                     productDB.open();
                     MyApplication.scheduledProducts=productDB.fetchScheduledProducts();
                    productDB.close();
                 }
                else if(product.getStatus().equals("delivered")){
                    productDB.open();

                    MyApplication.deliveredProducts=productDB.fetchDeliveredProducts();
                    productDB.close();
                }
                else if(product.getStatus().equals("new")){
                    productDB.open();

                    MyApplication.newProducts=productDB.fetchProducts();
                    productDB.close();

                }
                 dialog.dismiss(); // Dismiss the dialog if all fields are valid
             }
        });

        // Handle Cancel button click
        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
    }



    private void moveProductToScheduled(Product product) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();
        // Remove from new products and add to scheduled products
        myApplication.removeNewProduct(product);
        myApplication.addScheduledProduct(product);
        Toast.makeText(context,"Product added to scheduled ",Toast.LENGTH_SHORT).show();

         // Optionally update the database to reflect the status change
        ProductDB db = new ProductDB(context);
        db.open();
        db.updateStatus(product.getId(), "scheduled"); // Assuming you have a status for 'scheduled'
        db.close();

        // Notify that the adapter data has changed
        remove(product);
        notifyDataSetChanged();
        Toast.makeText(context,"notified ",Toast.LENGTH_SHORT).show();
        // Call the listener
        if (listener != null) {
            listener.onProductScheduled();
        }


    }
    private void moveProductToDelivered(Product product) {
        MyApplication myApplication = (MyApplication) context.getApplicationContext();
        // Remove from new products and add to scheduled products
        myApplication.removeNewProduct(product);
        myApplication.addScheduledProduct(product);
        Toast.makeText(context,"Product added to delivered ",Toast.LENGTH_SHORT).show();

        // Optionally update the database to reflect the status change
        ProductDB db = new ProductDB(context);
        db.open();
        db.updateStatus(product.getId(), "delivered"); // Assuming you have a status for 'scheduled'
        db.close();

        // Notify that the adapter data has changed
        remove(product);
        notifyDataSetChanged();
        Toast.makeText(context,"notified ",Toast.LENGTH_SHORT).show();
        // Call the listener
        if (listener != null) {
            listener.onProductScheduled();
        }


    }

}
