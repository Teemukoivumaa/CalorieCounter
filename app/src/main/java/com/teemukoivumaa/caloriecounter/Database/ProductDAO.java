package com.teemukoivumaa.caloriecounter.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductDAO {
    @Query("SELECT * FROM CalorieProduct")
    List<CalorieProduct> getAll();

    @Query("DELETE FROM CalorieProduct")
    void deleteALL();

    @Insert
    void insert(CalorieProduct calorieProduct);

}
