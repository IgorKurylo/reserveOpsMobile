package com.ops.models;

import com.google.gson.annotations.SerializedName;

public class AccessToken {
    @SerializedName("accessToken")
    String accessToken;
    @SerializedName("role")
    String role;

    public AccessToken(String accessToken, String role) {
        this.accessToken = accessToken;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
