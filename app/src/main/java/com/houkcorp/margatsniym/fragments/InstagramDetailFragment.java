package com.houkcorp.margatsniym.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.houkcorp.margatsniym.activities.InstagramDetailActivity;
import com.houkcorp.margatsniym.models.Media;

public class InstagramDetailFragment extends Fragment {

    public static InstagramDetailFragment newInstance(Media media) {
        Bundle extras = new Bundle();
        extras.putParcelable(InstagramDetailActivity.MEDIA_EXTRA, media);

        InstagramDetailFragment instagramDetailFragment = new InstagramDetailFragment();
        instagramDetailFragment.setArguments(extras);

        return instagramDetailFragment;
    }
}