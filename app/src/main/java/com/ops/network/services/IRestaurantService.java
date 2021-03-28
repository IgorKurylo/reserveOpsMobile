package com.ops.network.services;

import com.ops.models.Restaurant;
import com.ops.models.request.RequestRestaurant;
import com.ops.models.response.BaseResponse;
import com.ops.models.response.RestaurantResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IRestaurantService {

    @POST("restaurant/list")
    Call<BaseResponse<RestaurantResponse>> restaurantsByArea(@Body RequestRestaurant request);


}
