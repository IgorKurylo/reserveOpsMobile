package com.ops.network.services;

import com.ops.models.Reserve;
import com.ops.models.request.RequestReserve;
import com.ops.models.response.BaseResponse;
import com.ops.models.response.ReserveResponse;
import com.ops.models.response.ReservesResponse;
import com.ops.models.response.TimeAvailabilityResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IReserveService {

    @POST("reserve")
    Call<BaseResponse<ReserveResponse>> createReserve(@Body RequestReserve request);

    @GET("reserve")
    Call<BaseResponse<ReservesResponse>> reserves(@Query("date") String date, @Query("restaurantId") int restId);

    @GET("reserve/availableTimes")
    Call<BaseResponse<TimeAvailabilityResponse>> availableTimes(@Query("restaurantId") int restaurantId,
                                                                @Query("date") String date);

    @PUT("reserve/{id}")
    Call<BaseResponse<ReserveResponse>> updateReservationStatus(@Body RequestReserve request, @Path("id") int Id);
}
