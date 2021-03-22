package com.ops.models.response;

import com.google.gson.annotations.SerializedName;
import com.ops.models.Reserve;

import java.util.List;

public class ReservesResponse {

    @SerializedName("reserves")
    private List<Reserve> reserveList;

    public ReservesResponse(List<Reserve> reserveList) {
        this.reserveList = reserveList;
    }

    public List<Reserve> getReserveList() {
        return reserveList;
    }

    public void setReserveList(List<Reserve> reserveList) {
        this.reserveList = reserveList;
    }
}
