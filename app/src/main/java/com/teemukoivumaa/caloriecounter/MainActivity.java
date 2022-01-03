package com.teemukoivumaa.caloriecounter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView inputCalories;
    private TextView totalCalories;

    private ProgressBar progressBar;

    private static final String kcal = "kcal🔥";
    private int kcalDailyGoal;

    private ActivityResultLauncher<Intent> settingsResultLauncher = null;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputCalories = findViewById(R.id.newDailyGoal);
        totalCalories = findViewById(R.id.totalCalories);
        progressBar = findViewById(R.id.progressBar);

        inputCalories.setOnEditorActionListener((v, actionId, event) -> {
            addCalories();
            return true;
        });

        SharedPreferences pref = getApplicationContext().getSharedPreferences("CalorieCounter", 0);
        int storedCalories = pref.getInt("caloriesToday", 0);
        kcalDailyGoal = pref.getInt("kcalDailyGoal", 2242);
        setTotalCalories(storedCalories);

        settingsResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    kcalDailyGoal = pref.getInt("kcalDailyGoal", 2242);
                    String totalCals = totalCalories.getText().toString();
                    totalCals = totalCals.replaceFirst("/.*", "");

                    setTotalCalories(Integer.parseInt(totalCals));
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settingsButton) {
            Intent intent = new Intent(this, SettingsActivity.class);
            settingsResultLauncher.launch(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clearCalories(View view) {
        setTotalCalories(0);
        storeCalories(0);
    }

    @SuppressLint("DefaultLocale")
    public void addCalories() {
        int calories = Integer.parseInt(inputCalories.getText().toString());

        String totalCals = totalCalories.getText().toString();
        totalCals = totalCals.replace("/"+kcalDailyGoal+" "+kcal, "");

        int newTotalCals = Integer.parseInt(totalCals) + calories;

        setTotalCalories(newTotalCals);
        storeCalories(newTotalCals);
    }

    @SuppressLint("DefaultLocale")
    public void setTotalCalories(int calories) {
        totalCalories.setText(String.format("%d/%d %s", calories, kcalDailyGoal, kcal));
        calculateCalorieProgress(calories);
    }

    public void calculateCalorieProgress(int calories) {
        if (calories <= 0) {
            progressBar.setProgress(0);
            return;
        }

        if (calories > kcalDailyGoal) {
            progressBar.setProgress(100);
            return;
        }

        float calculation = (float)calories / kcalDailyGoal;
        String mainProgressValues = String.valueOf(calculation).substring(2,4);

        int progress = Integer.parseInt(mainProgressValues);

        progressBar.setProgress(progress);
    }

    public void storeCalories(int totalCalories) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CalorieCounter", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("caloriesToday", totalCalories);
        editor.apply();
    }
}