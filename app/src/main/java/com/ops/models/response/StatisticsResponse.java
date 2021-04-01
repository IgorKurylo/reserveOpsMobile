package com.ops.models.response;

import com.google.gson.annotations.SerializedName;
import com.ops.models.Reserve;
import com.ops.models.Restaurant;
import com.ops.models.Statistic;

public class StatisticsResponse {
    @SerializedName("upComingReserve")
    private Reserve reserve;
    @SerializedName("numberOfReservation")
    private int numberOfReservation;
    @SerializedName("lastRestaurantVisit")
    private Restaurant restaurant;

    public StatisticsResponse(Reserve reserve, int numberOfReservation, Restaurant restaurant) {
        this.reserve = reserve;
        this.numberOfReservation = numberOfReservation;
        this.restaurant = restaurant;
    }

    public Reserve getReserve() {
        return reserve;
    }

    public void setReserve(Reserve reserve) {
        this.reserve = reserve;
    }

    public int getNumberOfReservation() {
        return numberOfReservation;
    }

    public void setNumberOfReservation(int numberOfReservation) {
        this.numberOfReservation = numberOfReservation;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
