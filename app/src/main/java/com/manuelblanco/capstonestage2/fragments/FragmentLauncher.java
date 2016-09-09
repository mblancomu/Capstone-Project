package com.manuelblanco.capstonestage2.fragments;

import android.app.FragmentManager;
import android.content.Context;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.base.BaseActivity;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.Utils;

/**
 * Created by manuel on 2/09/16.
 */
public class FragmentLauncher {

    public static void launchCountriesFragment(Context context, Constants.AdapterTypes type, String tag) {

        CountriesFragment fr = CountriesFragment.newInstance(type);

        android.support.v4.app.FragmentManager fm = ((BaseActivity) context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up,R.anim.slide_down);
        fragmentTransaction.add(R.id.container_main, fr, tag);
        fragmentTransaction.commit();

    }

    public static void launchMainFragment(Context context, Constants.AdapterTypes type, String tag, boolean isReplace) {

        MainFragment fr = MainFragment.newInstance(type);

        android.support.v4.app.FragmentManager fm = ((BaseActivity) context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.push_left_in,R.anim.push_right_out);

        if (isReplace) {
            fragmentTransaction.replace(R.id.container_main, fr, tag);
        } else {
            fragmentTransaction.add(R.id.container_main, fr, tag);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    public static void launchDetailFragment(android.support.v4.app.FragmentManager fm, String idTrip, String tag, boolean isReplace) {

        DetailFragment fr = DetailFragment.newInstance(idTrip);

        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_bottom);

        if (isReplace) {
            fragmentTransaction.replace(R.id.container_detail, fr, tag);
        } else {
            fragmentTransaction.add(R.id.container_detail, fr, tag);
        }
        fragmentTransaction.commitAllowingStateLoss();


    }

    public static void launchSingleListFragment(Context context, String country, String type, String tag) {
        MainSingleListTrips fr = null;
        if (tag.equals(Constants.FRAGMENT_COUNTRIES) || tag.equals(Constants.FRAGMENT_TYPES)) {
            fr = MainSingleListTrips.newInstance(country, tag, type);
        } else if (tag.equals(Constants.FRAGMENT_FAVORITES)) {
            fr = MainSingleListTrips.newInstance(tag);
        }

        android.support.v4.app.FragmentManager fm = ((BaseActivity) context).getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_bottom);
        fragmentTransaction.add(R.id.container_main, fr);
        fragmentTransaction.commit();
    }

}
