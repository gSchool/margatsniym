package com.houkcorp.margatsniym.events;

public class ExpiredOAuthEvent {
    private boolean expired;

    public ExpiredOAuthEvent(boolean expired) {
        this.expired = expired;
    }

    public boolean isExpired() {
        return expired;
    }
}