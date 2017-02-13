package com.houkcorp.margatsniym.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Media implements Parcelable {

    private MediaImages images;
    private MediaImages videos;
    private String type;
    private User user;
    private CaptionInfo caption;
    private LocationInfo location;
    private ArrayList<String> tags;

    @SerializedName("user_has_liked")
    private boolean hasLiked;

    protected Media(Parcel in) {
        images = in.readParcelable(MediaImages.class.getClassLoader());
        videos = in.readParcelable(MediaImages.class.getClassLoader());
        type = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
        caption = in.readParcelable(CaptionInfo.class.getClassLoader());
        location = in.readParcelable(LocationInfo.class.getClassLoader());
        tags = in.createStringArrayList();
        hasLiked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(images, flags);
        dest.writeParcelable(videos, flags);
        dest.writeString(type);
        dest.writeParcelable(user, flags);
        dest.writeParcelable(caption, flags);
        dest.writeParcelable(location, flags);
        dest.writeStringList(tags);
        dest.writeByte((byte) (hasLiked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    public CaptionInfo getCaption() {
        return caption;
    }

    public LocationInfo getLocation() {
        return location;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public boolean isHasLiked() {
        return hasLiked;
    }
}