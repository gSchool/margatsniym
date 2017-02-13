package com.houkcorp.margatsniym.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationInfo implements Parcelable {
    private float latitude;
    private float longitude;
    private String name;

    protected LocationInfo(Parcel in) {
        latitude = in.readFloat();
        longitude = in.readFloat();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LocationInfo> CREATOR = new Creator<LocationInfo>() {
        @Override
        public LocationInfo createFromParcel(Parcel in) {
            return new LocationInfo(in);
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }
}