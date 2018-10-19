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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovoppc.calculator.Network.NetworkStatusReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

//TODO: CHECK OUT IF VIEWPAGER IS APPLICABLE
public class MainActivity extends AppCompatActivity {

    public final static String TAG_CALCULATOR_FRAGMENT = "calc_frag";
    public final static String TAG_CONVERTER_FRAGMENT = "conv_frag";
    public static final String LOG_TAG = "MAINACTIVITY_TAG";
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private CalculatorFragment calculatorFragment;
    boolean doubleBackToExitPressedOnce = false;


    private NetworkStatusReceiver mNSR;
    @BindView(R.id.pb_loadingbar)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_error_message)
    TextView mErrorMessage;


    /**
     * Code for handling bottom navigation
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_calculator:

                    if(!NetworkStatusReceiver.isAppOnline()){ notifyOffline(TAG_CALCULATOR_FRAGMENT, mErrorMessage); }

                    /*Replace the calculator fragment with the one previously created*/
                    calculatorFragment = (CalculatorFragment) fragmentManager.findFragmentByTag(TAG_CALCULATOR_FRAGMENT);
                    if(calculatorFragment!=null){
                        fragmentTransaction.replace(R.id.fl_fragment_container,calculatorFragment);
//                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }else{
                        Log.d(LOG_TAG, "Calc frag is null");
                        calculatorFragment = new CalculatorFragment();
                        fragmentTransaction.replace(R.id.fl_fragment_container, calculatorFragment,TAG_CALCULATOR_FRAGMENT);
                        fragmentTransaction.commit();
                    }
                    return true;
                case R.id.navigation_convertor:

                    if(!NetworkStatusReceiver.isAppOnline()){ notifyOffline(TAG_CONVERTER_FRAGMENT, mErrorMessage); }

                    /*Replace the calculator Fragment with the converter Fragment*/
                    ConverterFragment converterFragment = new ConverterFragment();
                    fragmentTransaction.replace(R.id.fl_fragment_container,converterFragment);
//                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        /*Instantiate the calculator fragment*/
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        calculatorFragment = new CalculatorFragment();
        fragmentTransaction.replace(R.id.fl_fragment_container, calculatorFragment,TAG_CALCULATOR_FRAGMENT);
//        fragmentTransaction.addToBackStack(null); //to resume fragment and not destroy
        fragmentTransaction.commit();



        //(1)Broadcast receiver instance
        mNSR = new NetworkStatusReceiver(MainActivity.this,mProgressBar,mErrorMessage);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //(2) Broadcast Receiver Register
        registerReceiver(mNSR, intentFilter);


        /*Code for bottom navigation*/
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //(3) Broadcast Receiver Unregister. Tried in onPause but crashes bc BR not registered anymore
        unregisterReceiver(mNSR);
    }

    /**
     * Helper Method to manage the system message for offline status
     * Static because it's also notified from inside {@link CalculatorFragment} when it is replaced
     * @param fragment which fragment the user is on
     */
    public static void notifyOffline(String fragment, final TextView mErrorMessage) {
        NetworkStatusReceiver.setsCurrentFragment(fragment);
        mErrorMessage.setVisibility(View.VISIBLE);
        //if message is on calculator remove after 2s.
        if(fragment.equals(TAG_CALCULATOR_FRAGMENT)) {
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

/*
    Method for double click to exit
*/

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
//            fragmentManager.popBackStackImmediate();
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
