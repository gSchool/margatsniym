package com.houkcorp.margatsniym.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.houkcorp.margatsniym.R;
import com.houkcorp.margatsniym.utils.InstagramWebViewClient;

public class InstagramLoginDialog extends DialogFragment {

    private static final String CLIENT_ID = "85326b79ccca4d1f8e155765dcb8a7b7";
    private static final String REDIRECT_URI = "https://houkappdevelopment.wordpress.com/";

    public static InstagramLoginDialog newInstance(){
        return new InstagramLoginDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_instagram_login, container, false);

        WebView webView = (WebView) root.findViewById(R.id.login_dialog_web_view);
        WebViewClient client = new WebViewClient();
        webView.setWebViewClient(new InstagramWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://api.instagram.com/oauth/authorize/?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&response_type=token");

        return root;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}