package com.ops.models;

import com.google.gson.annotations.SerializedName;

public class AvailableTime {
    @SerializedName("time")
    private String time;
    @SerializedName("isAvailable")
    private boolean isAvailable;

    public AvailableTime(String time, boolean isAvailable) {
        this.time = time;
        this.isAvailable = isAvailable;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
