package com.example.lenovoppc.calculator;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final static String TAG_CALCULATOR_FRAGMENT = "calc_frag";
    public static final String LOG_TAG = "MAINACTIVITY_TAG";
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private CalculatorFragment calculatorFragment;
    boolean doubleBackToExitPressedOnce = false;



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

        /*Instantiate the calculator fragment*/
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        calculatorFragment = new CalculatorFragment();
        fragmentTransaction.replace(R.id.fl_fragment_container, calculatorFragment,TAG_CALCULATOR_FRAGMENT);
//        fragmentTransaction.addToBackStack(null); //to resume fragment and not destroy
        fragmentTransaction.commit();



        /*Code for bottom navigation*/
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


/*
    Method for double click to exit
*/
 /*   @Override
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
*/
}
