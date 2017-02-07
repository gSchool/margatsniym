package com.houkcorp.margatsniym.models;

public class MediaResponse<T> {
    private T data;
    private ResponseError meta;
    private ResponsePagination pagination;

    public T getData() {
        return data;
    }

    public ResponseError getMeta() {
        return meta;
    }

    public ResponsePagination getPagination() {
        return pagination;
    }
}