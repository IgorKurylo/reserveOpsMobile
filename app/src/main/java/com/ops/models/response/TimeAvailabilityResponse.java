package com.ops.models.response;

import com.google.gson.annotations.SerializedName;
import com.ops.models.AvailableTime;

import java.util.List;

public class TimeAvailabilityResponse {
    @SerializedName("availableTimes")
    private List<AvailableTime> availableTimeList;

    public TimeAvailabilityResponse(List<AvailableTime> availableTimeList) {
        this.availableTimeList = availableTimeList;
    }

    public List<AvailableTime> getAvailableTimeList() {
        return availableTimeList;
    }

    public void setAvailableTimeList(List<AvailableTime> availableTimeList) {
        this.availableTimeList = availableTimeList;
    }
}
