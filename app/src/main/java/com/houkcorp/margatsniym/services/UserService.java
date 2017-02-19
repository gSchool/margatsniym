package com.houkcorp.margatsniym.services;

import com.houkcorp.margatsniym.models.Media;
import com.houkcorp.margatsniym.models.MediaResponse;
import com.houkcorp.margatsniym.models.User;

import java.util.ArrayList;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * The different API route calls and data to send.
 */
public interface UserService {
    String ACCESS_TOKEN_QUERY = "access_token";

    @GET("users/self")
    Observable<Response<MediaResponse<User>>> getUser(
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );

    @GET("users/self/media/recent")
    Observable<Response<MediaResponse<ArrayList<Media>>>> getUsersRecentMedia(
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );

    /*For this route you will need
        https://www.instagram.com/oauth/authorize?client_id=85326b79ccca4d1f8e155765dcb8a7b7&redirect_uri=https://houkappdevelopment.wordpress.com/&response_type=code&scope=basic+public_content+follower_list+comments+relationships+likes
    */
    @GET("users/self/media/liked")
    Observable<Response<MediaResponse<ArrayList<Media>>>> getUsersLikedMedia(
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );

    @GET("users/self/follows")
    Observable<Response<MediaResponse<ArrayList<User>>>> getFollowedUsers(
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );

    @GET("users/{user_id}/media/recent")
    Observable<Response<MediaResponse<ArrayList<Media>>>> getFollowedUsersMedia(
            @Path("user_id") long userId,
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );

    @POST("media/{media_id}/likes")
    Observable<Response<MediaResponse>> likeMedia(
            @Path("media_id") String mediaId,
            @Query(ACCESS_TOKEN_QUERY) String accessToken,
            @Body String empty
    );

    @DELETE("media/{media_id}/likes")
    Observable<Response<MediaResponse>> unLikeMedia(
            @Path("media_id") String mediaId,
            @Query(ACCESS_TOKEN_QUERY) String accessToken
    );
}