package com.example.lenovoppc.calculator.Online;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.lenovoppc.calculator.BuildConfig;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkStatusReceiver extends BroadcastReceiver {

    Activity mActivity;
    ProgressBar mProgressBar = null;
    SwipeRefreshLayout mRefreshLayout;
    private static final String API_URL = BuildConfig.API_KEY;
    private static boolean appOnline = true; //updated var for calling by Activities

    public static boolean isAppOnline() {
        return appOnline;
    }

    public static void setAppOnline(boolean appOnline) {
        NetworkStatusReceiver.appOnline = appOnline;
    }

    //Constructor call by MainActivity with progressBar instance
    public NetworkStatusReceiver(Activity activity, ProgressBar progressBar) {
        mActivity = activity;
        mProgressBar = progressBar;
    }


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
                setAppOnline(true);
                try {
                    run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("Network_Avail", "App is online");
            }
        } else {
            setAppOnline(false);
            Log.d("Network_Avail", "App is offline");
        }

    }

    void run() throws IOException {

        if(mProgressBar != null) {
            //START LOADING BAR IF IT HAS BEEN PASSED HERE.
            mProgressBar.setVisibility(View.VISIBLE);
        }

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
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
                            mProgressBar.setVisibility(View.INVISIBLE);
                        }else{
                            //ELSE STOP REFRESHING HERE ( in case of {@link WordList} )
                            mRefreshLayout.setRefreshing(false);
                        }
                        //TODO: SEND NETWORK RESPONSE TO ViewModel


                    }
                });

            }
        });
    }
}
