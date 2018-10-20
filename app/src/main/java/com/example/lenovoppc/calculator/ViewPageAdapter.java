package com.example.lenovoppc.calculator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPageAdapter extends FragmentStatePagerAdapter {

    private Fragment[] childFragments;

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
        childFragments = new Fragment[]{
                new CalculatorFragment(),
                new ConverterFragment()
        };
    }

    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    @Override
    public int getCount() {
        return childFragments.length;
    }


}
