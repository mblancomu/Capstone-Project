package com.manuelblanco.capstonestage2.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.backendless.Backendless;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.utils.TypefaceUtil;

/**
 * Created by manuel on 17/07/16.
 */
public class AppController extends MultiDexApplication {

    private static SqlHandler mSqlHandler;

    private static AppController mInstance;
    private Tracker mTracker;
    private boolean isLogin;
    private String mLogState;

    public String getmUserActive() {
        return mUserActive;
    }

    public void setmUserActive(String mUserActive) {
        this.mUserActive = mUserActive;
    }

    private String mUserActive;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public String getmLogState() {
        return mLogState;
    }

    public void setmLogState(String mLogState) {
        this.mLogState = mLogState;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        mSqlHandler = new SqlHandler(getApplicationContext());
        TypefaceUtil.overrideFont(getApplicationContext(), "NORMAL", "fonts/Roboto-Regular.ttf");

        Backendless.initApp(this,getResources().getString(R.string.app_id_backendless),
                getResources().getString(R.string.secret_key_backendless), getResources().getString(R.string.version_app));
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }
    public static synchronized SqlHandler getmSqlHandler(){
        return mSqlHandler;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    public synchronized Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            //AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.app_tracker);
        }
        return mTracker;
    }

}

