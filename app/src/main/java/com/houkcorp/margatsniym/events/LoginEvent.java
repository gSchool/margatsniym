package com.houkcorp.margatsniym.events;

/**
 * An event that launches when the login succeeds.  This event returns the Access Token.
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