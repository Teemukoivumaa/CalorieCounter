package com.teemukoivumaa.caloriecounter.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ProductHistory {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "product_name")
    public String productName;

    @ColumnInfo(name = "product_amount_with_prefix")
    public String productAmount;

    @ColumnInfo(name = "calorie_amount")
    public int calorieAmount;

    @ColumnInfo(name = "product_date")
    public String productDate;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setAmount(String amountPrefix) {
        this.productAmount = amountPrefix;
    }

    public String getAmount() {
        return productAmount;
    }

    public void setCalorieAmount(int calorieAmount) {
        this.calorieAmount = calorieAmount;
    }

    public int getCalorieAmount() {
        return calorieAmount;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }
}
