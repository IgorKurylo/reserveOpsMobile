package com.ops.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable {

    @SerializedName("Id")
    private int Id;
    @SerializedName("Name")
    private String restaurantName;
    @SerializedName("Area")
    private String area;
    @SerializedName("Address")
    private String address;
    @SerializedName("imageUrl")
    private String imageUrl;
    @SerializedName("Tables")
    private List<RestaurantTables> restaurantTablesList;
    @SerializedName("StartTime")
    private String workTimeStart;
    @SerializedName("StartTime")
    private String workTimeEnd;
    @SerializedName("PhoneNumber")
    private String phoneNumber;
    @SerializedName("WebSite")
    private String webSite;

    public Restaurant(int id, String restaurantName, String area, String address, String imageUrl, List<RestaurantTables> restaurantTablesList, String workTimeStart, String workTimeEnd, String phoneNumber, String webSite) {
        Id = id;
        this.restaurantName = restaurantName;
        this.area = area;
        this.address = address;
        this.imageUrl = imageUrl;
        this.restaurantTablesList = restaurantTablesList;
        this.workTimeStart = workTimeStart;
        this.workTimeEnd = workTimeEnd;
        this.phoneNumber = phoneNumber;
        this.webSite = webSite;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<RestaurantTables> getRestaurantTablesList() {
        return restaurantTablesList;
    }

    public void setRestaurantTablesList(List<RestaurantTables> restaurantTablesList) {
        this.restaurantTablesList = restaurantTablesList;
    }

    public String getWorkTimeStart() {
        return workTimeStart;
    }

    public void setWorkTimeStart(String workTimeStart) {
        this.workTimeStart = workTimeStart;
    }

    public String getWorkTimeEnd() {
        return workTimeEnd;
    }

    public void setWorkTimeEnd(String workTimeEnd) {
        this.workTimeEnd = workTimeEnd;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
}
