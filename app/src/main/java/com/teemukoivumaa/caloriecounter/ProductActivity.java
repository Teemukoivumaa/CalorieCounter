package com.teemukoivumaa.caloriecounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.teemukoivumaa.caloriecounter.Database.CalorieProduct;
import com.teemukoivumaa.caloriecounter.Database.ProductDAO;
import com.teemukoivumaa.caloriecounter.Database.ProductDatabase;

import java.util.Arrays;
import java.util.List;

public class ProductActivity extends AppCompatActivity {

    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        ProductDatabase productDatabase = Room.databaseBuilder(
                getApplicationContext(),
                ProductDatabase.class,
                "ProductDatabase"
        ).allowMainThreadQueries().build();
        productDAO = productDatabase.productDAO();

        createTable();
    }

    private void createTable() {
        TableLayout productTable = findViewById(R.id.productTableLayout);
        TableRow headerRow = new TableRow(this);
        List<String> tableHeaders = Arrays.asList(
                "Product",
                "Amount",
                "Prefix",
                "Calories"
        );

        for (int i = 0; i < tableHeaders.size(); i++) {
            TextView headerText = new TextView(this);
            headerText.setText(tableHeaders.get(i));
            headerText.setTextSize(24);
            headerText.setTextColor(getColor(R.color.white));
            headerText.setPadding(15,15,15,15);
            headerText.setBackground(getDrawable(R.drawable.border));
            headerRow.addView(headerText);
        }

        productTable.addView(headerRow);

        populateTable(productTable);
    }

    private void rebuildTable() {
        TableLayout productTable = findViewById(R.id.productTableLayout);
        productTable.removeAllViews();
        createTable();
    }

    private void populateTable(TableLayout productTable) {
        List<CalorieProduct> products = productDAO.getAll();

        for (int i = 0; i < products.size(); i++) {
            TableRow productRow = new TableRow(this);
            CalorieProduct calorieProduct = products.get(i);

            productRow.setOnClickListener(view -> {
                showProductPopup(calorieProduct);
                Log.d("Test", "populateTable: ");
            });

            TextView productName = new TextView(this);
            productName.setText(calorieProduct.getProductName());
            productName.setTextSize(20);
            productName.setTextColor(getColor(R.color.white));
            productName.setPadding(15,15,15,15);
            productName.setBackground(getDrawable(R.drawable.border));
            productRow.addView(productName);

            TextView productAmount = new TextView(this);
            productAmount.setText(String.valueOf(calorieProduct.getProductAmount()));
            productAmount.setTextSize(20);
            productAmount.setTextColor(getColor(R.color.white));
            productAmount.setPadding(15,15,15,15);
            productAmount.setBackground(getDrawable(R.drawable.border));
            productRow.addView(productAmount);

            TextView amountPrefix = new TextView(this);
            amountPrefix.setText(calorieProduct.getAmountPrefix());
            amountPrefix.setTextSize(20);
            amountPrefix.setTextColor(getColor(R.color.white));
            amountPrefix.setPadding(15,15,15,15);
            amountPrefix.setBackground(getDrawable(R.drawable.border));
            productRow.addView(amountPrefix);

            TextView calorieAmount = new TextView(this);
            calorieAmount.setText(String.valueOf(calorieProduct.getCalorieAmount()));
            calorieAmount.setTextSize(20);
            calorieAmount.setTextColor(getColor(R.color.white));
            calorieAmount.setPadding(15,15,15,15);
            calorieAmount.setBackground(getDrawable(R.drawable.border));
            productRow.addView(calorieAmount);

            productTable.addView(productRow);
        }

        TableRow emptyRow = new TableRow(this);
        TextView invisibleText = new TextView(this);
        invisibleText.setText("Invisible");
        invisibleText.setTextSize(26);
        invisibleText.setTextColor(getColor(R.color.SeeTrough));
        emptyRow.addView(invisibleText);
        productTable.addView(emptyRow);
    }

    private void addProduct(String name, int amount, String amountPrefix, int calorieAmount) {
        CalorieProduct calorieProduct = new CalorieProduct();
        calorieProduct.setProductName(name);
        calorieProduct.setProductAmount(amount);
        calorieProduct.setAmountPrefix(amountPrefix);
        calorieProduct.setCalorieAmount(calorieAmount);

        productDAO.insert(calorieProduct);
    }

    private void showProductPopup(CalorieProduct calorieProduct) {
        MaterialAlertDialogBuilder productPopup = new MaterialAlertDialogBuilder(this, R.style.RoundShapePopup)
                .setTitle("Product")
                .setMessage("View product info, modify it or delete it.")
                .setNeutralButton("DELETE", (dialogInterface, i) -> { });

        final View dialogView = LayoutInflater.from(productPopup.getContext()).inflate(R.layout.popup_product, null);

        String productName = calorieProduct.productName;
        String productAmount = Integer.toString(calorieProduct.productAmount);
        String productPrefix = calorieProduct.amountPrefix;
        String productCalories = Integer.toString(calorieProduct.calorieAmount);

        TextView nameTextView = dialogView.findViewById(R.id.productName);
        TextView amountTextView = dialogView.findViewById(R.id.productAmount);
        TextView prefixTextView = dialogView.findViewById(R.id.productPrefix);
        TextView caloriesTextView = dialogView.findViewById(R.id.productCalories);

        nameTextView.setText(productName);
        amountTextView.setText(productAmount);
        prefixTextView.setText(productPrefix);
        caloriesTextView.setText(productCalories);

        productPopup.setView(dialogView);
        productPopup.show();
    }

    public void showAddProductPopup(View view) {
        MaterialAlertDialogBuilder addProductPopup = new MaterialAlertDialogBuilder(this, R.style.RoundShapePopup)
                .setTitle("Create new product")
                .setMessage("Add the required info for the product.")
                .setNeutralButton("CANCEL", (dialogInterface, i) -> { });

        final View dialogView = LayoutInflater.from(addProductPopup.getContext()).inflate(R.layout.popup_add_product, null);

        TextView productNameInput = dialogView.findViewById(R.id.productNameInput);
        TextView productAmountInput = dialogView.findViewById(R.id.productAmountInput);
        TextView productPrefixInput = dialogView.findViewById(R.id.productPrefixInput);
        TextView productCaloriesInput = dialogView.findViewById(R.id.productCaloriesInput);

        addProductPopup.setView(dialogView);
        addProductPopup.setPositiveButton("ADD PRODUCT", (dialogInterface, i) -> {
            String productName = productNameInput.getText().toString();
            String productAmount = productAmountInput.getText().toString();
            String productPrefix = productPrefixInput.getText().toString();
            String productCalories = productCaloriesInput.getText().toString();

            addProduct(productName, Integer.parseInt(productAmount), productPrefix, Integer.parseInt(productCalories));
            rebuildTable();
        });
        addProductPopup.show();
    }
}