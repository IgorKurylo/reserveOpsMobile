package com.ops.network.services;

import com.ops.models.Restaurant;
import com.ops.models.response.BaseResponse;
import com.ops.models.response.RestaurantResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRestaurantService {

    @GET("/restaurants")
    Call<BaseResponse<RestaurantResponse>> restaurantsByArea(@Query("area") int area);


}
