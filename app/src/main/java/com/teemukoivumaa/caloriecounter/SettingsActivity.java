package com.teemukoivumaa.caloriecounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class SettingsActivity extends AppCompatActivity {

    private TextView newDailyGoal;

    private CalorieDAO calorieDAO;
    private Snackbar snackbar;
    private View contextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        newDailyGoal = findViewById(R.id.newDailyGoal);
        ConstraintLayout settingsLayout = findViewById(R.id.settingsLayout);
        contextView = this.findViewById(android.R.id.content);
        snackbar = Snackbar.make(
                contextView,
                "Are you sure you want to delete all data?\nThis can't be undone. Click anywhere to dismiss.",
                Snackbar.LENGTH_INDEFINITE
        );

        SharedPreferences pref = getApplicationContext().getSharedPreferences("CalorieCounter", 0);
        newDailyGoal.setText(String.valueOf(pref.getInt("kcalDailyGoal", 0)));

        newDailyGoal.setOnEditorActionListener((v, actionId, event) -> {
            storeCalories(Integer.parseInt(newDailyGoal.getText().toString()));
            finish();
            return true;
        });

        settingsLayout.setOnClickListener(v -> {
            if (snackbar.isShown()) {
                snackbar.dismiss();
            }
        });

        CalorieDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                CalorieDatabase.class,
                "calorie-counter"
        ).allowMainThreadQueries().build();

        calorieDAO = db.calorieDAO();
    }

    public void deleteAllQuestion(View view) {
        snackbar.setAction("YES", context -> youSure())
                .setActionTextColor(getColor(android.R.color.holo_red_dark));
        snackbar.show();
    }

    private void youSure() {
        snackbar = Snackbar.make(
                contextView,
                "Are you sure, you want to do that?\nClick anywhere to dismiss.",
                Snackbar.LENGTH_INDEFINITE)
                .setAction("YES", context -> deleteAll())
                .setActionTextColor(getColor(android.R.color.holo_red_dark)
                );
        snackbar.show();
    }

    private void deleteAll() {
        calorieDAO.deleteALL();
        Snackbar snackbar = Snackbar
                .make(contextView, "All data deleted.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void applyChanges(View view) {
        storeCalories(Integer.parseInt(newDailyGoal.getText().toString()));
        finish();
    }

    private void storeCalories(int newGoal) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CalorieCounter", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("kcalDailyGoal", newGoal);
        editor.apply();
    }

}