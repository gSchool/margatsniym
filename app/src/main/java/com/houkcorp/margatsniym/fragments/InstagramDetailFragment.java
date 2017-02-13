package com.houkcorp.margatsniym.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.VideoView;

import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.activities.InstagramDetailActivity;
import com.houkcorp.margatsniym.models.Media;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InstagramDetailFragment extends Fragment {
    private Media mMedia;

    @BindView(R.id.image_detail_scroll_view) ScrollView mDetailScrollView;
    @BindView(R.id.image_detail_image_view) ImageView mDetailImageView;
    @BindView(R.id.image_detail_video_view) VideoView mDetailVideoView;
    @BindView(R.id.image_detail_progress_bar) ProgressBar mDetailProgressBar;

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

        if (!TextUtils.isEmpty(mMedia.getCaption().getText())) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mMedia.getCaption().getText());

        }

        if (mMedia.getType().equals("image")) {
            Picasso
                    .with(getContext())
                    .load(mMedia.getImages().getStandardResolution().getUrl())
                    .into(mDetailImageView);
            mDetailImageView.setVisibility(View.VISIBLE);
            mDetailVideoView.setVisibility(View.GONE);
        } else if (mMedia.getType().equals("video")) {
            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(mDetailVideoView);
            mDetailVideoView.setMediaController(mediaController);
            mDetailVideoView.setVideoURI(Uri.parse(mMedia.getVideos().getLowResolution().getUrl()));
            mDetailVideoView.start();

            mDetailImageView.setVisibility(View.GONE);
            mDetailVideoView.setVisibility(View.VISIBLE);
        }

        mDetailProgressBar.setVisibility(View.GONE);
        mDetailScrollView.setVisibility(View.VISIBLE);

        return root;
    }
}