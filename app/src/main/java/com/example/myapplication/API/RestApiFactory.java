package com.example.myapplication.API;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.myapplication.Utils.AuthenticateInterceptor;

import java.util.Iterator;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiFactory {



    private static Retrofit.Builder builder;


    private static Retrofit retrofit;

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient =  new OkHttpClient.Builder();

    // No need to instantiate this class.
    private RestApiFactory(Context context) {

    }

    public static <S> S createService(Class<S> serviceClass,String authType) {
        return createService(serviceClass, authType,"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhbmRyb2lkQ2hhbGxlbmdlQHZpdnkuY29tIiwic3R5cGUiOiJVU0VSIiwic2NvcGUiOlsiYmFzaWMiXSwiaWQiOiJlY2I3NDBlNS0wNzMxLTQ3ZWEtODAyNC03YzFjYTlhZWQzMjciLCJleHAiOjE1NjM2ODE2OTYsImp0aSI6ImZhN2ZhM2Y0LTk3ODctNGM5Mi1iMGVkLTkwZmYxOTM5OWE1MCIsImNsaWVudF9pZCI6ImlwaG9uZSJ9.s6V21KCJvim0VkZZ43nIVPPuCRrpU3dDC6gChNMIFs0");
    }



    public static <S> S createService(Class<S> serviceClass, final String authType,final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticateInterceptor interceptor = new AuthenticateInterceptor(authType, authToken);
            if (!httpClient.interceptors().contains(interceptor)) {
                // remove existing interceptors
                Iterator<Interceptor> interceptors = httpClient.interceptors().iterator();
                while (interceptors.hasNext()) {
                    if (interceptors.next() instanceof AuthenticateInterceptor) {
                        interceptors.remove();
                    }
                }
                // add new interceptor & update retrofit
                httpClient.addInterceptor(interceptor);
                builder= new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(authType=="Basic"?APIUtils.OATH_URL:APIUtils.BASE_URL);
                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        return create(serviceClass);
    }



    private static <S> S create(Class<S> serviceClass) {
        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);

            builder.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }

    public static Retrofit retrofit() {
        return retrofit;
    }
}
