package com.houkcorp.margatsniym.fragments;

import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.events.ExpiredOAuthEvent;
import com.houkcorp.margatsniym.models.Media;
import com.houkcorp.margatsniym.models.MediaResponse;
import com.houkcorp.margatsniym.models.User;
import com.houkcorp.margatsniym.services.UserService;
import com.houkcorp.margatsniym.services.ServiceFactory;
import com.houkcorp.margatsniym.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This Fragment displays the basic info for an Instagram User.
 */
public class MyUserFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public static final String INSTAGRAM_ACCESS_KEY = "INSTAGRAM_ACCESS_KEY";

    public static MyUserFragment newInstance(String accessToken) {
        MyUserFragment myUserFragment = new MyUserFragment();
        Bundle args = new Bundle();
        args.putString(INSTAGRAM_ACCESS_KEY, accessToken);

        return myUserFragment;
    }

    private String mAccessKey;

    @BindView(R.id.my_user_scroll_view) ScrollView mScrollView;
    @BindView(R.id.my_user_progress_bar) ProgressBar mProgressBar;

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

        // If Access Key exists and is online get the info and load the view
        if (!TextUtils.isEmpty(mAccessKey)) {
            if (NetworkUtils.isOnline(getContext())) {
                mProgressBar.setVisibility(View.VISIBLE);
                mScrollView.setVisibility(View.INVISIBLE);
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

    /**
     * Fetches the basic info from the Instagram Server
     */
    private void retrieveBasicUserInfo() {
        UserService service = ServiceFactory.getInstagramUserService();
        service.getUser(mAccessKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<MediaResponse<User>>>() {
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
                    public void onNext(Response<MediaResponse<User>> response) {
                        // Looks for instagram error response.
                        ResponseBody errorBody = response.errorBody();
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
                            showUserInfo(response.body().getData());
                        }
                    }
                });
    }

    /**
     * Show the returned user info.
     *
     * @param user THe user returned from the server.
     */
    private void showUserInfo(User user) {
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

    /**
     * Fetch the Users recently posted media, from the server.  Fetch at max 20.
     */
    private void retrieveUsersRecentMedia() {
        UserService service = ServiceFactory.getInstagramUserService();
        service.getUsersRecentMedia(mAccessKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(20)
                .subscribe(new Subscriber<Response<MediaResponse<ArrayList<Media>>>>() {
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
                    public void onNext(Response<MediaResponse<ArrayList<Media>>> response) {
                        ResponseBody errorBody = response.errorBody();
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
                            showUsersRecentMedia(response.body().getData());
                        }
                    }
                });
    }

    /**
     * Display the list of returned recent media.
     *
     * @param media The media returned from the Instagram API's
     */
    private void showUsersRecentMedia(ArrayList<Media> media) {
        if (media != null && media.size() > 0) {
            int maxCount = media.size() < 20 ? media.size() : MAX_LIST_COUNT;
            List<Media> mediaSubLists = media.subList(0, maxCount);
            ImagesGridViewFragment gridViewFragment = ImagesGridViewFragment.newInstance(new ArrayList<Media>(mediaSubLists));
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.my_user_recent_media_frame_layout, gridViewFragment).commit();
            mRecentMediaLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mRecentMediaLinearLayout.setVisibility(View.GONE);
        }

        retrieveUsersLikedMedia();
    }

    /**
     * Fetches all media the user has liked.  Fetches at max 20.
     */
    private void retrieveUsersLikedMedia() {
        UserService service = ServiceFactory.getInstagramUserService();
        service.getUsersLikedMedia(mAccessKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(20)
                .subscribe(new Subscriber<Response<MediaResponse<ArrayList<Media>>>>() {
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
                    public void onNext(Response<MediaResponse<ArrayList<Media>>> response) {
                        ResponseBody errorBody = response.errorBody();
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
                            showUsersLikedMedia(response.body().getData());
                        }
                    }
                });
    }

    /**
     * Displays the liked media.
     *
     * @param media The media returned from Instagram API's
     */
    private void showUsersLikedMedia(ArrayList<Media> media) {
        if (media != null && media.size() > 0) {
            // Just a sanity check for now to make sure only 20 come back.
            int maxCount = media.size() < 20 ? media.size() : MAX_LIST_COUNT;
            List<Media> mediaSubLists = media.subList(0, maxCount);
            ImagesGridViewFragment gridViewFragment = ImagesGridViewFragment.newInstance(new ArrayList<>(mediaSubLists));
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.my_user_liked_media_frame_layout, gridViewFragment).commit();
            mLikedMediaLinearLayout.setVisibility(View.VISIBLE);
        } else {
            mLikedMediaLinearLayout.setVisibility(View.GONE);
        }

        isSyncingData = false;
        mProgressBar.setVisibility(View.INVISIBLE);
        mScrollView.setVisibility(View.VISIBLE);
        mUserSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Sets the Access Key of the fragment
     *
     * @param accessKey The Instagram Access Key
     */
    public void setAccessKey(String accessKey) {
        mAccessKey = accessKey;
        retrieveBasicUserInfo();
    }

    /**
     * This is called when the user pulls down to refresh the page.  This will clear and reacquire
     * all data.
     */
    @Override
    public void onRefresh() {
        if (!isSyncingData) {
            isSyncingData = true;

            mProgressBar.setVisibility(View.VISIBLE);
            mScrollView.setVisibility(View.INVISIBLE);

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