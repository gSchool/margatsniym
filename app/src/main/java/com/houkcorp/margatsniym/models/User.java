package com.houkcorp.margatsniym.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName("full_name")
    private String fullName;

    private long id;
    private String bio;
    private MediaCounts counts;

    @SerializedName("profile_picture")
    private String profilePicture;

    private String website;

    protected User(Parcel in) {
        fullName = in.readString();
        id = in.readLong();
        bio = in.readString();
        profilePicture = in.readString();
        website = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFullName() {
        return fullName;
    }

    public long getId() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeLong(id);
        dest.writeString(bio);
        dest.writeString(profilePicture);
        dest.writeString(website);
    }
}