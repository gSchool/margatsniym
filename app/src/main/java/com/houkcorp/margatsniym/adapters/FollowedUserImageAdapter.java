package com.houkcorp.margatsniym.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.activities.InstagramDetailActivity;
import com.houkcorp.margatsniym.models.Media;
import com.houkcorp.margatsniym.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The Followed users Recycler View Adapter.  THis will hold the individual users that the logged
 * in user is following.
 */
public class FollowedUserImageAdapter extends RecyclerView.Adapter<FollowedUserImageAdapter.ViewHolder> {
    private static final int MAX_USER_IMAGES_SIZE = 5;

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

        // Adds at max 5 images that the followed users have pushed up.
        int maxCount = mediaList.size() < 5 ? mediaList.size() : MAX_USER_IMAGES_SIZE;
        for (int i = 0; i < maxCount; i++) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 5, 5, 5);
            imageView.setLayoutParams(layoutParams);
            final Media media = mediaList.get(i);
            Picasso
                    .with(mContext)
                    .load(media.getImages().getThumbnail().getUrl())
                    .resize(200, 200)
                    .centerCrop()
                    .into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = InstagramDetailActivity.newIntent(mContext, media);
                    mContext.startActivity(intent);
                }
            });

            holder.mUserImagesLinearLayout.addView(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mFollowedUsersMedia.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.followed_user_image_view) ImageView mImageView;
        @BindView(R.id.followed_user_name_text_view) TextView mNameTextView;
        @BindView(R.id.followed_user_linear_layout) LinearLayout mUserImagesLinearLayout;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * Used for adding followed users to the ArrayList.  Clears before adding.
     *
     * @param followedUserMedia They ArrayList of ArrayLists for media.
     */
    public void addFollowedUsers(ArrayList<ArrayList<Media>> followedUserMedia) {
        mFollowedUsersMedia.clear();
        mFollowedUsersMedia.addAll(followedUserMedia);
        notifyDataSetChanged();
    }
}