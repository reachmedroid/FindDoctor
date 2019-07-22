package com.example.myapplication.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

 /**
    This public static method will return Retrofit client
    anywhere in current application
  **/

public class RetrofitClient {

    public static Retrofit retroFit = null;

     public static Retrofit getRetrofitClient(String baseURL){
        if(retroFit==null){
            retroFit= new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retroFit;

    }

}
