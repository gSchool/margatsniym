package com.houkcorp.margatsniym.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.events.ExpiredOAuthEvent;
import com.houkcorp.margatsniym.events.LoginEvent;
import com.houkcorp.margatsniym.models.InstagramMedia;
import com.houkcorp.margatsniym.models.InstagramResponse;
import com.houkcorp.margatsniym.models.InstagramUser;
import com.houkcorp.margatsniym.services.InstagramUserService;
import com.houkcorp.margatsniym.services.ServiceFactory;
import com.houkcorp.margatsniym.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyUserFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String INSTAGRAM_ACCESS_KEY = "INSTAGRAM_ACCESS_KEY";

    public static MyUserFragment newInstance(String accessToken) {
        MyUserFragment myUserFragment = new MyUserFragment();
        Bundle args = new Bundle();
        args.putString(INSTAGRAM_ACCESS_KEY, accessToken);

        return myUserFragment;
    }

    private String mAccessKey;

    @BindView(R.id.my_user_swipe_refresh_layout) SwipeRefreshLayout mUserSwipeRefreshLayout;
    @BindView(R.id.my_user_image_view) ImageView mUserImageView;
    @BindView(R.id.my_user_name_text_view) TextView mNameTextView;
    @BindView(R.id.my_user_website_text_view) TextView mWebsiteTextView;
    @BindView(R.id.my_user_bio_linear_layout) LinearLayout mBioLinearLayout;
    @BindView(R.id.my_user_bio_card_view) CardView mBioCardView;
    @BindView(R.id.my_user_bio_text_view) TextView mBioTextView;
    @BindView(R.id.my_user_counts_linear_layout) LinearLayout mCountsLinearLayout;
    @BindView(R.id.my_user_counts_card_view) CardView mCountsCardView;
    @BindView(R.id.my_user_media_count_text_view) TextView mMediaCountTextView;
    @BindView(R.id.my_user_follows_text_view) TextView mFollowsTextView;
    @BindView(R.id.my_user_followed_by_text_view) TextView mFollowedByTextView;
    @BindView(R.id.my_user_recent_media_linear_layout) LinearLayout mRecentMediaLinearLayout;
    @BindView(R.id.my_user_liked_media_linear_layout) LinearLayout mLikedMediaLinearLayout;

    private static final int MAX_LIST_COUNT = 20;
    private boolean isSyncingData = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_user, container, false);

        ButterKnife.bind(this, root);

        if (getArguments() != null) {
            mAccessKey = getArguments().getString(INSTAGRAM_ACCESS_KEY);
        }

        mUserSwipeRefreshLayout.setOnRefreshListener(this);

        if (!TextUtils.isEmpty(mAccessKey)) {
            if (NetworkUtils.isOnline(getContext())) {
                retrieveBasicUserInfo();
            } else {
                Toast.makeText(getContext(), R.string.not_online, Toast.LENGTH_LONG).show();
            }
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void retrieveBasicUserInfo() {
        final InstagramUserService service = ServiceFactory.getInstagramUserService();
        service.getUser(mAccessKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<InstagramResponse<InstagramUser>>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("This completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("There was an error: ");
                        System.out.println("There was an error: " + e.toString());
                    }

                    @Override
                    public void onNext(Response<InstagramResponse<InstagramUser>> response) {
                        ResponseBody errorBody = response.errorBody();
                        if (!response.isSuccessful() && errorBody != null) {
                            try {
                                Gson gson = new GsonBuilder().create();
                                InstagramResponse instagramErrorResponse = gson.fromJson(errorBody.string(), InstagramResponse.class);
                                Toast.makeText(getContext(), instagramErrorResponse.getMeta().getErrorMessage(), Toast.LENGTH_LONG).show();

                                if (instagramErrorResponse.getMeta().getErrorType().equals("OAuthAccessTokenException")) {
                                    EventBus.getDefault().post(new ExpiredOAuthEvent(true));
                                }
                            } catch (IOException e) {
                                Log.e("MyUserFragment", "There was a problem parsing the error response.");
                            }
                        } else {
                            showUserInfo(response.body().getData());
                        }
                    }
                });
    }

    private void showUserInfo(InstagramUser user) {
        //TODO: Need to look at and fix the centerCrop.
        Picasso
                .with(getContext())
                .load(user.getProfilePicture())
                .resize(75, 100)
                .centerCrop()
                .into(mUserImageView);

        mNameTextView.setText(user.getFullName());
        mWebsiteTextView.setText(user.getWebsite());

        if (!TextUtils.isEmpty(user.getBio())) {
            mBioTextView.setText(user.getBio());
            mBioTextView.setMovementMethod(new ScrollingMovementMethod());
            mBioLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mBioLinearLayout.setVisibility(View.GONE);
        }

        if (user.getCounts().getMedia() == 0 && user.getCounts().getFollows() == 0 && user.getCounts().getFollowedBy() == 0) {
            mCountsLinearLayout.setVisibility(View.GONE);
        } else {
            String mediaCount = "\t" + "\t" + "\t" + String.valueOf(user.getCounts().getMedia());
            mMediaCountTextView.setText(mediaCount);

            String followsCount = "\t" + "\t" + "\t" + String.valueOf(user.getCounts().getFollows());
            mFollowsTextView.setText(followsCount);

            String followedByCount = "\t" + "\t" + "\t" + String.valueOf(user.getCounts().getFollowedBy());
            mFollowedByTextView.setText(followedByCount);

            mCountsLinearLayout.setVisibility(View.VISIBLE);
        }

        retrieveUsersRecentMedia();
    }

    private void retrieveUsersRecentMedia() {
        InstagramUserService service = ServiceFactory.getInstagramUserService();
        service.getUsersRecentMedia(mAccessKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<InstagramResponse<ArrayList<InstagramMedia>>>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("This completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("There was an error: ");
                        System.out.println("There was an error: " + e.toString());
                    }

                    @Override
                    public void onNext(Response<InstagramResponse<ArrayList<InstagramMedia>>> response) {
                        ResponseBody errorBody = response.errorBody();
                        if (!response.isSuccessful() && errorBody != null) {
                            try {
                                Gson gson = new GsonBuilder().create();
                                InstagramResponse instagramErrorResponse = gson.fromJson(errorBody.string(), InstagramResponse.class);
                                Toast.makeText(getContext(), instagramErrorResponse.getMeta().getErrorMessage(), Toast.LENGTH_LONG).show();

                                if (instagramErrorResponse.getMeta().getErrorType().equals("OAuthAccessTokenException")) {
                                    EventBus.getDefault().post(new ExpiredOAuthEvent(true));
                                }
                            } catch (IOException e) {
                                Log.e("MyUserFragment", "There was a problem parsing the error response.");
                            }
                        } else {
                            showUsersRecentMedia(response.body().getData());
                        }
                    }
                });
    }

    private void showUsersRecentMedia(ArrayList<InstagramMedia> media) {
        if (media != null && media.size() > 0) {
            int maxCount = media.size() < 20 ? media.size() : MAX_LIST_COUNT;
            List<InstagramMedia> mediaSubLists = media.subList(0, maxCount);
            ImagesGridViewFragment gridViewFragment = ImagesGridViewFragment.newInstance(new ArrayList<InstagramMedia>(mediaSubLists));
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.my_user_recent_media_frame_layout, gridViewFragment).commit();
            mRecentMediaLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mRecentMediaLinearLayout.setVisibility(View.GONE);
        }

        retrieveUsersLikedMedia();
    }

    private void retrieveUsersLikedMedia() {
        InstagramUserService service = ServiceFactory.getInstagramUserService();
        service.getUsersLikedMedia(mAccessKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<InstagramResponse<ArrayList<InstagramMedia>>>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("This completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("There was an error: ");
                        System.out.println("There was an error: " + e.toString());
                    }

                    @Override
                    public void onNext(Response<InstagramResponse<ArrayList<InstagramMedia>>> response) {
                        ResponseBody errorBody = response.errorBody();
                        if (!response.isSuccessful() && errorBody != null) {
                            try {
                                Gson gson = new GsonBuilder().create();
                                InstagramResponse instagramErrorResponse = gson.fromJson(errorBody.string(), InstagramResponse.class);
                                Toast.makeText(getContext(), instagramErrorResponse.getMeta().getErrorMessage(), Toast.LENGTH_LONG).show();

                                if (instagramErrorResponse.getMeta().getErrorType().equals("OAuthAccessTokenException")) {
                                    EventBus.getDefault().post(new ExpiredOAuthEvent(true));
                                }
                            } catch (IOException e) {
                                Log.e("MyUserFragment", "There was a problem parsing the error response.");
                            }
                        } else {
                            showUsersLikedMedia(response.body().getData());
                        }
                    }
                });
    }

    private void showUsersLikedMedia(ArrayList<InstagramMedia> media) {
        if (media != null && media.size() > 0) {
            int maxCount = media.size() < 20 ? media.size() : MAX_LIST_COUNT;
            List<InstagramMedia> mediaSubLists = media.subList(0, maxCount);
            ImagesGridViewFragment gridViewFragment = ImagesGridViewFragment.newInstance(new ArrayList<InstagramMedia>(mediaSubLists));
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.my_user_liked_media_frame_layout, gridViewFragment).commit();
            mLikedMediaLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mLikedMediaLinearLayout.setVisibility(View.GONE);
        }

        isSyncingData = false;
        mUserSwipeRefreshLayout.setRefreshing(false);
    }

    public void setAccessKey(String accessKey) {
        mAccessKey = accessKey;
        retrieveBasicUserInfo();
    }

    @Override
    public void onRefresh() {
        if (!isSyncingData) {
            isSyncingData = true;
            mUserImageView.setImageResource(0);
            mNameTextView.setText("");
            mWebsiteTextView.setText("");
            mBioLinearLayout.setVisibility(View.GONE);
            mBioTextView.setText("");
            mCountsLinearLayout.setVisibility(View.GONE);
            mMediaCountTextView.setText("");
            mFollowsTextView.setText("");
            mFollowedByTextView.setText("");
            mRecentMediaLinearLayout.setVisibility(View.GONE);
            mLikedMediaLinearLayout.setVisibility(View.GONE);

            if (NetworkUtils.isOnline(getContext())) {
                retrieveBasicUserInfo();
            } else {
                Toast.makeText(getContext(), R.string.not_online, Toast.LENGTH_LONG).show();
            }
        }
    }
}