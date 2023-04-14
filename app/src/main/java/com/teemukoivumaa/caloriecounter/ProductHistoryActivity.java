package com.teemukoivumaa.caloriecounter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.teemukoivumaa.caloriecounter.Database.AppDatabase;
import com.teemukoivumaa.caloriecounter.Database.CalorieProduct;
import com.teemukoivumaa.caloriecounter.Database.ProductHistory;
import com.teemukoivumaa.caloriecounter.Database.ProductHistoryDAO;

import java.util.Arrays;
import java.util.List;

public class ProductHistoryActivity extends AppCompatActivity {

    private ProductHistoryDAO productHistoryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_history);
        setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
        productHistoryDAO = appDatabase.productHistoryDAO();

        createTable();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        return true;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void createTable() {
        TableLayout productTable = findViewById(R.id.productHistoryTableLayout);
        TableRow headerRow = new TableRow(this);
        List<String> tableHeaders = Arrays.asList(
                "Product",
                "Amount",
                "Calories",
                "Datetime"
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

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void populateTable(TableLayout productTable) {
        List<ProductHistory> products = productHistoryDAO.getAll();

        for (int i = 0; i < products.size(); i++) {
            TableRow productRow = new TableRow(this);
            ProductHistory productHistory = products.get(i);

            /*
            productRow.setOnClickListener(view -> {
                showProductPopup(calorieProduct);
                Log.d("Test", "populateTable: ");
            });
            */

            TextView productName = new TextView(this);
            productName.setText(productHistory.getProductName());
            productName.setTextSize(20);
            productName.setTextColor(getColor(R.color.white));
            productName.setPadding(15,15,15,15);
            productName.setBackground(getDrawable(R.drawable.border));
            productRow.addView(productName);


            TextView productAmount = new TextView(this);
            productAmount.setText(productHistory.getAmount());
            productAmount.setTextSize(20);
            productAmount.setTextColor(getColor(R.color.white));
            productAmount.setPadding(15,15,15,15);
            productAmount.setBackground(getDrawable(R.drawable.border));
            productRow.addView(productAmount);

            TextView calorieAmount = new TextView(this);
            calorieAmount.setText(String.valueOf(productHistory.getCalorieAmount()));
            calorieAmount.setTextSize(20);
            calorieAmount.setTextColor(getColor(R.color.white));
            calorieAmount.setPadding(15,15,15,15);
            calorieAmount.setBackground(getDrawable(R.drawable.border));
            productRow.addView(calorieAmount);

            TextView productDate = new TextView(this);
            productDate.setText(productHistory.getProductDate());
            productDate.setTextSize(20);
            productDate.setTextColor(getColor(R.color.white));
            productDate.setPadding(15,15,15,15);
            productDate.setBackground(getDrawable(R.drawable.border));
            productRow.addView(productDate);

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
}