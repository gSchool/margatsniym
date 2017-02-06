package com.houkcorp.margatsniym.models;

public class InstagramResponse<T> {
    private T data;
    private InstagramError meta;
    private ResponsePagination pagination;

    public T getData() {
        return data;
    }

    public InstagramError getMeta() {
        return meta;
    }

    public ResponsePagination getPagination() {
        return pagination;
    }
}