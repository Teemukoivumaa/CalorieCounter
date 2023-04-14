package com.teemukoivumaa.caloriecounter.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProductHistoryDAO {
    @Query("SELECT * FROM ProductHistory")
    List<ProductHistory> getAll();

    @Query("DELETE FROM ProductHistory")
    void deleteALL();

    @Insert
    void insert(ProductHistory productHistory);
}
