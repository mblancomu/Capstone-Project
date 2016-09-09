package com.manuelblanco.capstonestage2.views;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.DetailActivity;
import com.manuelblanco.capstonestage2.activities.MapsActivity;
import com.manuelblanco.capstonestage2.adapters.MainListTripsAdapter;
import com.manuelblanco.capstonestage2.base.BaseActivity;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.URLUtils;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by manuel on 6/08/16.
 */
public class CustomMarkerInfo implements GoogleMap.InfoWindowAdapter {

    private Context mContext;
    private HashMap<Marker, Trip> mMarkersHashMap;

    public TextView viewTitle;
    public TextView viewVotes;
    public TextView viewDescription;
    public ImageView viewPhoto;

    public CustomMarkerInfo(Context context, HashMap<Marker, Trip> markerTripHashMap){
        this.mContext = context;
        this.mMarkersHashMap = markerTripHashMap;
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker)
    {
        View v  = ((MapsActivity)mContext).getLayoutInflater().inflate(R.layout.marker_info_window, null);

        Trip trip = mMarkersHashMap.get(marker);

        viewTitle = (TextView) v.findViewById(R.id.title_card);
        viewVotes = (TextView) v.findViewById(R.id.votes);
        viewDescription = (TextView) v.findViewById(R.id.description_card);
        viewPhoto = (ImageView)v.findViewById(R.id.thumbnail_trip);

        viewTitle.setText(trip.getTitle());
        viewVotes.setText(trip.getRate());
        viewDescription.setText(trip.getDescription());

        viewTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, Utils.getFlagIso(mContext,trip.getCountry()), 0);

        try {
            Picasso.with(mContext)
                    .load(URLUtils.getURLImageTest(trip.getPhoto()))
                    .placeholder(R.drawable.trip_placeholder)
                    .into(viewPhoto);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return v;
    }

}
