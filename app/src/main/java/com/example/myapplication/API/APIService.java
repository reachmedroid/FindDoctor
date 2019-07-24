package com.example.myapplication.API;

import com.example.myapplication.Models.LoginResponse;
import com.example.myapplication.Models.SearchResultResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {


    /** Send POST request to server to validate
     * the user and fetch access token.
     * @return LoginResponse
     */
    @FormUrlEncoded
    @POST("/oauth/token?grant_type=password")
    Call<LoginResponse> authenticateUser(@Field("username")  String userName, @Field("password") String Password);


    /** Get doctors based on the search criteria,here geo location is
     * hardcoded to keep the design simple as delivery of this app is
     * intend in berlin area.
     * @return SearchResultResponse
     */
    @GET("api/users/me/doctors?lat=52.534709&lng=13.3976972")
    Call<SearchResultResponse> findNearestDoctor(@Query("search") String searchText);


    /** Get doctors based on the search criteria and also more doctors list,
     * here geo location is hardcoded to keep the design simple as delivery
     * of this app is intend in berlin area.
     * @return SearchResultResponse
     */

    @GET("api/users/me/doctors?lat=52.534709&lng=13.3976972")
    Call<SearchResultResponse> fetchMoreDoctorsData(@Query("search") String searchText,
                                                    @Query("lastKey") String lastKey);

    /** Get doctors based on the search criteria and also more doctors list,
     * here geo location is hardcoded to keep the design simple as delivery
     * of this app is intend in berlin area.
     * @return SearchResultResponse
     */

    @GET("api/doctors/{doctor-id}/keys/profilepictures?")
    Call<SearchResultResponse> fetchProfilePicture(@Path("doctor-id") String doctorID,
                                                    @Query("name") String doctorName);

}
