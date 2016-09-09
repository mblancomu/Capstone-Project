package com.manuelblanco.capstonestage2.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.design.widget.CoordinatorLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.SphericalUtil;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.AddTripActivity;
import com.manuelblanco.capstonestage2.activities.DetailActivity;
import com.manuelblanco.capstonestage2.activities.MapsActivity;
import com.manuelblanco.capstonestage2.base.BaseActivity;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.dialogs.MapInfoDialog;
import com.manuelblanco.capstonestage2.model.AroundMe;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.model.TripsTypesItem;
import com.manuelblanco.capstonestage2.services.GPSTracker;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.StringsUtils;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.manuelblanco.capstonestage2.utils.UtilsView;
import com.manuelblanco.capstonestage2.views.CustomMarkerInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by manuel on 28/07/16.
 */
public class MapPresenter {

    private Context mContext;
    private GoogleMap mMap;
    private HashMap<Marker, Trip> mMarkersHashMap;
    private String mCurrentCountry;
    private GPSTracker mGPSTracker;

    public MapPresenter(Context context, GoogleMap map, GPSTracker tracker) {
        this.mContext = context;
        this.mMap = map;
        this.mGPSTracker = tracker;
    }

    public void getAllTripsMarkers() {

        List<Trip> listOfTrips = SqlHandler.getAllTrips();
        mMarkersHashMap = new HashMap<Marker, Trip>();

        for (Trip trip : listOfTrips) {

            LatLng position = getPositionFronDB(trip.getCoords());
            Bitmap markerDraw = BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.marker_trip);

            Marker markerTrip = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .icon(BitmapDescriptorFactory.fromBitmap(markerDraw))
                    //BitmapDescriptorFactory.fromResource(R.drawable.arrow)
                    .title(trip.getTitle())
                    .snippet(trip.getDescription()));

