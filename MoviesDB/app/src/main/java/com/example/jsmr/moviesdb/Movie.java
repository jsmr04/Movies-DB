package com.example.jsmr.moviesdb;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Movie  {

    @PrimaryKey
    private int id ;
    private String title;
    private String overview;
    private String backdrop_path;
    private String poster;
    private String genre;
    private int year;
    private int time;
    private double vote_average;

    public int getId() {return id;}

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPoster() {
        return poster;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() {
        return year;
    }

    public int getTime() {
        return time;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setId(int id) {this.id = id;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }
}


