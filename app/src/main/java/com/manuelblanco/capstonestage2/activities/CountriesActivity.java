package com.manuelblanco.capstonestage2.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.app.AppController;
import com.manuelblanco.capstonestage2.base.BaseActivity;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.fragments.FragmentLauncher;
import com.manuelblanco.capstonestage2.listeners.DetailTabletListener;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.manuelblanco.capstonestage2.utils.Validator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 5/08/16.
 */
public class CountriesActivity extends BaseActivity implements DetailTabletListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.view_false_toolbar)
    View mFalseToolbar;

    private Tracker mTracker;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        ButterKnife.bind(this);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AppController application = (AppController) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        launchCountriesFragment();
    }

    private void launchCountriesFragment() {

        FragmentLauncher.launchCountriesFragment(this, Constants.AdapterTypes.TYPE_COUNTRIES, Constants.FRAGMENT_COUNTRIES);

        if (Utils.isTablet(this)) {
            final List<Trip> trip = SqlHandler.getAllTrips();
            final android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            new Handler().post(new Runnable() {
                public void run() {
                    FragmentLauncher.launchDetailFragment(fm,
                            Validator.isIdTripFromListValid(CountriesActivity.this,trip),
                            Constants.FRAGMENT_DETAIL, true);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_search:

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("Share")
                        .build());
                break;
            case R.id.action_map:
                Intent i = new Intent(this, MapsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String idTrip) {
        final android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentLauncher.launchDetailFragment(fm, idTrip, "", true);
    }
}
