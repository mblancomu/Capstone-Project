package com.manuelblanco.capstonestage2.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.fenjuly.mylibrary.SpinnerLoader;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.app.AppController;
import com.manuelblanco.capstonestage2.base.BaseActivity;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.fragments.DetailFragment;
import com.manuelblanco.capstonestage2.fragments.FragmentLauncher;
import com.manuelblanco.capstonestage2.fragments.MainSingleListTrips;
import com.manuelblanco.capstonestage2.listeners.VotesListener;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.manuelblanco.capstonestage2.utils.Validator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 8/08/16.
 */
public class FavoritesActivity extends BaseActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container_main)
    FrameLayout mContainer;
    @Nullable
    @BindView(R.id.view_false_toolbar)
    View mFalseToolbar;
    private Tracker mTracker;

    private Fragment detailFragment;
    android.support.v4.app.FragmentManager fm;

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

        launchFavoritesFragment();

    }

    private void launchFavoritesFragment() {

        if (Utils.isTablet(this)) {
            final List<Trip> trip = SqlHandler.getAllFavorites();
            mFalseToolbar.setVisibility(View.VISIBLE);
            FragmentLauncher.launchSingleListFragment(this, "", "", Constants.FRAGMENT_FAVORITES);
            fm = getSupportFragmentManager();
            FragmentLauncher.launchDetailFragment(fm,
                    Validator.isIdTripFromListValid(FavoritesActivity.this, trip), Constants.FRAGMENT_DETAIL, false);

        }else{
            FragmentLauncher.launchSingleListFragment(this, "", "", Constants.FRAGMENT_FAVORITES);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTracker.setScreenName(getString(R.string.add_screen));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
