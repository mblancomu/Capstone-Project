package com.manuelblanco.capstonestage2.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.fenjuly.mylibrary.SpinnerLoader;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.base.BaseActivity;
import com.manuelblanco.capstonestage2.fragments.FollowUsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FollowUsActivity extends BaseActivity {

    @BindView(R.id.back_progress)
    View mView;
    @BindView(R.id.progressbar)
    SpinnerLoader mProgress;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_follow);
        ButterKnife.bind(this);

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        launchFollowFragment();

    }

    private void launchFollowFragment(){

        FollowUsFragment fr = FollowUsFragment.newInstance();

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.container_main, fr);
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

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}
