package com.houkcorp.margatsniym.models;

import com.google.gson.annotations.SerializedName;

public class InstagramCounts {
    private double media;

    @SerializedName("followed_by")
    private double followedBy;

    private double follows;
}