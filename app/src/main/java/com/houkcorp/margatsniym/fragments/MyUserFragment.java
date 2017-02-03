package com.houkcorp.margatsniym.fragments;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.models.InstagramResponse;
import com.houkcorp.margatsniym.models.InstagramUser;
import com.houkcorp.margatsniym.services.InstagramUserService;
import com.houkcorp.margatsniym.services.ServiceFactory;
import com.squareup.picasso.Picasso;

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
    @BindView(R.id.my_user_name) TextView mNameTextView;
    @BindView(R.id.my_user_website) TextView mWebsiteTextView;
    @BindView(R.id.my_user_bio) TextView mBioTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_user, container, false);

        ButterKnife.bind(this, root );

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
                        //Do things.
                        showUserInfo(instagramUserInstagramResponse.getData());
                    }
                });
    }

    private void showUserInfo(InstagramUser user) {
        //TODO: Need to look at and fix the centerCrop.
        Picasso
                .with(getContext())
                .load(user.getProfilePicture())
                .centerCrop()
                .into(mUserImageView);

        mNameTextView.setText(user.getFullName());
        mWebsiteTextView.setText(user.getWebsite());
        mBioTextView.setText(user.getBio());
    }

    public void setAccessKey(String accessKey) {
        mAccessKey = accessKey;
        retrieveBasicUserInfo();
    }
}