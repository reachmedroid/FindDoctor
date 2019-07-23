package com.example.myapplication.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResultResponse {


    @SerializedName("doctors")
    public List<DoctorsFeed> doctorsList;
    @SerializedName("lastKey")
    public String nextKey;

}
