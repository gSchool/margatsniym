package com.houkcorp.margatsniym.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;

import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.dialogs.InstagramLoginDialog;
import com.houkcorp.margatsniym.events.ExpiredOAuthEvent;
import com.houkcorp.margatsniym.events.LoginEvent;
import com.houkcorp.margatsniym.fragments.FollowedUserImageList;
import com.houkcorp.margatsniym.fragments.MyUserFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NavigationBarActivity extends AppCompatActivity {
    private static final String SELECTED_MENU_ITEM = "SELECTED_MENU_ITEM_ID";
    private static final String MY_USER_FRAGMENT = "MY_USER_FRAGMENT";
    private static final String FOLLOWING_FRAGMENT = "FOLLOWING_FRAGMENT";
    private static final String MARGATSNIYM_PREF_NAME = "MargatsniymPrefs";

    private int mSelectedMenuItemId;

    @BindView(R.id.bottom_nav_bar) BottomNavigationView mNavigationView;
    private InstagramLoginDialog mDialogFragment;
    private String mAccessKey;
    private MyUserFragment mMyUserFragment;
    private FollowedUserImageList mFollowedUserImageList;
    private boolean mAuthRunning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_bar);

        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getSharedPreferences(MARGATSNIYM_PREF_NAME, 0);
        mAccessKey = sharedPreferences.getString(MARGATSNIYM_PREF_NAME, "");

        mNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_bar);
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectMenuItem(item);

                return false;
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("loginFrag");
        if (currentFragment != null) {
            fragmentTransaction.remove(currentFragment);
        }

        if (TextUtils.isEmpty(mAccessKey)) {
            mAuthRunning = true;
            mDialogFragment = InstagramLoginDialog.newInstance();
            mDialogFragment.show(fragmentTransaction, "loginFrag");
        }

        if (savedInstanceState != null) {
            selectMenuItem(mNavigationView.getMenu().findItem(savedInstanceState.getInt(SELECTED_MENU_ITEM, 0)));
        } else {
            selectMenuItem(mNavigationView.getMenu().getItem(0));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_MENU_ITEM, mSelectedMenuItemId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void selectMenuItem(MenuItem menuItem) {
        Fragment fragment = null;
        String fragmentTag = null;


        switch (menuItem.getItemId()) {
            case R.id.my_user:
                fragmentTag = MY_USER_FRAGMENT;

                if (mMyUserFragment == null) {
                    mMyUserFragment = MyUserFragment.newInstance(mAccessKey);
                }

                fragment = mMyUserFragment;
                mMyUserFragment.setAccessKey(mAccessKey);
                setActivityTitle(getString(R.string.my_user));

                break;

            case R.id.following:
                fragment = getSupportFragmentManager().findFragmentByTag(FOLLOWING_FRAGMENT);
                fragmentTag = FOLLOWING_FRAGMENT;

                if (fragment == null) {
                    mFollowedUserImageList = FollowedUserImageList.newInstance();
                }

                fragment = mFollowedUserImageList;
                setActivityTitle(getString(R.string.following));

                break;
        }

        mSelectedMenuItemId = menuItem.getItemId();

        menuItem.setChecked(true);

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.navigation_bar_frame_layout, fragment, fragmentTag);
            ft.commit();
        }
    }

    @Subscribe
    public void onEvent(LoginEvent event){
        mDialogFragment.dismiss();
        mAccessKey = event.getAccessToken();
        mMyUserFragment.setAccessKey(mAccessKey);

        mAuthRunning = false;

        SharedPreferences sharedPreferences = getSharedPreferences(MARGATSNIYM_PREF_NAME, 0);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(MARGATSNIYM_PREF_NAME, mAccessKey);
        prefsEditor.apply();
    }

    @Subscribe
    public void onEvent(ExpiredOAuthEvent event) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (mDialogFragment == null) {
            mDialogFragment = InstagramLoginDialog.newInstance();
        }

        if (!mAuthRunning){
            fragmentTransaction.remove(mDialogFragment);
            mAuthRunning = true;
            mDialogFragment.show(fragmentTransaction, "loginFrag");
        }
    }

    private void setActivityTitle(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }
}