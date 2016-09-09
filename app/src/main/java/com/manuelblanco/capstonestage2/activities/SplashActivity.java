package com.manuelblanco.capstonestage2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.app.AppController;
import com.manuelblanco.capstonestage2.base.BaseActivity;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.dialogs.InfoDialog;
import com.manuelblanco.capstonestage2.listeners.LoadDataListener;
import com.manuelblanco.capstonestage2.network.BackendlessHandler;
import com.manuelblanco.capstonestage2.utils.Constants;

/**
 * Created by manuel on 6/09/16.
 */
public class SplashActivity extends BaseActivity implements LoadDataListener {

    private AppController appController;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AppController application = (AppController) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        appController = ((AppController)getApplicationContext());

        BackendlessHandler backendlessHandler = new BackendlessHandler(this, this);
        backendlessHandler.initLoadData(false);

        boolean isLogin = backendlessHandler.verifyIsLogged();

        if (isLogin){
            appController.setLogin(true);
        }else {
            appController.setLogin(false);
        }

    }

    @Override
    public void onSuccessLoadData() {

        if (verifyIfDBisPopulated()){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            InfoDialog dialog = InfoDialog.newInstance(getString(R.string.dialog_info_splash));
            dialog.show(getSupportFragmentManager(), Constants.DIALOG_SPLASH);
        }

    }

    private boolean verifyIfDBisPopulated(){
        boolean isPopulated = false;
        int trips = SqlHandler.getCountTrips();

        isPopulated = trips > 0 ? true : false;

        return isPopulated;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTracker.setScreenName(getString(R.string.add_screen));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
