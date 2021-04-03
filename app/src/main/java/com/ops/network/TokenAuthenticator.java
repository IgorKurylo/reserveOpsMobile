package com.ops.network;

import android.content.Context;

import com.ops.utils.CacheManager;
import com.ops.utils.Constant;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {
    public TokenAuthenticator() {
    }

    @Override
    public Request authenticate(Route route, @NotNull Response response) throws IOException {
        Request request = null;
        String accessToken = CacheManager.getInstance().getString(Constant.ACCESS_TOKEN);
        request = response.request().newBuilder()
                .header("Authorization", String.format("Bearer %s", accessToken)).build();
        return request;
    }
}
