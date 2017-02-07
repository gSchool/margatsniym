package com.houkcorp.margatsniym.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.houkcorp.margatsniym.models.Media;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private ArrayList<Media> mMedia;
    private Context mContext;

    public ImageAdapter(Context context, ArrayList<Media> media) {
        mMedia = media;
        mContext = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, media.getImages().getThumbnail().getUrl(), Toast.LENGTH_SHORT).show();
            }
        });

        return imageView;
    }

    public void clearImages() {
        mMedia = new ArrayList<>();
    }

    public void addImages(ArrayList<Media> media) {
        mMedia.addAll(media);
    }
}