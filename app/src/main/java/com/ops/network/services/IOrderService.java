package com.ops.network.services;

import com.ops.models.response.BaseResponse;
import com.ops.models.response.TimeAvailabilityResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOrderService {

    @GET("order/availableTimes")
    Call<BaseResponse<TimeAvailabilityResponse>> availableTimes(@Query("restaurantId") int restaurantId,
                                                                @Query("date") String date);
}
