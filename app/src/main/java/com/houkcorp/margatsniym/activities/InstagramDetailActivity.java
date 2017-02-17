package com.houkcorp.margatsniym.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.fragments.InstagramDetailFragment;
import com.houkcorp.margatsniym.models.Media;

/**
 * The detail view of an Instagram image.
 */
public class InstagramDetailActivity extends AppCompatActivity {
    public static final String ACCESS_TOKEN_EXTRA = "ACCESS_TOKEN_EXTRA";
    public static final String INSTAGRAM_DETAIL_FRAGMENT = "INSTAGRAM_DETAIL_FRAGMENT";
    public static final String MEDIA_EXTRA = "MEDIA_EXTRA";

    private Media mMedia;

    public static Intent newIntent(Context context, Media media, String accessToken) {
        Intent intent = new Intent(context, InstagramDetailActivity.class);
        intent.putExtra(ACCESS_TOKEN_EXTRA, accessToken);
        intent.putExtra(MEDIA_EXTRA, media);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_instagram_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String accessToken = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mMedia = bundle.getParcelable(MEDIA_EXTRA);
            accessToken = bundle.getString(ACCESS_TOKEN_EXTRA);
        }

        InstagramDetailFragment instagramDetailFragment = InstagramDetailFragment.newInstance(mMedia, accessToken);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.detail_view_frame_layout, instagramDetailFragment, INSTAGRAM_DETAIL_FRAGMENT).commit();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Sets the Media of the Detail.
     * B
     * @param media The media to display.
     */
    public void setMedia(Media media) {
        mMedia = media;
    }
}