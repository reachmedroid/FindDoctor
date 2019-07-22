package com.example.myapplication.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResultResponse {


    public String refreshToken;
    @SerializedName("doctors")
    public List<DoctorsFeed> doctorsList;
}
