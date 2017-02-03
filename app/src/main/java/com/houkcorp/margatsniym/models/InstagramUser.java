package com.houkcorp.margatsniym.models;

import com.google.gson.annotations.SerializedName;

public class InstagramUser {

    @SerializedName("full_name")
    private String fullName;

    private double id;

    private String bio;

    private InstagramCounts counts;

    @SerializedName("profile_picture")
    private String profilePicture;

    private String website;

    public String getFullName() {
        return fullName;
    }

    public double getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }

    public InstagramCounts getCounts() {
        return counts;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getWebsite() {
        return website;
    }
}