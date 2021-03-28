package com.ops.models.request;

import com.google.gson.annotations.SerializedName;
import com.ops.models.Area;

import java.util.List;

import retrofit2.http.Body;

public class RequestRestaurant {

    @SerializedName("areas")
    List<Area> areas;

    public RequestRestaurant(List<Area> areas) {
        this.areas = areas;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }
}
