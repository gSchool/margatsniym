package com.houkcorp.margatsniym.models;

import android.os.Parcel;
import android.os.Parcelable;

public class InstagramMedia implements Parcelable {

    private MediaImages images;

    private MediaImages videos;

    private String type;

    protected InstagramMedia(Parcel in) {
        type = in.readString();
    }

    public static final Creator<InstagramMedia> CREATOR = new Creator<InstagramMedia>() {
        @Override
        public InstagramMedia createFromParcel(Parcel in) {
            return new InstagramMedia(in);
        }

        @Override
        public InstagramMedia[] newArray(int size) {
            return new InstagramMedia[size];
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