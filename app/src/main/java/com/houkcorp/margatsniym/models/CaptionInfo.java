package com.houkcorp.margatsniym.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A class for holding any caption data for Media.
 */
public class CaptionInfo implements Parcelable {
    private String text;

    protected CaptionInfo(Parcel in) {
        text = in.readString();
    }

    public static final Creator<CaptionInfo> CREATOR = new Creator<CaptionInfo>() {
        @Override
        public CaptionInfo createFromParcel(Parcel in) {
            return new CaptionInfo(in);
        }

        @Override
        public CaptionInfo[] newArray(int size) {
            return new CaptionInfo[size];
        }
    };

    public String getText() {
        return text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
    }
}