package com.ops.network.services;

import com.ops.models.Reserve;
import com.ops.models.response.BaseResponse;
import com.ops.models.response.ReserveResponse;
import com.ops.models.response.ReservesResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IReserveService {

    @POST("/reserve")
    Call<BaseResponse<ReserveResponse>> makeReserve(@Body Reserve reserve);

    @GET("/reserve/next")
    Call<BaseResponse<ReserveResponse>> upComingReserve();

    @GET("/reserve")
    Call<BaseResponse<ReservesResponse>> reserves(@Query("date") String date);
}
