package com.example.myapplication.API;

import android.text.TextUtils;
import com.example.myapplication.Utils.AuthenticateInterceptor;
import java.util.Iterator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 *  A Factory class to create & build the retrofit request to server with
 *  needed header and build parameters before sending request to server.
 */
public class RestApiFactory {


    private static Retrofit.Builder builder;
    private static Retrofit retrofit;

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient =  new OkHttpClient.Builder();


    public static <S> S createService(Class<S> serviceClass, final String authType,final String authToken,final String baseURL) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticateInterceptor interceptor = new AuthenticateInterceptor(authType, authToken);
            if (!httpClient.interceptors().contains(interceptor)) {
                Iterator<Interceptor> interceptors = httpClient.interceptors().iterator();
                while (interceptors.hasNext()) {
                    if (interceptors.next() instanceof AuthenticateInterceptor) {
                        interceptors.remove();
                    }
                }
                httpClient.addInterceptor(interceptor);
                builder= new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(baseURL);
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

}
