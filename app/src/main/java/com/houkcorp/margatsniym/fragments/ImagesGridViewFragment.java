package com.houkcorp.margatsniym.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.adapters.ImageAdapter;
import com.houkcorp.margatsniym.models.InstagramMedia;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagesGridViewFragment extends Fragment {
    public static final String MEDIA_EXTRAS = "MEDIA_EXTRAS";

    private ImageAdapter mAdapter;
    private ArrayList<InstagramMedia> mMedia = new ArrayList<>();

    @BindView(R.id.images_grid_view) GridView mImagesGridView;

    public static ImagesGridViewFragment newInstance(ArrayList<InstagramMedia> media) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(MEDIA_EXTRAS, media);

        ImagesGridViewFragment imagesGridViewFragment = new ImagesGridViewFragment();
        imagesGridViewFragment.setArguments(args);

        return imagesGridViewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_images_grid_view, container, false);

        ButterKnife.bind(this, root);

        if (getArguments() != null) {
            mMedia = getArguments().getParcelableArrayList(MEDIA_EXTRAS);
        }

        mAdapter = new ImageAdapter(getActivity(), mMedia);
        mImagesGridView.setAdapter(mAdapter);

        return root;
    }
}