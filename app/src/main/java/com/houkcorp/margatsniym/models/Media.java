package com.houkcorp.margatsniym.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Media implements Parcelable {

    private MediaImages images;

    private MediaImages videos;

    private String type;

    protected Media(Parcel in) {
        type = in.readString();
    }

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel in) {
            return new Media(in);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public MediaImages getImages() {
        return images;
    }

    public MediaImages getVideos() {
        return videos;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
    }
}