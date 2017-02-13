package com.houkcorp.margatsniym.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationInfo implements Parcelable {
    private double latitude;
    private double longitude;
    private String name;

    protected LocationInfo(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }
}