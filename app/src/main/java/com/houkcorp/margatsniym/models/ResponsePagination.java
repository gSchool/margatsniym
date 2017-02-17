package com.houkcorp.margatsniym.models;

import com.google.gson.annotations.SerializedName;

/**
 * The page information for a list of data returned from the Instagram API.
 */
public class ResponsePagination {
    @SerializedName("next_url")
    private String nextUrl;

    @SerializedName("next_max_id")
    private double nextMaxId;

    public String getNextUrl() {
        return nextUrl;
    }

    public double getNextMaxId() {
        return nextMaxId;
    }
}