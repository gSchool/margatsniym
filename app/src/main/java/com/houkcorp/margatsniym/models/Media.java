package com.houkcorp.margatsniym.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Media implements Parcelable {

    private MediaImages images;

    private MediaImages videos;

    private String type;

    private User user;

    protected Media(Parcel in) {
        images = in.readParcelable(MediaImages.class.getClassLoader());
        videos = in.readParcelable(MediaImages.class.getClassLoader());
        type = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
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

    public User getUser() {
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(images, flags);
        dest.writeParcelable(videos, flags);
        dest.writeString(type);
        dest.writeParcelable(user, flags);
    }
}