package com.houkcorp.margatsniym.models;

import com.google.gson.annotations.SerializedName;

public class ResponseError {
    @SerializedName("error_type")
    private String errorType;

    private int code;

    @SerializedName("error_message")
    private String errorMessage;

    public String getErrorType() {
        return errorType;
    }

    public int getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}