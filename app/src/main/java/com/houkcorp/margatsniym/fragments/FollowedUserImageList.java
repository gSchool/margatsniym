package com.houkcorp.margatsniym.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.houkcorp.margatsniym.R;

public class FollowedUserImageList extends Fragment {

    public static FollowedUserImageList newInstance() {
        return new FollowedUserImageList();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_followed_user_images, container, false);

        return root;
    }
}