package com.houkcorp.margatsniym.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.models.Media;
import com.houkcorp.margatsniym.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowedUserImageAdapter extends RecyclerView.Adapter<FollowedUserImageAdapter.ViewHolder> {
    private ArrayList<ArrayList<Media>> mFollowedUsersMedia = new ArrayList<>();
    private Context mContext;

    public FollowedUserImageAdapter(Context context, ArrayList<ArrayList<Media>> followedUserMedia) {
        mContext = context;
        mFollowedUsersMedia = followedUserMedia;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_followed_user, parent, false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ArrayList<Media> mediaList = mFollowedUsersMedia.get(position);
        User user = mediaList.get(0).getUser();

        Picasso
                .with(mContext)
                .load(user.getProfilePicture())
                .resize(200, 200)
                .centerCrop()
                .into(holder.mImageView);

        holder.mNameTextView.setText(user.getFullName());

        ImageView imageView = new ImageView(mContext);
        Media media = mediaList.get(0);
        Picasso
                .with(mContext)
                .load(media.getImages().getThumbnail().getUrl())
                .resize(200, 200)
                .centerCrop()
                .into(imageView);
        holder.mHorizontalScrollView.addView(imageView);
    }

    @Override
    public int getItemCount() {
        return mFollowedUsersMedia.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.followed_user_image_view) ImageView mImageView;
        @BindView(R.id.followed_user_name_text_view) TextView mNameTextView;
        @BindView(R.id.followed_user_horizontal_scroll_view) HorizontalScrollView mHorizontalScrollView;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public void addFollowedUsers(ArrayList<ArrayList<Media>> followedUserMedia) {
        mFollowedUsersMedia.clear();
        mFollowedUsersMedia.addAll(followedUserMedia);
        notifyDataSetChanged();
    }
}