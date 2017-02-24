package com.houkcorp.margatsniym.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.adapters.FollowedUserImageAdapter;
import com.houkcorp.margatsniym.events.ExpiredOAuthEvent;
import com.houkcorp.margatsniym.events.MediaChangedEvent;
import com.houkcorp.margatsniym.models.Media;
import com.houkcorp.margatsniym.models.MediaResponse;
import com.houkcorp.margatsniym.models.User;
import com.houkcorp.margatsniym.services.ServiceFactory;
import com.houkcorp.margatsniym.services.UserService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A Recycler view of max 20 users and max 5 images that are followed by the logged in user.
 */
public class FollowedUserImageListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String FOLLOWED_USER_IMAGE_LIST_FRAGMENT = "FollowedUserImageListFragment";

    private ArrayList<ArrayList<Media>> mFollowedUsersMedia = new ArrayList<>();
    private boolean isDualPane = false;
    private boolean isSyncingData = false;
    private FollowedUserImageAdapter mFollowedRecyclerAdapter;
    private String mAccessToken;

    @BindView(R.id.followed_users_progress_bar) ProgressBar mFollowedProgressBar;
    @BindView(R.id.followed_users_recycler_view) RecyclerView mFollowedRecyclerView;
    @BindView(R.id.followed_users_swipe_refresh_layout) SwipeRefreshLayout mFollowedSwipeRefreshLayout;

    /**
     * Retrieve a new instance of FollowedUserImageListFragment
     *
     * @param accessToken The access key of the logged in user.
     * @return Returns a new instance of FollowedUserImageListFragment
     */
    public static FollowedUserImageListFragment newInstance(String accessToken) {
        Bundle extras = new Bundle();
        extras.putString(MyUserFragment.INSTAGRAM_ACCESS_TOKEN, accessToken);

        FollowedUserImageListFragment followedUserImageListFragment = new FollowedUserImageListFragment();
        followedUserImageListFragment.setArguments(extras);

        return followedUserImageListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_followed_user_image_list, container, false);

        ButterKnife.bind(this, root);

        // Hide the recycler view and show the progress bar.
        mFollowedProgressBar.setVisibility(View.VISIBLE);
        mFollowedSwipeRefreshLayout.setVisibility(View.INVISIBLE);

        if (getArguments() != null) {
            mAccessToken = getArguments().getString(MyUserFragment.INSTAGRAM_ACCESS_TOKEN);
        }

        mFollowedSwipeRefreshLayout.setOnRefreshListener(this);

        mFollowedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mFollowedRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFollowedRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        FrameLayout frameLayout = (FrameLayout) root.findViewById(R.id.followed_users_liked_media_frame_layout);
        if (frameLayout != null) {
            isDualPane = true;
        }

        mFollowedRecyclerAdapter = new FollowedUserImageAdapter(getContext(), new ArrayList<ArrayList<Media>>(), mAccessToken, isDualPane, this, true);
        mFollowedRecyclerView.setAdapter(mFollowedRecyclerAdapter);

        retrieveFollowedMedia();

        return root;
    }

    @Override
    public void onRefresh() {
        if (!isSyncingData) {
            isSyncingData = true;
            mFollowedSwipeRefreshLayout.setVisibility(View.INVISIBLE);
            mFollowedProgressBar.setVisibility(View.VISIBLE);
            mFollowedUsersMedia = new ArrayList<>();
            mFollowedRecyclerAdapter.clear();

            retrieveFollowedMedia();
        }

        mFollowedSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(MediaChangedEvent event) {
        updateMediaContent(event.getMedia());
    }

    /**
     * Uses the User Service to retrieve all users and media from the Instagram API.
     */
    //TODO: Fix the body type to return the error type correctly
    private void retrieveFollowedMedia() {
        final UserService service = ServiceFactory.getInstagramUserService();
        service.getFollowedUsers(mAccessToken)
                .flatMap(new Func1<Response<MediaResponse<ArrayList<User>>>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Response<MediaResponse<ArrayList<User>>> response) {
                        return Observable.from(response.body().getData());
                    }
                })
                .flatMap(new Func1<Object, Observable<Response<MediaResponse<ArrayList<Media>>>>>() {
                    @Override
                    public Observable<Response<MediaResponse<ArrayList<Media>>>> call(Object o) {
                        User user = (User) o;
                        return service.getFollowedUsersMedia(user.getId(), mAccessToken);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(20)
                .subscribe(new Subscriber<Response<MediaResponse<ArrayList<Media>>>>() {
                    @Override
                    public void onCompleted() {
                        showFollowedUserMediaList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(FOLLOWED_USER_IMAGE_LIST_FRAGMENT, e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(Response<MediaResponse<ArrayList<Media>>> response) {
                        ResponseBody errorBody = response.errorBody();
                        // Look for error responses from the Instagram API
                        if (!response.isSuccessful() && errorBody != null) {
                            try {
                                Converter<ResponseBody, MediaResponse> errorConverter = ServiceFactory.getRetrofit().responseBodyConverter(MediaResponse.class, new Annotation[0]);
                                MediaResponse mediaError = errorConverter.convert(response.errorBody());
                                Toast.makeText(getContext(), mediaError.getMeta().getErrorMessage(), Toast.LENGTH_LONG).show();

                                if (mediaError.getMeta().getErrorType().equals(getString(R.string.o_auth_error))) {
                                    EventBus.getDefault().post(new ExpiredOAuthEvent(true));
                                }
                            } catch (IOException e) {
                                Log.e(FOLLOWED_USER_IMAGE_LIST_FRAGMENT, "There was a problem parsing the error response.");
                            }
                        } else {
                            //If no errors, post the results.
                            addFollowedUsersMedia(response.body().getData());
                        }
                    }
                });
    }

    /**
     * Adds the users returned media to an ArrayList.
     *
     * @param data The List of returned media.
     */
    private void addFollowedUsersMedia(ArrayList<Media> data) {
        if (data != null && data.size() > 0) {
            mFollowedUsersMedia.add(data);
        }
    }

    /**
     * Adds the media to the Adapter and displays it in the RecyclerView.
     */
    private void showFollowedUserMediaList() {
        if (mFollowedUsersMedia != null && mFollowedUsersMedia.size() > 0) {
            mFollowedRecyclerAdapter.addFollowedUsers(mFollowedUsersMedia);
        }

        mFollowedSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mFollowedProgressBar.setVisibility(View.INVISIBLE);
        isSyncingData = false;
    }

    /**
     * Updates the Media on the adapter.
     *
     * @param media The media to update.
     */
    private void updateMediaContent(Media media) {
        mFollowedRecyclerAdapter.updateMediaContent(media);
    }

    /**
     * Loads the view in the Master/Detail style from the Adapter
     */
    public void showTabletFrameLayout(Media media) {
        ImageDetailFragment imageDetailFragment = ImageDetailFragment.newInstance(media, mAccessToken);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.followed_users_liked_media_frame_layout, imageDetailFragment).commit();
    }
}