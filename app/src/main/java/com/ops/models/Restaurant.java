package com.ops.models;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable {

    private int Id;
    private String restaurantName;
    private RestaurantArea area;
    private String address;
    private String imageUrl;
    private List<RestaurantTables> restaurantTablesList;
    private String workTimeStart;
    private String workTimeEnd;
    private String phoneNumber;

    public Restaurant(int id, String restaurantName, RestaurantArea area, String address, String imageUrl, List<RestaurantTables> restaurantTablesList, String workTimeStart, String workTimeEnd, String phoneNumber) {
        Id = id;
        this.restaurantName = restaurantName;
        this.area = area;
        this.address = address;
        this.imageUrl = imageUrl;
        this.restaurantTablesList = restaurantTablesList;
        this.workTimeStart = workTimeStart;
        this.workTimeEnd = workTimeEnd;
        this.phoneNumber = phoneNumber;
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

    public RestaurantArea getArea() {
        return area;
    }

    public void setArea(RestaurantArea area) {
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
}
