package com.ops.models.response;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {

    @SerializedName("data")
    private T data;
    @SerializedName("isSuccess")
    private boolean isSuccess;
    @SerializedName("error")
    private String error;

    public BaseResponse(T data, boolean isSuccess, String error) {
        this.data = data;
        this.isSuccess = isSuccess;
        this.error = error;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
