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
import com.houkcorp.margatsniym.dialogs.LoginDialog;
import com.houkcorp.margatsniym.events.ExpiredOAuthEvent;
import com.houkcorp.margatsniym.events.LoginEvent;
import com.houkcorp.margatsniym.fragments.FollowedUserImageListFragment;
import com.houkcorp.margatsniym.fragments.MyUserFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The bas activity for Margatsnimy.  This holds the Bottom Navigation Bar and handles the changes
 * in Fragment views.
 */
public class NavigationBarActivity extends AppCompatActivity {
    private static final String SELECTED_MENU_ITEM = "SELECTED_MENU_ITEM_ID";
    private static final String MY_USER_FRAGMENT = "MY_USER_FRAGMENT";
    private static final String FOLLOWING_FRAGMENT = "FOLLOWING_FRAGMENT";
    private static final String MARGATSNIYM_PREF_NAME = "MargatsniymPrefs";

    private int mSelectedMenuItemId;

    @BindView(R.id.bottom_nav_bar) BottomNavigationView mNavigationView;
    private LoginDialog mDialogFragment;
    private String mAccessToken;
    private MyUserFragment mMyUserFragment;
    private FollowedUserImageListFragment mFollowedUserImageListFragment;
    private boolean mAuthRunning = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_bar);

        ButterKnife.bind(this);

        // Retrieve the access code from Shared Prefs.
        SharedPreferences sharedPreferences = getSharedPreferences(MARGATSNIYM_PREF_NAME, 0);
        mAccessToken = sharedPreferences.getString(MARGATSNIYM_PREF_NAME, "");

        // Builds the bottom navigation drawer on the view.
        mNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nav_bar);
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectMenuItem(item);

                return false;
            }
        });

        // If the old login dialog is up, find and kill it.
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("loginFrag");
        if (currentFragment != null) {
            fragmentTransaction.remove(currentFragment);
        }

        // Test the retrieved access key.  If empty launch the login dialog.
        if (TextUtils.isEmpty(mAccessToken)) {
            mAuthRunning = true;
            mDialogFragment = LoginDialog.newInstance();
            mDialogFragment.show(fragmentTransaction, "loginFrag");
        }

        // Launch the menu
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

    /**
     * Catches the login and saves the Access Key to shared Prefs.
     *
     * @param event The event being returned.  Holds the Access Key.
     */
    @Subscribe
    public void onEvent(LoginEvent event){
        // Dismiss the dialog.
        mDialogFragment.dismiss();
        mAccessToken = event.getAccessToken();
        mMyUserFragment.setAccessToken(mAccessToken);

        mAuthRunning = false;

        // Store the access key for later user.  Not the safest way to store, but ok for my purposes.
        SharedPreferences sharedPreferences = getSharedPreferences(MARGATSNIYM_PREF_NAME, 0);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(MARGATSNIYM_PREF_NAME, mAccessToken);
        prefsEditor.apply();
    }

    /**
     * Receives a notice if the access token has expired.
     *
     * @param event Failed Oauth event.
     */
    @Subscribe
    public void onEvent(ExpiredOAuthEvent event) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        // If the Dialog has not been created.  Create one.
        if (mDialogFragment == null) {
            mDialogFragment = LoginDialog.newInstance();
        }

        // If not currently updating token, Launch login dialog.
        if (!mAuthRunning){
            fragmentTransaction.remove(mDialogFragment);
            mAuthRunning = true;
            mDialogFragment.show(fragmentTransaction, "loginFrag");
        }
    }

    /**
     * Assigns the correct Fragment to the view.
     *
     * @param menuItem The selected item.
     */
    public void selectMenuItem(MenuItem menuItem) {
        Fragment fragment = null;
        String fragmentTag = null;


        switch (menuItem.getItemId()) {
            case R.id.my_user:
                fragmentTag = MY_USER_FRAGMENT;

                if (mMyUserFragment == null) {
                    mMyUserFragment = MyUserFragment.newInstance(mAccessToken);
                }

                // If fragment does not exist, create a new one.
                fragment = mMyUserFragment;
                mMyUserFragment.setAccessToken(mAccessToken);
                setActivityTitle(getString(R.string.my_user));

                break;

            case R.id.following:
                fragment = getSupportFragmentManager().findFragmentByTag(FOLLOWING_FRAGMENT);
                fragmentTag = FOLLOWING_FRAGMENT;

                if (fragment == null) {
                    mFollowedUserImageListFragment = FollowedUserImageListFragment.newInstance(mAccessToken);
                }

                fragment = mFollowedUserImageListFragment;
                setActivityTitle(getString(R.string.following));

                break;
        }

        mSelectedMenuItemId = menuItem.getItemId();

        menuItem.setChecked(true);

        // Add the fragment to the view.
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.navigation_bar_frame_layout, fragment, fragmentTag);
            ft.commit();
        }
    }

    /**
     * Sets the Activities title for the selected menu item.
     *
     * @param text The title to be displayed.
     */
    private void setActivityTitle(CharSequence text) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(text);
        }
    }
}