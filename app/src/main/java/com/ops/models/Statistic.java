package com.ops.models;

import com.google.gson.annotations.SerializedName;

public class Statistic {

    @SerializedName("Reservation")
    private int reservationNumber;
    @SerializedName("Visits")
    private int visits;
    @SerializedName("LastRestaurant")
    private String lastRestaurantName;

    public Statistic(int reservationNumber, int visits, String lastRestaurantName) {
        this.reservationNumber = reservationNumber;
        this.visits = visits;
        this.lastRestaurantName = lastRestaurantName;
    }

    public int getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(int reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }

    public String getLastRestaurantName() {
        return lastRestaurantName;
    }

    public void setLastRestaurantName(String lastRestaurantName) {
        this.lastRestaurantName = lastRestaurantName;
    }
}
