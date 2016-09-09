package com.manuelblanco.capstonestage2.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.fragments.PageCountriesFragment;
import com.manuelblanco.capstonestage2.fragments.PageListTripsFragment;
import com.manuelblanco.capstonestage2.fragments.PageListTypesFragment;
import com.manuelblanco.capstonestage2.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manuel on 17/07/16.
 */
public class MainTripsPagerAdapter extends FragmentPagerAdapter {

    private int NUM_ITEMS;
    private Context mContext;
    private ArrayList<String> mContinents;
    private List<android.support.v4.app.Fragment> mFragments;


    public MainTripsPagerAdapter(FragmentManager fragmentManager, Context context, int numberTabs) {
        super(fragmentManager);
        this.mContext = context;
        this.NUM_ITEMS = numberTabs;
    }

    @Override
    public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return PageListTripsFragment.newInstance(position);
                case 1:
                    return PageListTripsFragment.newInstance(position);
                case 2:
                    return PageListTypesFragment.newInstance(position);
            }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return mContext.getResources().getString(R.string.tab_lastest);
                case 1:
                    return mContext.getResources().getString(R.string.tab_rating);
                default:
                    return mContext.getResources().getString(R.string.tab_types);
            }
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