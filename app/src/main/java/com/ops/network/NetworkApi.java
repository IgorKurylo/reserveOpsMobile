package com.ops.network;

import android.content.Context;


import com.ops.R;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkApi {

    static private Retrofit retrofit = null;
    static private NetworkApi networkApi;
    private Context mContext;

    static public NetworkApi getInstance() {
        if (networkApi == null) {
            networkApi = new NetworkApi();
        }
        return networkApi;
    }

    public void Init(Context context) {

        mContext = context;
        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.baseUrl))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(httpClient())
                .build();

    }

    private OkHttpClient httpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.authenticator(new TokenAuthenticator());
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

}
