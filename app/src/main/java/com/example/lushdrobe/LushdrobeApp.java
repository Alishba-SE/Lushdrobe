package com.example.lushdrobe;

import android.app.Application;
import com.example.lushdrobe.utils.DatabaseHelper;

public class LushdrobeApp extends Application {
    private static DatabaseHelper databaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper = new DatabaseHelper(this);
    }

    public static DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    @Override
    public void onTerminate() {
        databaseHelper.close();
        super.onTerminate();
    }
}