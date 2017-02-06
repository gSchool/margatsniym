package com.houkcorp.margatsniym.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.models.InstagramMedia;
import com.houkcorp.margatsniym.models.InstagramResponse;
import com.houkcorp.margatsniym.models.InstagramUser;
import com.houkcorp.margatsniym.services.InstagramUserService;
import com.houkcorp.margatsniym.services.ServiceFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyUserFragment extends Fragment {
    private static final String INSTAGRAM_ACCESS_KEY = "INSTAGRAM_ACCESS_KEY";

    public static MyUserFragment newInstance(String accessToken) {
        MyUserFragment myUserFragment = new MyUserFragment();
        Bundle args = new Bundle();
        args.putString(INSTAGRAM_ACCESS_KEY, accessToken);
        return myUserFragment;
    }

    private String mAccessKey;

    @BindView(R.id.my_user_image_view) ImageView mUserImageView;
    @BindView(R.id.my_user_name_text_view) TextView mNameTextView;
    @BindView(R.id.my_user_website_text_view) TextView mWebsiteTextView;
    @BindView(R.id.my_user_bio_card_view) CardView mBioCardView;
    @BindView(R.id.my_user_bio_text_view) TextView mBioTextView;
    @BindView(R.id.my_user_counts_card_view) CardView mCountsCardView;
    @BindView(R.id.my_user_media_count_text_view) TextView mMediaCountTextView;
    @BindView(R.id.my_user_follows_text_view) TextView mFollowsTextView;
    @BindView(R.id.my_user_followed_by_text_view) TextView mFollowedByTextView;

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
            retrieveBasicUserInfo();
        }

        return root;
    }

    private void retrieveBasicUserInfo() {
        InstagramUserService service = ServiceFactory.getInstagramUserService();
        service.getUser(mAccessKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InstagramResponse<InstagramUser>>() {
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
                    public void onNext(InstagramResponse<InstagramUser> instagramUserInstagramResponse) {
                        showUserInfo(instagramUserInstagramResponse.getData());
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
        mBioTextView.setText(user.getBio());
        mBioTextView.setMovementMethod(new ScrollingMovementMethod());

        String mediaCount = "\t" + "\t" + "\t" + String.valueOf(user.getCounts().getMedia());
        mMediaCountTextView.setText(mediaCount);

        String followsCount = "\t" + "\t" + "\t" + String.valueOf(user.getCounts().getFollows());
        mFollowsTextView.setText(followsCount);

        String followedByCount = "\t" + "\t" + "\t" + String.valueOf(user.getCounts().getFollowedBy());
        mFollowedByTextView.setText(followedByCount);

        retrieveUsersRecentMedia();
    }

    private void retrieveUsersRecentMedia() {
        InstagramUserService service = ServiceFactory.getInstagramUserService();
        service.getUsersRecentMedia(mAccessKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InstagramResponse<ArrayList<InstagramMedia>>>() {
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
                    public void onNext(InstagramResponse<ArrayList<InstagramMedia>> instagramUserInstagramResponse) {
                        showUsersRecentMedia(instagramUserInstagramResponse.getData());
                    }
                });
    }

    private void showUsersRecentMedia(ArrayList<InstagramMedia> media) {
        ImagesGridViewFragment gridViewFragment = ImagesGridViewFragment.newInstance(media);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.my_user_recent_media_frame_layout, gridViewFragment).commit();
    }

    private void retrieveUsersLikedMedia() {
        InstagramUserService service = ServiceFactory.getInstagramUserService();
        service.getUSersLikedMedia(mAccessKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InstagramResponse<ArrayList<InstagramMedia>>>() {
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
                    public void onNext(InstagramResponse<ArrayList<InstagramMedia>> instagramUserInstagramResponse) {
                        showUsersLikedMedia(instagramUserInstagramResponse.getData());
                    }
                });
    }

    private void showUsersLikedMedia(ArrayList<InstagramMedia> media) {
        ImagesGridViewFragment gridViewFragment = ImagesGridViewFragment.newInstance(media);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.my_user_liked_media_frame_layout, gridViewFragment).commit();
    }

    public void setAccessKey(String accessKey) {
        mAccessKey = accessKey;
        retrieveBasicUserInfo();
    }
}