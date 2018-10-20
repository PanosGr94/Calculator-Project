package com.example.lenovoppc.calculator;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovoppc.calculator.Network.NetworkStatusReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public final static String TAG_CALCULATOR_FRAGMENT = "calc_frag";
    public final static String TAG_CONVERTER_FRAGMENT = "conv_frag";
    private FragmentManager fragmentManager;
    boolean doubleBackToExitPressedOnce = false;


    private NetworkStatusReceiver mNSR;
    @BindView(R.id.pb_loadingbar)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_error_message)
    TextView mErrorMessage;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;


    /**
     * Code for handling bottom navigation
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_calculator:
                    //if app is offline show error message
                    if (!NetworkStatusReceiver.isAppOnline()) {
                        notifyOffline(TAG_CALCULATOR_FRAGMENT,
                                mErrorMessage, getResources().getString(R.string.offline_error_message));
                    }
                    //when tab is clicked switch Fragment through ViewPager
                    mViewPager.setCurrentItem(0, true);
                    return true;
                case R.id.navigation_convertor:

                    if (!NetworkStatusReceiver.isAppOnline()) {
                        notifyOffline(TAG_CONVERTER_FRAGMENT,
                                mErrorMessage, getResources().getString(R.string.offline_error_message));
                    }
                    mViewPager.setCurrentItem(1, true);
                    return false;
            }
            return false;
        }
    };

    /**
     * Helper Method to manage the system message for offline status
     *
     * @param fragment tag of which fragment the user is on
     */
    public static void notifyOffline(String fragment, final TextView mErrorMessage, String error_text) {
        mErrorMessage.setText(error_text);
        mErrorMessage.setVisibility(View.VISIBLE);
        //if message is on calculator remove after 2s.
        if (fragment.equals(TAG_CALCULATOR_FRAGMENT)) {
            CountDownTimer countDownTimerStatic = new CountDownTimer(2000, 16) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    mErrorMessage.setVisibility(View.GONE);
                }
            };
            countDownTimerStatic.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /*Code for bottom navigation*/
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Instantiate the viewpager holding our Fragments
        fragmentManager = getSupportFragmentManager();

        mViewPager.setAdapter(new ViewPageAdapter(fragmentManager));
        //listener updates bottom navigation tab
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float posOffset, int posOffsetPixels) {
                navigation.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageSelected(int pos) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        /*Initiating BroadcastReceiver to make calls when the app is online and show errors when
        it is offline */
        //(1)Broadcast receiver instance
        mNSR = new NetworkStatusReceiver(MainActivity.this,mProgressBar,mErrorMessage);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //(2) Broadcast Receiver Register
        registerReceiver(mNSR, intentFilter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //(3) Broadcast Receiver Unregister. Tried in onPause but crashes,BR not registered anymore
        unregisterReceiver(mNSR);
    }

    /*
        Method for double click to exit
    */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
