package com.ops.models.request;

import com.google.gson.annotations.SerializedName;
import com.ops.models.Reserve;

public class RequestReserve {

    @SerializedName("reserve")
    private Reserve reserve;

    public RequestReserve(Reserve reserve) {
        this.reserve = reserve;
    }

    public Reserve getReserve() {
        return reserve;
    }

    public void setReserve(Reserve reserve) {
        this.reserve = reserve;
    }
}
