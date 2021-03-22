package com.ops;

import android.app.Application;

import com.ops.network.NetworkApi;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkApi.getInstance().Init(this);
    }
}
