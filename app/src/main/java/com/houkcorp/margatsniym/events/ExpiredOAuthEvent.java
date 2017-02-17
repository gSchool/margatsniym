package com.houkcorp.margatsniym.events;

/**
 * An event for notifying when OAuth fails.
 */
public class ExpiredOAuthEvent {
    private boolean expired;

    public ExpiredOAuthEvent(boolean expired) {
        this.expired = expired;
    }

    public boolean isExpired() {
        return expired;
    }
}