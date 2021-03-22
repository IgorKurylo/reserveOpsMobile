package com.ops.network;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class TokenAuthenticator implements Authenticator {
    public TokenAuthenticator(Context mContext) {
    }

    @Override
    public Request authenticate(Route route, @NotNull Response response) throws IOException {
        return null;
    }
}
