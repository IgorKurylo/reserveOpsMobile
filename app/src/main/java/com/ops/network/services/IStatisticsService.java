package com.ops.network.services;

import com.ops.models.response.AdminStatisticsResponse;
import com.ops.models.response.StatisticsResponse;
import com.ops.models.response.BaseResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IStatisticsService {
    @GET("statistics/user")
    Call<BaseResponse<StatisticsResponse>> userStatistics();

    @GET("statistics/admin")
    Observable<BaseResponse<AdminStatisticsResponse>> adminStatistics(@Query("restaurantId") int restaurantId);

}
