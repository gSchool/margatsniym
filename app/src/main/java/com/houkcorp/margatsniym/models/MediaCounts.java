package com.houkcorp.margatsniym.models;

import com.google.gson.annotations.SerializedName;

/**
 * The follow counts for media.
 */
public class MediaCounts {
    private double media;

    @SerializedName("followed_by")
    private double followedBy;

    private double follows;

    public double getMedia() {
        return media;
    }

    public double getFollowedBy() {
        return followedBy;
    }

    public double getFollows() {
        return follows;
    }
}