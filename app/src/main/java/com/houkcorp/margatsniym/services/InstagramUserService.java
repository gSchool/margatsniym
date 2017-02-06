package com.houkcorp.margatsniym.services;

import com.houkcorp.margatsniym.models.InstagramMedia;
import com.houkcorp.margatsniym.models.InstagramResponse;
import com.houkcorp.margatsniym.models.InstagramUser;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface InstagramUserService {
    String ACCESS_TOKEN_QUERY = "access_token";

    @GET("self")
    Observable<InstagramResponse<InstagramUser>> getUser(
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );

    @GET("self/media/recent")
    Observable<InstagramResponse<InstagramMedia>> getUsersRecentMedia(
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );
}