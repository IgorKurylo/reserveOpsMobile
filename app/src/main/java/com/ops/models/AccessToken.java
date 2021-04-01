package com.ops.models;

import com.google.gson.annotations.SerializedName;

public class AccessToken {
    @SerializedName("accessToken")
    String accessToken;
    @SerializedName("role")
    String role;
    @SerializedName("user")
    User user;

    public AccessToken(String accessToken, String role, User user) {
        this.accessToken = accessToken;
        this.role = role;
        this.user = user;
    }

    public AccessToken(String accessToken, String role) {
        this.accessToken = accessToken;
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
