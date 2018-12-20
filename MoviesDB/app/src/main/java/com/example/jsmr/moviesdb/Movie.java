package com.example.jsmr.moviesdb;

import java.util.Date;

public class Movie {
            /*
            name
            poster
            backdrop
            genre
            time
            date
            vote_average
         */

    private String title;
    private String overview;
    private String backdrop_path;
    private String poster;
    private String genre;
    private int year;
    private int time;
    private Date date;
    private double vote_average;


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

    public Date getDate() {
        return date;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDate(Date date) {
        this.date = date;
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
