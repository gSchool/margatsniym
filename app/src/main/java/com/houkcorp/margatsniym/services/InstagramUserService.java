package com.houkcorp.margatsniym.services;

import com.houkcorp.margatsniym.models.InstagramMedia;
import com.houkcorp.margatsniym.models.InstagramResponse;
import com.houkcorp.margatsniym.models.InstagramUser;

import java.util.ArrayList;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface InstagramUserService {
    String ACCESS_TOKEN_QUERY = "access_token";

    @GET("self")
    Observable<Response<InstagramResponse<InstagramUser>>> getUser(
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );

    @GET("self/media/recent")
    Observable<Response<InstagramResponse<ArrayList<InstagramMedia>>>> getUsersRecentMedia(
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );

    /*For this route you will need
        https://www.instagram.com/oauth/authorize?client_id=85326b79ccca4d1f8e155765dcb8a7b7&redirect_uri=https://houkappdevelopment.wordpress.com/&response_type=code&scope=basic+public_content+follower_list+comments+relationships+likes
    */
    @GET("self/media/liked")
    Observable<Response<InstagramResponse<ArrayList<InstagramMedia>>>> getUsersLikedMedia(
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );
}