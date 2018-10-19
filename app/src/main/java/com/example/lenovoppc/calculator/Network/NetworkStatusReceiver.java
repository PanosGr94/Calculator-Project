package com.example.lenovoppc.calculator.Network;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovoppc.calculator.BuildConfig;
import com.example.lenovoppc.calculator.MainActivity;
import com.example.lenovoppc.calculator.Model.Exchange;
import com.example.lenovoppc.calculator.Model.ExchangeViewModel;
import com.example.lenovoppc.calculator.Model.SharedViewModel;

/**
 * BroadcastReceiver checks for network changes and calls the API EndPoint when online.
 */
public class NetworkStatusReceiver extends BroadcastReceiver {

    private TextView mErrorMessage;
    Activity mActivity;
    ProgressBar mProgressBar = null;
    private static final String API_ACCESS_KEY = BuildConfig.API_ACCESS_KEY; //add api access skey here or at gradle.properties
    private static final String PARAM_SYMBOLS = "USD,MXN,JPY,GBP,AUD";
    private static boolean appOnline = true; //updated var for calling by Fragments
    private static Exchange exchange = null;
    private ExchangeViewModel exchangeViewModel;

    public static boolean isDataDownloaded() {
        return exchange != null;
    }


    public static boolean isAppOnline() {
        return appOnline;
    }

    public static void setAppOnline(boolean appOnline) {
        NetworkStatusReceiver.appOnline = appOnline;
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

        exchangeViewModel = ViewModelProviders.of((MainActivity) mActivity).get(ExchangeViewModel.class);

        if(isConnected && isOK) {
            if (wifi.isAvailable() || mobile.isAvailable()) {
                Log.d("Network_Avail", "App is online");
                setAppOnline(true);
                if(mErrorMessage!=null){
                    mErrorMessage.setVisibility(View.GONE);
                }
                //Caching results to avoid any extra calls while app running.
                if (isDataDownloaded()) {
                    Log.d("Network_Avail", "data has been used from cache");
                    exchangeViewModel.updateRates(exchange);
                } else {
                    executeCall();
                }

            }
        } else {
            Log.d("Network_Avail", "App is offline");
            setAppOnline(false);

        }

    }

    /**
     * Method uses Retrofit Library to make REST call to API endpoint
     */
    void executeCall() {

        if(mProgressBar != null) {
            //START LOADING BAR HERE.
            mProgressBar.setVisibility(View.VISIBLE);
        }

        NetworkService.getInstance()
                .getJSONApi()
                .getExchanges(API_ACCESS_KEY,PARAM_SYMBOLS)
                .enqueue(new retrofit2.Callback<Exchange>() {
                    @Override
                    public void onResponse(retrofit2.Call<Exchange> call, retrofit2.Response<Exchange> response) {
                        exchange = response.body();
                        if(mProgressBar != null) {
                            //END LOADING BAR HERE
                            mProgressBar.setVisibility(View.GONE);
                        }
                        exchangeViewModel.updateRates(exchange);

                        Toast.makeText(mActivity, "Exchange rates updated.", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(retrofit2.Call<Exchange> call, Throwable t) {

                        Log.e("Retrofit","Error occurred while getting request!");
                        t.printStackTrace();
                    }
                });


    }

}
