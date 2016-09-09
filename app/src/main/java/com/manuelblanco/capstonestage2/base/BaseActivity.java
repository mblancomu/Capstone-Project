package com.manuelblanco.capstonestage2.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.fragments.DetailFragment;
import com.manuelblanco.capstonestage2.listeners.VotesListener;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.manuelblanco.capstonestage2.utils.UtilsView;

/**
 * Created by manuel on 16/07/16.
 */
public class BaseActivity extends AppCompatActivity implements VotesListener{

    private Context context;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main);

        context = getApplicationContext();
        activity = this;

        if(Utils.isTablet(this)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onRateChange(String vote, String rate) {
        DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_DETAIL);
        detailFragment.updateVotes(vote, rate);
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            UtilsView.showSnackBar((CoordinatorLayout) findViewById(R.id.map_coordinator),getString(R.string.request_permission_gps),
                    getResources().getColor(R.color.colorAccent));
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UtilsView.showSnackBar((CoordinatorLayout) findViewById(R.id.map_coordinator),getString(R.string.granted_permission_gps),
                            getResources().getColor(android.R.color.holo_green_dark));
                } else {
                    UtilsView.showSnackBar((CoordinatorLayout) findViewById(R.id.map_coordinator),getString(R.string.granted_permission_gps),
                            getResources().getColor(android.R.color.holo_red_dark));
                }
                break;
        }
    }

}
