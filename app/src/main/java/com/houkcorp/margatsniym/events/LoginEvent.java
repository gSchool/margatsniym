package com.houkcorp.margatsniym.events;

public class LoginEvent {
    private String accessToken;

    public LoginEvent(String token) {
        accessToken = token;
    }

    public String getAccessToken() {
        return accessToken;
    }
}