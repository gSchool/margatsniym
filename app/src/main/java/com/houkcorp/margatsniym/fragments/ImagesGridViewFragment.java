package com.houkcorp.margatsniym.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.adapters.ImageAdapter;
import com.houkcorp.margatsniym.models.Media;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A reusable fragment for displaying a list of images.
 */
public class ImagesGridViewFragment extends Fragment {
    public static final String ACCESS_TOKEN_EXTRAS = "ACCESS_KEY_EXTRAS";
    public static final String IS_DUAL_PANE_EXTRAS = "IS_DUAL_PANE_EXTRAS";
    public static final String IS_FIRST_LOAD_EXTRAS = "IS_FIRST_LOAD_EXTRAS";
    public static final String MEDIA_EXTRAS = "MEDIA_EXTRAS";

    private ArrayList<Media> mMedia = new ArrayList<>();
    private boolean mIsDualPane;
    private boolean mIsFirstLoad;
    private ImageAdapter mAdapter;
    private MyUserFragment mMyUserFragment;
    private String mAccessToken;

    @BindView(R.id.images_grid_view) GridView mImagesGridView;

    public static ImagesGridViewFragment newInstance(ArrayList<Media> media, String accessToken, boolean isDualPane, boolean isFirstLoad) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(MEDIA_EXTRAS, media);
        args.putString(ACCESS_TOKEN_EXTRAS, accessToken);
        args.putBoolean(IS_DUAL_PANE_EXTRAS, isDualPane);
        args.putBoolean(IS_FIRST_LOAD_EXTRAS, isFirstLoad);

        ImagesGridViewFragment imagesGridViewFragment = new ImagesGridViewFragment();
        imagesGridViewFragment.setArguments(args);

        return imagesGridViewFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_images_grid_view, container, false);

        ButterKnife.bind(this, root);

        if (getArguments() != null) {
            mMedia = getArguments().getParcelableArrayList(MEDIA_EXTRAS);
            mAccessToken = getArguments().getString(ACCESS_TOKEN_EXTRAS);
            mIsDualPane = getArguments().getBoolean(IS_DUAL_PANE_EXTRAS);
            mIsFirstLoad = getArguments().getBoolean(IS_FIRST_LOAD_EXTRAS);
        }

        //Adding the adapter
        mAdapter = new ImageAdapter(getActivity(), mMedia, mAccessToken, mIsDualPane, mMyUserFragment, mIsFirstLoad);
        mImagesGridView.setAdapter(mAdapter);

        return root;
    }

    /**
     * Calls the adapters addOrRemoveMediaContent method.
     *
     * @param media The media to add or delete
     */
    public void addOrRemoveMediaContent(Media media) {
        mAdapter.addOrRemoveMediaContent(media);
    }

    /**
     * Calls the adapters update method.
     *
     * @param media The Media to update on the adapter.
     */
    public void updateMediaContent(Media media) {
        mAdapter.updateMediaContent(media);
    }

    /**
     * Sets the MyUserFragment for this fragment
     *
     * @param myUserFragment The MyUserFragment
     */
    public void setMyUserFragment(MyUserFragment myUserFragment) {
        mMyUserFragment = myUserFragment;
    }
}