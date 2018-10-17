package com.example.lenovoppc.calculator;

import android.os.Bundle;
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
                        fragmentTransaction.commit();
                    }else{
                        Log.e(LOG_TAG, "Calc frag is null");
                    }
                    return true;
                case R.id.navigation_convertor:
                    /*Replace the calculator Fragment with the converter Fragment*/
                    ConverterFragment converterFragment = new ConverterFragment();
                    fragmentTransaction.replace(R.id.fl_fragment_container,converterFragment);
                    fragmentTransaction.addToBackStack(null);
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
        fragmentTransaction.add(R.id.fl_fragment_container, calculatorFragment,TAG_CALCULATOR_FRAGMENT);
        fragmentTransaction.addToBackStack(null); //to resume fragment and not destroy
        fragmentTransaction.commit();


        /*Code for bottom navigation*/
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
