package com.example.vidhiraj.sample;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by lenovo on 12/09/2016.
 */
public class Utils {

    public static boolean isConnected(Context context)
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return  networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
