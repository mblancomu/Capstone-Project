package com.manuelblanco.capstonestage2.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.listeners.DialogPositionListener;
import com.manuelblanco.capstonestage2.presenter.MapPresenter;
import com.manuelblanco.capstonestage2.services.GPSTracker;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.PermissionsHelper;

/**
 * Created by manuel on 20/08/16.
 */
public class PositionDialog extends DialogFragment implements GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback {

    public String TAG = PositionDialog.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private DialogPositionListener mListener;
    private String mPosition;
    private MapPresenter presenter;
    private String mTitle;
    private String mOrigin;
    private View v;

    public static PositionDialog newInstance(String title, String origin) {
        PositionDialog fragment = new PositionDialog();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_TITLE, title);
        args.putString(Constants.KEY_ORIGIN_POSITION, origin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(getActivity(), this)
                .build();

        mTitle = getArguments().getString(Constants.KEY_TITLE);
        mOrigin = getArguments().getString(Constants.KEY_ORIGIN_POSITION);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogPositionListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setNegativeButton(getActivity().getResources().getString(R.string.button_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dismiss();
                            }
                        }
                )
                .setPositiveButton(getActivity().getResources().getString(R.string.btn_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (mOrigin.equals(Constants.FROM_ADD_TRIP)) {
                                    mListener.onPositionSelected(mPosition != null ? mPosition : "");
                                } else if (mOrigin.equals(Constants.FROM_RECOMMENDS)) {
                                    mListener.onPositionRecommends(mPosition != null ? mPosition : "");
                                }

                                dismiss();
                            }
                        }
                );

        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null)
                parent.removeView(v);
        }
        try {
            LayoutInflater i = getActivity().getLayoutInflater();
            v = i.inflate(R.layout.dialog_position, null);
        } catch (InflateException e) {
        }

        //View v = i.inflate(R.layout.dialog_position, null);

        TextView textDialog = (TextView) v.findViewById(R.id.text_info_dialog);
        textDialog.setText(getString(R.string.select_position));

        final SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getActivity().getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        getChildFragmentManager().beginTransaction().add(R.id.place_autocomplete_fragment, autocompleteFragment).commit();

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {

                mPosition = place.getLatLng().toString();

                if (mMap != null) {
                    mMap.clear();

                    Marker markerTrip = mMap.addMarker(new MarkerOptions()
                            .position(place.getLatLng())
                            .icon(BitmapDescriptorFactory.defaultMarker())
                            .title((String) place.getName())
                            .snippet((String) place.getAddress()));

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                }
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getActivity(), getString(R.string.warning_error), Toast.LENGTH_LONG).show();
            }
        });

        autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input)).setText("");
                mMap.clear();
                view.setVisibility(View.GONE);
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.map_dialog);

        getChildFragmentManager().beginTransaction().add(R.id.map_dialog, mapFragment).commit();

        mapFragment.getMapAsync(this);

        b.setView(v);
        return b.create();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        presenter = new MapPresenter(getActivity(), mMap, new GPSTracker(getActivity()));
        presenter.initMap(mMap, 12, true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(mTitle != null ? mTitle : latLng.latitude + " : " + latLng.longitude);
                mPosition = latLng.toString();

                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addMarker(markerOptions);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    public void onDestroyView() {
        super.onDestroyView();
        Fragment fragmentAutocomple = (getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment));
        Fragment fragmentMap = (getFragmentManager().findFragmentById(R.id.map_dialog));
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.remove(fragmentAutocomple);
        ft.remove(fragmentMap);
        ft.commit();
    }

}
