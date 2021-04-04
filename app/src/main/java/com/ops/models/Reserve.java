package com.ops.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Reserve implements Serializable {

    @SerializedName("Id")
    private int Id;
    @SerializedName("Date")
    private String date;
    @SerializedName("Time")
    private String time;
    @SerializedName("Restaurant")
    private Restaurant restaurant;
    @SerializedName("Guests")
    private int guestsNumber;
    @SerializedName("Wishes")
    private String wishes;

    public Reserve(int id, String date, String time, Restaurant restaurant, int guestsNumber, String wishes) {
        Id = id;
        this.date = date;
        this.time = time;
        this.restaurant = restaurant;
        this.guestsNumber = guestsNumber;
        this.wishes = wishes;
    }

    public Reserve(String date, String time, Restaurant restaurant, int guestsNumber, String wishes) {
        this.date = date;
        this.time = time;
        this.restaurant = restaurant;
        this.guestsNumber = guestsNumber;
        this.wishes = wishes;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getGuestsNumber() {
        return guestsNumber;
    }

    public void setGuestsNumber(int guestsNumber) {
        this.guestsNumber = guestsNumber;
    }

    public String getWishes() {
        return wishes;
    }

    public void setWishes(String wishes) {
        this.wishes = wishes;
    }
}
