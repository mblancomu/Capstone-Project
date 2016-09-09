package com.manuelblanco.capstonestage2.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.fragments.InfoFragment;
import com.manuelblanco.capstonestage2.fragments.PageListTripsFragment;
import com.manuelblanco.capstonestage2.fragments.PageListTypesFragment;
import com.manuelblanco.capstonestage2.fragments.RecommendsFragment;
import com.manuelblanco.capstonestage2.fragments.SmartFragmentStatePagerAdapter;
import com.manuelblanco.capstonestage2.fragments.VotesFragment;
import com.manuelblanco.capstonestage2.model.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manuel on 25/08/16.
 */
public class DetailPagerAdapter  extends SmartFragmentStatePagerAdapter {

    private int NUM_ITEMS;
    private Context mContext;
    private Trip mTrip;


    public DetailPagerAdapter(FragmentManager fragmentManager, Context context, int numberTabs, Trip trip) {
        super(fragmentManager);
        this.mContext = context;
        this.NUM_ITEMS = numberTabs;
        this.mTrip = trip;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return InfoFragment.newInstance(mTrip.getDescription());
            case 1:
                return RecommendsFragment.newInstance(mTrip.getId_trip(),position);
            case 2:
                return RecommendsFragment.newInstance(mTrip.getId_trip(),position);
            case 3:
                return VotesFragment.newInstance(mTrip.getId_trip());

        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return mContext.getResources().getString(R.string.tab_info);
            case 1:
                return mContext.getResources().getString(R.string.tab_slepp);
            case 2:
                return mContext.getResources().getString(R.string.tab_eat);
            case 3:
                return mContext.getResources().getString(R.string.tab_vote);
            default:
                return mContext.getResources().getString(R.string.tab_info);
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
