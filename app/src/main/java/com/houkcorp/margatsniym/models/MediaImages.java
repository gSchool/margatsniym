package com.houkcorp.margatsniym.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MediaImages implements Parcelable {

    @SerializedName("standard_resolution")
    private MediaImagesMeta standardResolution;

    @SerializedName("low_resolution")
    private MediaImagesMeta lowResolution;

    @SerializedName("low_bandwidth")
    private MediaImagesMeta lowBandwidth;

    private MediaImagesMeta thumbnail;

    public MediaImagesMeta getStandardResolution() {
        return standardResolution;
    }

    public MediaImagesMeta getLowResolution() {
        return lowResolution;
    }

    public MediaImagesMeta getLowBandwidth() {
        return lowBandwidth;
    }

    public MediaImagesMeta getThumbnail() {
        return thumbnail;
    }

    protected MediaImages(Parcel in) {
    }

    public static final Creator<MediaImages> CREATOR = new Creator<MediaImages>() {
        @Override
        public MediaImages createFromParcel(Parcel in) {
            return new MediaImages(in);
        }

        @Override
        public MediaImages[] newArray(int size) {
            return new MediaImages[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}