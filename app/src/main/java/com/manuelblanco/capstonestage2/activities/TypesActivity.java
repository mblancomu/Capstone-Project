package com.manuelblanco.capstonestage2.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fenjuly.mylibrary.SpinnerLoader;
import com.manuelblanco.capstonestage2.R;
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
 * Created by manuel on 27/08/16.
 */
public class TypesActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String mSelectedType;
    @Nullable
    @BindView(R.id.view_false_toolbar)
    View mFalseToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                mSelectedType = "";
            } else {
                mSelectedType = extras.getString(Constants.SELECTED_TYPE);
            }
        } else {
            //mFromMenu = (boolean) savedInstanceState.getSerializable(Constants.KEY_LOGIN_FROM_MENU);
        }

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        toolbar.setTitle(mSelectedType);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        launchTypesFragment(mSelectedType);

    }

    private void launchTypesFragment(String selectedType) {

        FragmentLauncher.launchSingleListFragment(this,"",selectedType,Constants.FRAGMENT_TYPES);

        if (Utils.isTablet(this)){
            final List<Trip> trip = SqlHandler.getTripsByType(mSelectedType,"*");
            mFalseToolbar.setVisibility(View.VISIBLE);
            final android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            new Handler().post(new Runnable() {
                public void run() {
                    FragmentLauncher.launchDetailFragment(fm,
                            Validator.isIdTripFromListValid(TypesActivity.this,trip),Constants.FRAGMENT_DETAIL, false);
                }
            });
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

}