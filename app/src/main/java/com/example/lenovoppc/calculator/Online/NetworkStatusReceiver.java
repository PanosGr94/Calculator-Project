package com.example.lenovoppc.calculator.Online;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovoppc.calculator.BuildConfig;
import com.example.lenovoppc.calculator.MainActivity;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * BroadcastReceiver checks for network changes and calls the API EndPoint when online.
 */
public class NetworkStatusReceiver extends BroadcastReceiver {

    private TextView mErrorMessage;
    Activity mActivity;
    ProgressBar mProgressBar = null;
    private static final String API_URL = BuildConfig.API_KEY;
    private static String sCurrentFragment = MainActivity.TAG_CALCULATOR_FRAGMENT;
    private static boolean appOnline = true; //updated var for calling by Fragments

    public static boolean isAppOnline() {
        return appOnline;
    }

    public static void setAppOnline(boolean appOnline) {
        NetworkStatusReceiver.appOnline = appOnline;
    }

    public static void setsCurrentFragment(String sCurrentFragment) {
        NetworkStatusReceiver.sCurrentFragment = sCurrentFragment;
    }

    //Constructor call by MainActivity with progressBar and errormessage instance
    public NetworkStatusReceiver(Activity activity, ProgressBar progressBar, TextView errorMessage) {
        mActivity = activity;
        mProgressBar = progressBar;
        mErrorMessage = errorMessage;
    }

//    Here is what happens when network changes
    @Override
    public void onReceive(Context context, Intent intent) {

        //Make sure the action is proper
        boolean isOK = intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION);

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected && isOK) {
            if (wifi.isAvailable() || mobile.isAvailable()) {
                Log.d("Network_Avail", "App is online");
                setAppOnline(true);
                if(mErrorMessage!=null){
                    mErrorMessage.setVisibility(View.GONE);
                }
                try {
                    run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("Network_Avail", "App is offline");
            setAppOnline(false);

        }

    }

    void run() throws IOException {

        if(mProgressBar != null) {
            //START LOADING BAR HERE.
            mProgressBar.setVisibility(View.VISIBLE);
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("test_tag",e.getMessage());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                final String myResponse = response.body().string();

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(mProgressBar != null) {
                            //END LOADING BAR HERE
                            mProgressBar.setVisibility(View.GONE);
                        }
                        //TODO: SEND NETWORK RESPONSE TO ViewModel
                        Log.d("test_tag","response returned");
                        Toast.makeText(mActivity, "Exchange rates updated.", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });
    }
}
