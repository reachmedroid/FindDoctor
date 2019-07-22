package com.example.myapplication.Models;

public class AccessToken {

    public AccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String accessToken;
    private String tokenType;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}