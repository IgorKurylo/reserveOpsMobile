package com.ops.network.services;

import com.ops.models.AccessToken;
import com.ops.models.AuthCredentials;
import com.ops.models.User;
import com.ops.models.response.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAuthService {

    @POST("auth")
    Call<BaseResponse<AccessToken>> authentication(@Body AuthCredentials authCredentials);
    @POST("auth/register")
    Call<BaseResponse<User>> register(@Body User user);

}
