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

    protected MediaImages(Parcel in) {
        standardResolution = in.readParcelable(MediaImagesMeta.class.getClassLoader());
        lowResolution = in.readParcelable(MediaImagesMeta.class.getClassLoader());
        lowBandwidth = in.readParcelable(MediaImagesMeta.class.getClassLoader());
        thumbnail = in.readParcelable(MediaImagesMeta.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(standardResolution, flags);
        dest.writeParcelable(lowResolution, flags);
        dest.writeParcelable(lowBandwidth, flags);
        dest.writeParcelable(thumbnail, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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
}