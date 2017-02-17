package com.houkcorp.margatsniym.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.houkcorp.margatsniym.activities.ImageDetailActivity;
import com.houkcorp.margatsniym.fragments.MyUserFragment;
import com.houkcorp.margatsniym.models.Media;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private ArrayList<Media> mMedia;
    private boolean mIsDualPane;
    private Context mContext;
    private MyUserFragment mMyUserFragment;
    private String mAccessToken;

    public ImageAdapter(Context context, ArrayList<Media> media, String acessToken, boolean isDualPane, MyUserFragment myUserFragment) {
        mMedia = media;
        mContext = context;
        mAccessToken = acessToken;
        mIsDualPane = isDualPane;
        mMyUserFragment = myUserFragment;
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
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        final Media media = mMedia.get(position);

        Picasso
                .with(mContext)
                .load(media.getImages().getThumbnail().getUrl())
                .resize(200, 200)
                .centerCrop()
                .into(imageView);

        // Launches a detail view of the selected image.
        imageView.setOnClickListener(new View.OnClickListener() {
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

        return imageView;
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