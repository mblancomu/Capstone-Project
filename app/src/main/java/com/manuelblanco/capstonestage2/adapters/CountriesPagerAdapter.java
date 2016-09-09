package com.manuelblanco.capstonestage2.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.manuelblanco.capstonestage2.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manuel on 3/08/16.
 */
public class CountriesPagerAdapter extends FragmentPagerAdapter {

    private int NUM_ITEMS;
    private Context mContext;
    private ArrayList<String> mContinents;
    private List<Fragment> mFragments;


    public CountriesPagerAdapter(FragmentManager fragmentManager, Context context,
                                 ArrayList<String> continents, List<Fragment> fragments) {
        super(fragmentManager);
        this.mContext = context;
        this.NUM_ITEMS = continents.size();
        this.mFragments = fragments;
        this.mContinents = continents;
    }

    @Override
    public Fragment getItem(int position) {

        return mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContinents.get(position).toString();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //if (position >= getCount()) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) object);
        trans.commit();
        //}
    }
}