package com.ops.models.response;

import com.google.gson.annotations.SerializedName;
import com.ops.models.Restaurant;

public class AdminMetaDataResponse {
    @SerializedName("restaurant")
    private Restaurant restaurant;

    public AdminMetaDataResponse() {
    }

    public AdminMetaDataResponse(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
