package com.example.jsmr.moviesdb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Movie.class},version = 1)
public abstract class MovieDatabase extends RoomDatabase {

    private static volatile MovieDatabase sInstance;
    private static final String DB_NAME = "Movie.db";

    public static MovieDatabase getInstance() {
        if (sInstance == null) {
            sInstance = create(RoomApp.getInstance().getApplicationContext());
        }
        return sInstance;
    }

    private static MovieDatabase create(Context context) {
        return Room.databaseBuilder(
                context,
                MovieDatabase.class,
                DB_NAME).fallbackToDestructiveMigration().build();
    }

    public abstract IMovieDao MovieDao();

}
