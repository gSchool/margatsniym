package com.houkcorp.margatsniym.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.activities.ImageDetailActivity;
import com.houkcorp.margatsniym.fragments.MyUserFragment;
import com.houkcorp.margatsniym.models.Media;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends BaseAdapter {
    private ArrayList<Media> mMedia;
    private boolean mIsDualPane;
    private boolean mIsFirstLoad;
    private Context mContext;
    private MyUserFragment mMyUserFragment;
    private String mAccessToken;

    @BindView(R.id.image_image_view)
    ImageView mImageImageView;

    //TODO: remove the access token.  Could remove some other items and have a listener handle all of the work.  Give the adapter less work.  Just context and media.
    // Change to recycler View.  Can Dynamically change the way it looks.  Diff util on recycler View.
    public ImageAdapter(Context context, ArrayList<Media> media, String acessToken, boolean isDualPane, MyUserFragment myUserFragment, boolean isFirstLoad) {
        mMedia = media;
        mContext = context;
        mAccessToken = acessToken;
        mIsDualPane = isDualPane;
        mMyUserFragment = myUserFragment;
        mIsFirstLoad = isFirstLoad;
    }

    @Override
    public int getCount() {
        return mMedia.size();
    }

    @Override
    public Object getItem(int position) {
        return mMedia.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_image_view, parent, false);

        ButterKnife.bind(this, root);

        final Media media = mMedia.get(position);

        Picasso
                .with(mContext)
                .load(media.getImages().getThumbnail().getUrl())
                .into(mImageImageView);

        if (position == 0 && mIsDualPane && mIsFirstLoad) {
            mIsFirstLoad = false;
            mMyUserFragment.showTabletFrameLayout(media);
        }

        // Launches a detail view of the selected image.
        mImageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsDualPane) {
                    mMyUserFragment.showTabletFrameLayout(media);
                } else {
                    Intent intent = ImageDetailActivity.newIntent(mContext, media, mAccessToken);
                    mContext.startActivity(intent);
                }
            }
        });

        return root;
    }

    /**
     * Add or remove liked media based on whether or not the image was liked.
     *
     * @param media The media to add or remove.
     */
    public void addOrRemoveMediaContent(Media media) {
        if (mMedia.contains(media)) {
            mMedia.remove(media);
        } else {
            mMedia.add(0, media);
        }

        notifyDataSetChanged();
    }

    /**
     * Updates the media at a certain position.
     *
     * @param media The media to update.
     */
    public void updateMediaContent(Media media) {
        if (mMedia.contains(media)) {
            int position = mMedia.indexOf(media);
            mMedia.set(position, media);

            notifyDataSetChanged();
        }
    }
}