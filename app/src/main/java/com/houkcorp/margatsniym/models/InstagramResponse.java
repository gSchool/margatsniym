package com.houkcorp.margatsniym.models;

public class InstagramResponse<T> {
    private T data;
    private InstagramError meta;

    public T getData() {
        return data;
    }

    public InstagramError getMeta() {
        return meta;
    }
}