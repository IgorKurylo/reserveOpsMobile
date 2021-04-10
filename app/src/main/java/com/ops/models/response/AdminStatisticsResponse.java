package com.ops.models.response;

import com.google.gson.annotations.SerializedName;
import com.ops.models.ReservationWeekStatistics;

import java.util.List;

public class AdminStatisticsResponse {
    @SerializedName("todayReservation")
    public int todayReservation;
    @SerializedName("pendingReservation")
    public int pendingReservation;
    @SerializedName("weekReservations")
    public List<ReservationWeekStatistics> reservationWeekList;

    public AdminStatisticsResponse(int todayReservation, int pendingReservation, List<ReservationWeekStatistics> reservationWeekList) {
        this.todayReservation = todayReservation;
        this.pendingReservation = pendingReservation;
        this.reservationWeekList = reservationWeekList;
    }

    public int getTodayReservation() {
        return todayReservation;
    }

    public void setTodayReservation(int todayReservation) {
        this.todayReservation = todayReservation;
    }

    public int getPendingReservation() {
        return pendingReservation;
    }

    public void setPendingReservation(int pendingReservation) {
        this.pendingReservation = pendingReservation;
    }

    public List<ReservationWeekStatistics> getReservationWeekList() {
        return reservationWeekList;
    }

    public void setReservationWeekList(List<ReservationWeekStatistics> reservationWeekList) {
        this.reservationWeekList = reservationWeekList;
    }
}
