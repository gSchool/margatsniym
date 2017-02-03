package com.houkcorp.margatsniym.services;

import com.houkcorp.margatsniym.models.InstagramResponse;
import com.houkcorp.margatsniym.models.InstagramUser;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface InstagramUserService {
    @GET("self")
    Observable<InstagramResponse<InstagramUser>> getUser(
            @Query("access_token") String accessToken
    );
}