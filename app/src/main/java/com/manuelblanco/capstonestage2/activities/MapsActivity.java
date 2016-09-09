package com.manuelblanco.capstonestage2.activities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.Manifest;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.app.AppController;
import com.manuelblanco.capstonestage2.base.BaseActivity;
import com.manuelblanco.capstonestage2.dialogs.MapAroundMeDialog;
import com.manuelblanco.capstonestage2.dialogs.TypesDialog;
import com.manuelblanco.capstonestage2.listeners.DialogMapListener;
import com.manuelblanco.capstonestage2.listeners.DialogTypesListener;
import com.manuelblanco.capstonestage2.model.AroundMe;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.presenter.MapPresenter;
import com.manuelblanco.capstonestage2.services.GPSTracker;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.UtilsView;
import com.manuelblanco.capstonestage2.utils.Validator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback, DialogMapListener,
        DialogTypesListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private GoogleMap mMap;
    private MapPresenter presenter;
    private String TAG = MapsActivity.class.getSimpleName();
    private Tracker mTracker;
    private Trip mTrip;
    private Context context;
    private Activity activity;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private View view;
    private GPSTracker mGPSTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);

        context = getApplicationContext();
        activity = this;

        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        toolbar.setTitle(getResources().getString(R.string.map));

        Bundle b = this.getIntent().getExtras();
        if (b != null)
            mTrip = (Trip) b.getSerializable(Constants.KEY_COORDS);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AppController application = (AppController) getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mGPSTracker = new GPSTracker(this);

        presenter = new MapPresenter(this, mMap, mGPSTracker);

        if (!checkPermission()) {
            requestPermission();
        }else {

            if (mTrip != null) {
                presenter.centerTripOnMarker(mMap, mTrip);
            } else {
                presenter.initMap(mMap, getResources().getInteger(R.integer.initial_zoom), false);
                presenter.getAllTripsMarkers();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_filter:
                TypesDialog dialog = TypesDialog.newInstance(Validator.isCountryValid(this,presenter.getCurrentCountry()));
                dialog.show(getSupportFragmentManager(), Constants.DIALOG_TYPES);
                break;
            case R.id.action_around:
                MapAroundMeDialog aroundMeDialog = MapAroundMeDialog.newInstance();
                aroundMeDialog.show(getSupportFragmentManager(), Constants.DIALOG_AROUNDME);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTripSelected(Trip trip) {

        if (mMap != null) {
            presenter.centerTripOnMarker(mMap, trip);
        }
    }

    @Override
    public void onAroundMeSelected(AroundMe aroundMe) {
        if (mMap != null) {
            presenter.centerTripOnMarker(mMap, aroundMe.getTrip());
        }
    }

    @Override
    public void onTypesSelected(ArrayList<String> types, String area) {
        presenter.getMarkerByType(types, area);
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocationManager locationManager = mGPSTracker.getLocationManager();
        if (locationManager != null && checkPermission()) {
            locationManager.removeUpdates(mGPSTracker);
        }

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

    @Override
    protected void onResume() {
        super.onResume();

        mTracker.setScreenName(getString(R.string.map_screen));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
