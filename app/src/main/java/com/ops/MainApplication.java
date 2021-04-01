package com.ops;

import android.app.Application;

import com.ops.network.NetworkApi;
import com.ops.utils.CacheManager;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkApi.getInstance().Init(this);
        CacheManager.getInstance().Init(this);
    }
}