            mMarkersHashMap.put(markerTrip, trip);
            mMap.setInfoWindowAdapter(new CustomMarkerInfo(mContext, mMarkersHashMap));
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {

                    if (!Utils.isTablet(mContext)) {
                        final Trip tripMarker = mMarkersHashMap.get(marker);
                        final Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra(Constants.KEY_IDTRIP, tripMarker.getId_trip());
                        mContext.startActivity(intent);
                    }
                    //ActivityTransitionLauncher.with((BaseActivity)mContext).from(viewPhoto).launch(intent);
                }
            });
        }
    }

    public void getMarkerByType(ArrayList<String> type, String area) {

        mMap.clear();

        List<TripsTypesItem> tripsTypesItems = SqlHandler.getItemsForType(Utils.typesForSqlite(type));

        List<Trip> listOfTrips = SqlHandler.getTripsByTypeForMarkers(tripsTypesItems, area);


        HashMap<Marker, Trip> mMarkersHashMap = new HashMap<Marker, Trip>();

        for (Trip trip : listOfTrips) {

            LatLng position = getPositionFronDB(trip.getCoords());

            if (position == null){
                position = getPositionFronDB(mContext.getString(R.string.location_default));
            }

            createSingleMarker(position, trip, mMarkersHashMap);
        }

        mMap.animateCamera(CameraUpdateFactory.zoomTo(mContext.getResources().getInteger(R.integer.initial_zoom)));

    }

    private BitmapDescriptor getIconByType(String type) {

        Bitmap markerDraw = BitmapFactory.decodeResource(mContext.getResources(),
                Utils.getMarkerType(mContext, type));

        if (markerDraw == null){
            markerDraw = BitmapFactory.decodeResource(mContext.getResources(),
                    R.drawable.marker_trip);
        }

        return BitmapDescriptorFactory.fromBitmap(markerDraw);
    }

    public LatLng getPositionFronDB(String position) {

        String[] latlong = position.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);

        LatLng latLng = new LatLng(latitude, longitude);

        return latLng;
    }

    public void initMap(GoogleMap map, int zoom, boolean isdDialogPosition) {

        map.setMyLocationEnabled(true);
        LatLng latLng;

        if (mGPSTracker.canGetLocation() && ((BaseActivity) mContext).checkPermission()) {

            double latitude = mGPSTracker.getLatitude();
            double longitude = mGPSTracker.getLongitude();

            if (!isdDialogPosition) {
                getInfoLocationUser(latitude, longitude, new LatLng(latitude, longitude));
            }

            latLng = new LatLng(latitude, longitude);


        } else {

            if (mContext instanceof MapsActivity) {
                UtilsView.showSnackBar((CoordinatorLayout) ((BaseActivity) mContext).findViewById(R.id.map_coordinator),
                        mContext.getString(R.string.no_current_location), mContext.getResources().getColor(android.R.color.holo_red_dark));
            }else if (mContext instanceof AddTripActivity){
                UtilsView.showSnackBar((CoordinatorLayout) ((BaseActivity) mContext).findViewById(R.id.add_coordinator),
                        mContext.getString(R.string.no_current_location), mContext.getResources().getColor(android.R.color.holo_red_dark));
            }

            latLng = getPositionFronDB(mContext.getString(R.string.location_default));
        }

        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(zoom));

    }

    public String getCurrentCountry() {
        return mCurrentCountry;
    }

    public LatLng getLatLngFromLocation(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    private void getInfoLocationUser(double lat, double lng, LatLng currentPos) {

        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            mCurrentCountry = addresses.get(0).getCountryCode().toLowerCase();
            MapInfoDialog dialog = MapInfoDialog.newInstance(addresses.get(0).getCountryCode().toLowerCase(), currentPos);
            dialog.show(((BaseActivity) mContext).getSupportFragmentManager(), Constants.DIALOG_MAP);
        } else {
            mCurrentCountry = mContext.getString(R.string.country_default);
        }

    }

    public void centerTripOnMarker(GoogleMap map, Trip trip) {

        mMap.clear();
        HashMap<Marker, Trip> mMarkersHashMap = new HashMap<Marker, Trip>();
        LatLng position = getPositionFronDB(trip.getCoords());

        if (position == null){
            position = getPositionFronDB(mContext.getString(R.string.location_default));
        }

        createSingleMarker(position, trip, mMarkersHashMap);

        map.setBuildingsEnabled(true);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(mContext.getResources().getInteger(R.integer.detail_zoom))
                .bearing(0)
                .tilt(30)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    public void createSingleMarker(LatLng position, Trip trip, HashMap<Marker, Trip> mMarkersHashMap){

        List<String> types = StringsUtils.convertStringToArrayList(trip.getType());

        for (int i = 0; i < types.size(); i++) {

            Marker markerTrip = mMap.addMarker(new MarkerOptions()
                    .position(position)
                    .icon(getIconByType(types.get(i).toString()))
                    .title(trip.getTitle())
                    .rotation(i * 30f)
                    .snippet(trip.getDescription()));

            mMarkersHashMap.put(markerTrip, trip);
            mMap.setInfoWindowAdapter(new CustomMarkerInfo(mContext, mMarkersHashMap));
        }

    }

    public LatLng getCurrentLocation() {

        if (mGPSTracker.canGetLocation() && ((MapsActivity) mContext).checkPermission()) {

            double latitude = mGPSTracker.getLatitude();
            double longitude = mGPSTracker.getLongitude();


            return new LatLng(latitude, longitude);
        } else {
            if (mContext instanceof MapsActivity) {
                UtilsView.showSnackBar((CoordinatorLayout) ((BaseActivity) mContext).findViewById(R.id.map_coordinator),
                        mContext.getString(R.string.no_current_location), mContext.getResources().getColor(android.R.color.holo_red_dark));
            }else if (mContext instanceof AddTripActivity){
                UtilsView.showSnackBar((CoordinatorLayout) ((BaseActivity) mContext).findViewById(R.id.add_coordinator),
                        mContext.getString(R.string.no_current_location), mContext.getResources().getColor(android.R.color.holo_red_dark));
            }
        }

        return getPositionFronDB(mContext.getString(R.string.location_default));
    }

    public ArrayList<AroundMe> getTripsAroundMe(int ratio) {

        List<Trip> listOfTrips = SqlHandler.getAllTrips();
        ArrayList<AroundMe> aroundMeArrayList = new ArrayList<>();

        for (Trip trip : listOfTrips) {

            LatLng position = getPositionFronDB(trip.getCoords());
            double distance;

            if (getCurrentLocation() != null) {
                distance = SphericalUtil.computeDistanceBetween(getCurrentLocation(), position);
            } else {
                distance = Double.parseDouble(mContext.getString(R.string.distance_default));
            }

            if (distance < ratio) {

                AroundMe aroundMe = new AroundMe();
                aroundMe.setDistance(distance);
                aroundMe.setTrip(trip);

                aroundMeArrayList.add(aroundMe);

            }

        }

        Collections.sort(aroundMeArrayList);

        return aroundMeArrayList;
    }
}
