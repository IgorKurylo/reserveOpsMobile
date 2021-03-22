package com.ops.models.response;

import com.google.gson.annotations.SerializedName;

public class ReserveResponse {

    @SerializedName("Id")
    private int OrderId;

    public ReserveResponse(int orderId) {
        OrderId = orderId;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }
}
