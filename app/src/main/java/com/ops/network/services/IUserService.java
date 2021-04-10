package com.ops.network.services;

import com.ops.models.UserMetaData;
import com.ops.models.response.AdminMetaDataResponse;
import com.ops.models.response.BaseResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IUserService {
    @GET("user/metadata")
    Observable<BaseResponse<AdminMetaDataResponse>> metaData();
}
