package com.ops.models;

import com.google.gson.annotations.SerializedName;

public class ReservationWeekStatistics {

    @SerializedName("day")
    private String day;
    @SerializedName("reservations")
    private int reservations;
    @SerializedName("reservationsInPercent")
    private double reservationsInPercent;

    public ReservationWeekStatistics(String day, int reservations, double reservationsInPercent) {
        this.day = day;
        this.reservations = reservations;
        this.reservationsInPercent = reservationsInPercent;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getReservations() {
        return reservations;
    }

    public void setReservations(int reservations) {
        this.reservations = reservations;
    }

    public double getReservationsInPercent() {
        return reservationsInPercent;
    }

    public void setReservationsInPercent(double reservationsInPercent) {
        this.reservationsInPercent = reservationsInPercent;
    }

}
