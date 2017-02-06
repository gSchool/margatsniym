package com.houkcorp.margatsniym.models;

import com.google.gson.annotations.SerializedName;

public class MediaImages {

    @SerializedName("standard_resolution")
    private MediaImagesMeta standardResolution;

    @SerializedName("low_resolution")
    private MediaImagesMeta lowResolution;

    @SerializedName("low_bandwidth")
    private MediaImagesMeta lowBandwidth;

    private MediaImagesMeta thumbnail;
}