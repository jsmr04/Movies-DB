package com.example.jsmr.moviesdb;

import android.app.Application;

public class RoomApp extends Application {
    private static RoomApp sInstance;

    public static RoomApp getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }
}
