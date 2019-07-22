package com.example.myapplication.Models;

class AuthToken {
    private static final AuthToken ourInstance = new AuthToken();

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    private String accessToken;

    static AuthToken getInstance() {
        return ourInstance;
    }

    private AuthToken() {
    }
}
