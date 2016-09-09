package com.manuelblanco.capstonestage2.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.base.BaseActivity;
import com.manuelblanco.capstonestage2.fragments.DetailFragment;
import com.manuelblanco.capstonestage2.listeners.VotesListener;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.Validator;

/**
 * Created by manuel on 24/08/16.
 */
public class DetailActivity extends BaseActivity implements VotesListener{

    private String mIdTrip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                mIdTrip = "";
            } else {
                mIdTrip = extras.getString(Constants.KEY_IDTRIP);
            }
        } else {
            //mFromMenu = (boolean) savedInstanceState.getSerializable(Constants.KEY_LOGIN_FROM_MENU);
        }

        launchDetailFragment(Constants.FRAGMENT_DETAIL, Validator.isIdTripValid(this,mIdTrip));

    }

    private void launchDetailFragment(String tag, String idTrip) {

        DetailFragment fr = DetailFragment.newInstance(idTrip);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.container_main, fr, tag);
        fragmentTransaction.commit();

    }

    @Override
    public void onRateChange(String vote, String rate) {
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_DETAIL);
        detailFragment.updateVotes(vote, rate);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
