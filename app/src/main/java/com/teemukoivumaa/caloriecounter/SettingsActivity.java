package com.teemukoivumaa.caloriecounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private TextView newDailyGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        newDailyGoal = findViewById(R.id.newDailyGoal);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CalorieCounter", 0);
        newDailyGoal.setText(String.valueOf(pref.getInt("kcalDailyGoal", 0)));

        newDailyGoal.setOnEditorActionListener((v, actionId, event) -> {
            storeCalories(Integer.parseInt(newDailyGoal.getText().toString()));
            finish();
            return true;
        });
    }

    public void applyChanges(View view) {
        storeCalories(Integer.parseInt(newDailyGoal.getText().toString()));
        finish();
    }

    public void storeCalories(int newGoal) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CalorieCounter", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("kcalDailyGoal", newGoal);
        editor.apply();
    }

}