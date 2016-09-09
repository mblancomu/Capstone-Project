package com.manuelblanco.capstonestage2.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fenjuly.mylibrary.SpinnerLoader;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.app.AppController;
import com.manuelblanco.capstonestage2.base.BaseActivity;
import com.manuelblanco.capstonestage2.fragments.LogInFragment;
import com.manuelblanco.capstonestage2.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 9/08/16.
 */
public class LogInActivity extends BaseActivity {

    private boolean mFromMenu;

    @BindView(R.id.back_progress)
    View mBackground;
    @BindView(R.id.progressbar)
    SpinnerLoader mProgress;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_login);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mFromMenu = false;
            } else {
                mFromMenu = extras.getBoolean(Constants.KEY_LOGIN_FROM_MENU);
            }
        } else {
            //mFromMenu = (boolean) savedInstanceState.getSerializable(Constants.KEY_LOGIN_FROM_MENU);
        }

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

        mBackground.setBackgroundColor(getResources().getColor(R.color.back_progress_login));

        launchLoginFragment(mFromMenu);

    }

    private void launchLoginFragment(boolean from) {

        LogInFragment fr = LogInFragment.newInstance(from);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.container_main, fr, Constants.FRAGMENT_LOGIN);
        fragmentTransaction.commit();

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

        mTracker.setScreenName(getString(R.string.favorites_screen));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
