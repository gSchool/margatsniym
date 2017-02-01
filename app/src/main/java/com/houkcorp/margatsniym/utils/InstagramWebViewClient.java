package com.houkcorp.margatsniym.utils;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InstagramWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        System.out.println("This is where we are atish: " + url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        System.out.println("This is where we are at: " + request.getUrl().toString());
        return super.shouldOverrideUrlLoading(view, request);
    }
}