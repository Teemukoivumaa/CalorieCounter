package com.teemukoivumaa.caloriecounter;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CalorieDay.class}, version = 1)
public abstract class CalorieDatabase extends RoomDatabase {
    public abstract CalorieDAO calorieDAO();
}
