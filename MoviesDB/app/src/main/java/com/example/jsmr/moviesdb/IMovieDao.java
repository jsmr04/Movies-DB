package com.example.jsmr.moviesdb;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IMovieDao {
    @Query("SELECT * FROM movie WHERE id = :id")
    public Movie getById(int id);

    @Query("SELECT * FROM movie")
    public List<Movie> listAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Movie movie);

    @Delete
    public void delete(Movie movie);
}
