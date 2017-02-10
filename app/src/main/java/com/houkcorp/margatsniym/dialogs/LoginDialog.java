package com.houkcorp.margatsniym.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.utils.MargatsniYmWebViewClient;
import com.houkcorp.margatsniym.utils.NetworkUtils;

/**
 * Launches a Login Dialog.  Shows the Instagram Login inside of a Web View.
 */
public class LoginDialog extends DialogFragment {

    private static final String CLIENT_ID = "85326b79ccca4d1f8e155765dcb8a7b7";
    private static final String REDIRECT_URI = "https://houkappdevelopment.wordpress.com/";
    private String mAccessToken;

    public static LoginDialog newInstance(){
        return new LoginDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_instagram_login, container, false);
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // If online, launch the Web View to display the login.
        if (NetworkUtils.isOnline(getContext())) {
            WebView webView = (WebView) root.findViewById(R.id.login_dialog_web_view);
            webView.setWebViewClient(new MargatsniYmWebViewClient(this));
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl("https://api.instagram.com/oauth/authorize/?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&response_type=token");
        } else {
            Toast.makeText(getContext(), R.string.not_online, Toast.LENGTH_LONG).show();
        }

        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mAccessToken == null) {
            getActivity().finish();
        }

        super.onDismiss(dialog);
    }

    /**
     * Set the access token
     *
     * @param token The access token being passed.
     */
    public void setAccessToken(String token) {
        mAccessToken = token;
    }
}