package com.houkcorp.margatsniym.services;

import com.houkcorp.margatsniym.models.Media;
import com.houkcorp.margatsniym.models.MediaResponse;
import com.houkcorp.margatsniym.models.User;

import java.util.ArrayList;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface UserService {
    String ACCESS_TOKEN_QUERY = "access_token";

    @GET("self")
    Observable<Response<MediaResponse<User>>> getUser(
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );

    @GET("self/media/recent")
    Observable<Response<MediaResponse<ArrayList<Media>>>> getUsersRecentMedia(
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );

    /*For this route you will need
        https://www.instagram.com/oauth/authorize?client_id=85326b79ccca4d1f8e155765dcb8a7b7&redirect_uri=https://houkappdevelopment.wordpress.com/&response_type=code&scope=basic+public_content+follower_list+comments+relationships+likes
    */
    @GET("self/media/liked")
    Observable<Response<MediaResponse<ArrayList<Media>>>> getUsersLikedMedia(
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );

    @GET("self/follows")
    Observable<Response<MediaResponse<ArrayList<User>>>> getFollowedUsers(
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );

    @GET("{user_id}/media/recent")
    Observable<Response<MediaResponse<ArrayList<Media>>>> getFollowedUsersMedia(
            @Path("user_id") long userId,
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );
}