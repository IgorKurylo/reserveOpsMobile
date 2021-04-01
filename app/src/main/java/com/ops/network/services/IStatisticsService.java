package com.ops.network.services;

import com.ops.models.response.StatisticsResponse;
import com.ops.models.response.BaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IStatisticsService {
    @GET("statistics")
    Call<BaseResponse<StatisticsResponse>> statistics();
}
