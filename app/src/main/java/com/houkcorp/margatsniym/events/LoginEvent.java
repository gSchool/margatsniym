package com.houkcorp.margatsniym.events;

/**
 * An event for notifying when login is successful.  This event returns the Access Token.
 */
public class LoginEvent {
    private String accessToken;

    public LoginEvent(String token) {
        accessToken = token;
    }

    public String getAccessToken() {
        return accessToken;
    }
}