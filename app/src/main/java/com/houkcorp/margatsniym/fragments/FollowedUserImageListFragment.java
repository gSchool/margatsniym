package com.houkcorp.margatsniym.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.adapters.FollowedUserImageAdapter;
import com.houkcorp.margatsniym.events.ExpiredOAuthEvent;
import com.houkcorp.margatsniym.models.Media;
import com.houkcorp.margatsniym.models.MediaResponse;
import com.houkcorp.margatsniym.models.User;
import com.houkcorp.margatsniym.services.ServiceFactory;
import com.houkcorp.margatsniym.services.UserService;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A Recycler view of max 20 users and max 5 images that are followed by the logged in user.
 */
public class FollowedUserImageListFragment extends Fragment {
    ArrayList<ArrayList<Media>> mFollowedUsersMedia = new ArrayList<>();

    @BindView(R.id.followed_users_recycler_view) RecyclerView mFollowedRecyclerView;
    @BindView(R.id.followed_users_progress_bar) ProgressBar mFollowedProgressBar;
    private FollowedUserImageAdapter mFollowedRecyclerAdapter;

    /**
     * Retrieve a new instance of FollowedUserImageListFragment
     *
     * @param accessKey The access key of the logged in user.
     * @return Returns a new instance of FollowedUserImageListFragment
     */
    public static FollowedUserImageListFragment newInstance(String accessKey) {
        Bundle extras = new Bundle();
        extras.putString(MyUserFragment.INSTAGRAM_ACCESS_KEY, accessKey);

        FollowedUserImageListFragment followedUserImageListFragment = new FollowedUserImageListFragment();
        followedUserImageListFragment.setArguments(extras);

        return followedUserImageListFragment;
    }

    private String mAccessKey;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_followed_user_image_list, container, false);

        ButterKnife.bind(this, root);

        // Hide the recycler view and show the progress bar.
        mFollowedProgressBar.setVisibility(View.VISIBLE);
        mFollowedRecyclerView.setVisibility(View.GONE);

        if (getArguments() != null) {
            mAccessKey = getArguments().getString(MyUserFragment.INSTAGRAM_ACCESS_KEY);
        }

        mFollowedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mFollowedRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mFollowedRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mFollowedRecyclerAdapter = new FollowedUserImageAdapter(getContext(), new ArrayList<ArrayList<Media>>());
        mFollowedRecyclerView.setAdapter(mFollowedRecyclerAdapter);

        retrieveFollowedMedia();

        return root;
    }

    /**
     * Uses the User Service to retrieve all users and media from the Instagram API.
     */
    private void retrieveFollowedMedia() {
        final UserService service = ServiceFactory.getInstagramUserService();
        service.getFollowedUsers(mAccessKey)
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
                        return service.getFollowedUsersMedia(user.getId(), mAccessKey);
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
                        System.out.println();
                    }

                    @Override
                    public void onNext(Response<MediaResponse<ArrayList<Media>>> response) {
                        ResponseBody errorBody = response.errorBody();
                        // Look for error responses from the Instagram API
                        if (!response.isSuccessful() && errorBody != null) {
                            try {
                                Gson gson = new GsonBuilder().create();
                                MediaResponse instagramErrorResponse = gson.fromJson(errorBody.string(), MediaResponse.class);
                                Toast.makeText(getContext(), instagramErrorResponse.getMeta().getErrorMessage(), Toast.LENGTH_LONG).show();

                                if (instagramErrorResponse.getMeta().getErrorType().equals("OAuthAccessTokenException")) {
                                    EventBus.getDefault().post(new ExpiredOAuthEvent(true));
                                }
                            } catch (IOException e) {
                                Log.e("MyUserFragment", "There was a problem parsing the error response.");
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

        mFollowedRecyclerView.setVisibility(View.VISIBLE);
        mFollowedProgressBar.setVisibility(View.GONE);
    }
}