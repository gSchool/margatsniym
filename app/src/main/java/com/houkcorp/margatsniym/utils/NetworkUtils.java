package com.houkcorp.margatsniym.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * A basic Network class to do network calls.
 */
public class NetworkUtils {

    /**
     * Checks to see whether or not the device is online.
     *
     * @param context The context from the calling Fragment/Activity
     *
     * @return A boolean value stating whether or not the device is online.
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}