package com.example.myapplication.API;

import com.example.myapplication.Models.DoctorsFeed;
import com.example.myapplication.Models.LoginResponse;
import com.example.myapplication.Models.SearchResultResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIService {


    /** Send POST request to server to validate
     * the user and fetch access token.
     * @return LoginResponse
     */
    @FormUrlEncoded
    @POST("/oauth/token?grant_type=password")
    Call<LoginResponse> authenticateUser(@Field("username")  String userName, @Field("password") String Password);



    /*
     * We would be using the below url:
     * https://newsapi.org/v2/everything?q=movies&apiKey=079dac74a5f94ebdb990ecf61c8854b7&pageSize=20&page=2
     * The url has four query parameters.
     * We would be changing the pageSize and the page
     */
    @GET("/v2/everything")
    Call<DoctorsFeed> fetchFeed(@Query("q") String q,
                                @Query("apiKey") String apiKey,
                                @Query("page") long page,
                                @Query("pageSize") int pageSize);


    @GET("api/users/me/doctors?lat=52.534709&lng=13.3976972")
    Call<SearchResultResponse> findNearestDoctor(@Query("search") String searchText);


    @GET("api/users/me/doctors?lat=52.534709&lng=13.3976972")
    Call<SearchResultResponse> fetchMoreDoctorsData(@Query("search") String searchText,
                                                    @Query("lastKey") String lastKey);


}
