package com.houkcorp.margatsniym.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * The basic size info of a Media Image.
 */
public class MediaImagesMeta implements Parcelable {
    private int width;
    private int height;
    private String url;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getUrl() {
        return url;
    }

    protected MediaImagesMeta(Parcel in) {
        width = in.readInt();
        height = in.readInt();
        url = in.readString();
    }

    public static final Creator<MediaImagesMeta> CREATOR = new Creator<MediaImagesMeta>() {
        @Override
        public MediaImagesMeta createFromParcel(Parcel in) {
            return new MediaImagesMeta(in);
        }

        @Override
        public MediaImagesMeta[] newArray(int size) {
            return new MediaImagesMeta[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeString(url);
    }
}