package com.houkcorp.margatsniym.utils;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.houkcorp.margatsniym.dialogs.InstagramLoginDialog;
import com.houkcorp.margatsniym.events.LoginEvent;

import org.greenrobot.eventbus.EventBus;

public class InstagramWebViewClient extends WebViewClient {
    private static final String ACCESS_TOKEN = "access_token=";

    private InstagramLoginDialog mInstagramLoginDialog;

    public InstagramWebViewClient(InstagramLoginDialog instagramLoginDialog) {
        super();
        mInstagramLoginDialog = instagramLoginDialog;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.contains(ACCESS_TOKEN)) {
            String[] splitString = url.split(ACCESS_TOKEN);
            mInstagramLoginDialog.setAccessToken(splitString[1]);
            System.out.println("This is where we are atish: " + splitString[1]);
            EventBus.getDefault().post(new LoginEvent(splitString[1]));
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (request.getUrl().toString().contains(ACCESS_TOKEN)) {
            System.out.println("This is where we are at: " + request.getUrl().toString());
            String[] splitString = request.getUrl().toString().split(ACCESS_TOKEN);
            mInstagramLoginDialog.setAccessToken(splitString[1]);
            EventBus.getDefault().post(new LoginEvent(splitString[1]));
        }

        return super.shouldOverrideUrlLoading(view, request);
    }
}