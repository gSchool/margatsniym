package com.houkcorp.margatsniym.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.activities.InstagramDetailActivity;
import com.houkcorp.margatsniym.models.Media;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InstagramDetailFragment extends Fragment {
    private Media mMedia;

    @BindView(R.id.image_detail_image_view) ImageView mDetailImageView;

    public static InstagramDetailFragment newInstance(Media media) {
        Bundle extras = new Bundle();
        extras.putParcelable(InstagramDetailActivity.MEDIA_EXTRA, media);

        InstagramDetailFragment instagramDetailFragment = new InstagramDetailFragment();
        instagramDetailFragment.setArguments(extras);

        return instagramDetailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_instagram_detail, container, false);

        ButterKnife.bind(this, root);

        Bundle extras = getArguments();
        if (extras != null) {
            mMedia = extras.getParcelable(InstagramDetailActivity.MEDIA_EXTRA);
        }

        // TODO: Need to set this page up to handle videos as well.
        Picasso
                .with(getContext())
                .load(mMedia.getImages().getStandardResolution().getUrl())
                .into(mDetailImageView);

        return root;
    }
}