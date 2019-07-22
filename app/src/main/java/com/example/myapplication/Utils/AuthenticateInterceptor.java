package com.example.myapplication.Utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticateInterceptor  implements Interceptor {

    private String authToken;
    private String authType;

    public AuthenticateInterceptor(String tokenType, String token) {
        this.authToken = token;
        this.authType= tokenType;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .addHeader("Authorization", authType+" " +authToken);
        Request request = builder.build();
        return chain.proceed(request);
    }
}