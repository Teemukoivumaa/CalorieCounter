package com.teemukoivumaa.caloriecounter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProgressActivity extends AppCompatActivity {

    private final SimpleDateFormat shortDateFormat = new SimpleDateFormat("d.M EEE", Locale.ENGLISH);
    private final SimpleDateFormat longDateFormat = new SimpleDateFormat("y-MM-d", Locale.ENGLISH);

    private CalorieDAO calorieDAO;

    private final List<String> calorieDates = new ArrayList<>();
    private final List<Integer> calories = new ArrayList<>();
    private final List<TextView> textViews = new ArrayList<>();
    private final List<ProgressBar> progressBars = new ArrayList<>();

    private final List<Integer> progressBarIds = Arrays.asList(
            R.id.progressBar1,
            R.id.progressBar2,
            R.id.progressBar3,
            R.id.progressBar4,
            R.id.progressBar5,
            R.id.progressBar6,
            R.id.progressBar7
    );
    private final List<Integer> textIds = Arrays.asList(
            R.id.daySeven,
            R.id.daySix,
            R.id.dayFive,
            R.id.dayFour,
            R.id.dayThree,
            R.id.dayTwo,
            R.id.dayOne
    );


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        CalorieDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                CalorieDatabase.class,
                "calorie-counter"
        ).allowMainThreadQueries().build();

        calorieDAO = db.calorieDAO();
        getProgressData();
        getScreenItems();

        int max = getMax();

        for (int i = 0; i < 7; i++) {
            String calorieDate = calorieDates.get(i);
            int calsToday = calories.get(i);

            ProgressBar progressBar = progressBars.get(i);
            progressBar.setMax(max);
            progressBar.setProgress(calsToday);

            TextView calsText = textViews.get(i);
            calsText.setText(String.format("%d", calsToday));
            calsText.setTextSize(15);
            calsText.setTooltipText(calorieDate);
        }
    }

    private void getScreenItems() {
        for (int i = 0; i < 7; i++) {
            ProgressBar progressBar = findViewById(progressBarIds.get(i));
            TextView textView = findViewById(textIds.get(i));

            progressBars.add(progressBar);
            textViews.add(textView);
        }
    }

    /*
    private void test(CalorieDAO calorieDAO) {
        String date = getCurrentDateLong();
        for (int i = 1; i < 20; i++) {
            date = getDateBackwards(date, -i);
            CalorieDay abc = new CalorieDay();
            abc.setCalorieAmount(new Random().nextInt((3000 - 500) + 1));
            abc.setCalorieDate(date);
            calorieDAO.insert(abc);
        }
    }
    */

    private void getProgressData() {
        String startDate = getCurrentDate(longDateFormat);
        CalorieDay calorieDayToday = calorieDAO.getCalorieDayByDate(startDate);
        int caloriesToday = 0;

        if (calorieDayToday != null) { // If there's a record for today, get data from it.
            startDate = calorieDayToday.getCalorieDate();
            caloriesToday = calorieDayToday.getCalorieAmount();
        }

        for (int i = -6; i < 0; i++) { // This counts 6 days backwards and tries to get the CalorieDays.
            String dateToCheck = getDate(startDate, i);
            CalorieDay calorieDay = calorieDAO.getCalorieDayByDate(dateToCheck);

            if (calorieDay != null) { // If CalorieDay existed on that day, get values from that day.
                calorieDates.add(
                        convertFromLongToShort(calorieDay.getCalorieDate())
                );
                calories.add(calorieDay.getCalorieAmount());
            } else { // CalorieDay didn't exist on that day, "create" a day with 0 calories.
                calorieDates.add(convertFromLongToShort(dateToCheck));
                calories.add(0);
            }
        }

        calorieDates.add(getCurrentDate(shortDateFormat));
        calories.add(caloriesToday);
    }

    private String getCurrentDate(SimpleDateFormat sdf) {
        Date currentTime = Calendar.getInstance().getTime();

        return sdf.format(currentTime);
    }

    private String convertFromLongToShort(String longDate) { // Converts long date to short version. Example: "2022-04-18" -> "18.4 Mon"
        Calendar calendar = Calendar.getInstance();
        Date date = convertStringToDate(longDate, longDateFormat);

        assert date != null;
        calendar.setTime(date);

        Date newDate = calendar.getTime();
        return shortDateFormat.format(newDate);
    }

    // Get date going backwards or forwards of given date and return the requested date.
    // Example: startDate = "2022-04-18", numOfDays = -6. Returned date is: "2022-04-12"
    private String getDate(String startDate, int numberOfDays) {
        Calendar calendar = Calendar.getInstance();
        Date date = convertStringToDate(startDate, longDateFormat);

        assert date != null;
        calendar.setTime(date);

        calendar.add(Calendar.DAY_OF_YEAR, numberOfDays);
        Date newDate = calendar.getTime();
        return longDateFormat.format(newDate);
    }

    private Date convertStringToDate(String stringDate, SimpleDateFormat sdf) { // Convert string to date with given format.
        try {
            return sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            // Create error msg
        }
        return null;
    }

    private int getMax() { // Get the max amount of calories.
        int max = 0;
        for (int comparable : calories) {
            if (max < comparable) {
                max = comparable;
            }
        }
        return max;
    }

}