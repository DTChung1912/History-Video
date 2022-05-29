package com.example.historyvideo.model;

public class User_Log {
    public String dateTime;
    public String movieWatched;

    public User_Log() {
    }

    public User_Log(String dateTime, String movieWatched) {
        this.dateTime = dateTime;
        this.movieWatched = movieWatched;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMovieWatched() {
        return movieWatched;
    }

    public void setMovieWatched(String movieWatched) {
        this.movieWatched = movieWatched;
    }
}
