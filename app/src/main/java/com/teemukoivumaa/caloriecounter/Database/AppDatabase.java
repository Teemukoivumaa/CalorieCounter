package com.teemukoivumaa.caloriecounter.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(
        version = 4,
        entities = {CalorieDay.class, CalorieProduct.class, ProductHistory.class},
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    private static final Object sLock = new Object();
    static final Migration MIGRATION_1_4 = new Migration(1, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `ProductHistory` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "`product_name` TEXT, `product_amount_with_prefix` TEXT, " +
                    "`calorie_amount` INTEGER NOT NULL," +
                    "`product_date` TEXT)"
            );

            database.execSQL("CREATE TABLE `CalorieDay` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "`calorie_date` TEXT, `calorie_amount` INTEGER NOT NULL)"
            );
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE `CalorieDay` (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "`calorie_date` TEXT, `calorie_amount` INTEGER NOT NULL)"
            );
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE `ProductHistory` " +
                    "ADD COLUMN `product_date` TEXT"
            );

            database.execSQL("DELETE FROM `ProductHistory` WHERE `product_date` is NULL");
        }
    };

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class, "ProductDatabase")
                        .addMigrations(MIGRATION_1_4, MIGRATION_2_3, MIGRATION_3_4)
                        .allowMainThreadQueries()
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract ProductDAO productDAO();
    public abstract CalorieDAO calorieDAO();
    public abstract ProductHistoryDAO productHistoryDAO();
}