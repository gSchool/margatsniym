package com.houkcorp.margatsniym.models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("full_name")
    private String fullName;

    private double id;
    private String bio;
    private MediaCounts counts;

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

    public MediaCounts getCounts() {
        return counts;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getWebsite() {
        return website;
    }
}