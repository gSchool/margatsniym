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
import com.houkcorp.margatsniym.activities.ImageDetailActivity;
import com.houkcorp.margatsniym.fragments.FollowedUserImageListFragment;
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
    private boolean mIsDualPane;
    private boolean mIsFirstLoad;
    private Context mContext;
    private FollowedUserImageListFragment mFollowedUserImageListFragment;
    private String mAccessToken;

    public FollowedUserImageAdapter(Context context, ArrayList<ArrayList<Media>> followedUserMedia, String accessToken, boolean isDualPane, FollowedUserImageListFragment followedUserImageListFragment, boolean isFirstLoad) {
        mContext = context;
        mFollowedUsersMedia = followedUserMedia;
        mAccessToken = accessToken;
        mIsDualPane = isDualPane;
        mFollowedUserImageListFragment = followedUserImageListFragment;
        mIsFirstLoad = isFirstLoad;
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
                .into(holder.mImageView);

        holder.mNameTextView.setText(user.getFullName());

        holder.mUserImagesLinearLayout.removeAllViews();
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

            if (position == 0 && mIsDualPane && mIsFirstLoad) {
                mIsFirstLoad = false;
                mFollowedUserImageListFragment.showTabletFrameLayout(media);
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIsDualPane) {
                        mFollowedUserImageListFragment.showTabletFrameLayout(media);
                    } else {
                        Intent intent = ImageDetailActivity.newIntent(mContext, media, mAccessToken);
                        mContext.startActivity(intent);
                    }
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

    /**
     * Update the media at a certain point.
     * Has to check the inner list and update that.
     *
     * @param media The media to be updated.
     */
    public void updateMediaContent(Media media) {
        for (int i = 0; i < mFollowedUsersMedia.size(); i++) {
            ArrayList<Media> innerMediaList = mFollowedUsersMedia.get(i);
            if (innerMediaList.contains(media)) {
                int position = innerMediaList.indexOf(media);
                innerMediaList.set(position, media);

                notifyItemChanged(i);
            }
        }
    }

    /**
     * Clears the adapter when refreshing the data.
     */
    public void clear() {
        int range = mFollowedUsersMedia.size();
        this.mFollowedUsersMedia.clear();
        this.notifyItemRangeChanged(0, range);
    }
}