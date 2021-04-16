package com.ops.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Reserve implements Serializable {

    @SerializedName("id")
    private int Id;
    @SerializedName("date")
    private String date;
    @SerializedName("time")
    private String time;
    @SerializedName("restaurant")
    private Restaurant restaurant;
    @SerializedName("guests")
    private int guestsNumber;
    @SerializedName("comment")
    private String wishes;
    @SerializedName("status")
    private String status;

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

    public Reserve(int id, String date, String time, Restaurant restaurant, int guestsNumber, String wishes, String status) {
        Id = id;
        this.date = date;
        this.time = time;
        this.restaurant = restaurant;
        this.guestsNumber = guestsNumber;
        this.wishes = wishes;
        this.status = status;
    }

    public Reserve(int id, String status) {
        this.Id = id;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
