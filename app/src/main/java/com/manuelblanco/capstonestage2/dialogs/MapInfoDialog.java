package com.manuelblanco.capstonestage2.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.adapters.MapDialogAdapter;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.listeners.DialogMapListener;
import com.manuelblanco.capstonestage2.model.Country;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.Utils;

/**
 * Created by manuel on 6/08/16.
 */
public class MapInfoDialog extends DialogFragment implements MapDialogAdapter.OnItemClickListener {

    private String mCountry;
    private TextView mTextCountry;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MapDialogAdapter mAdapter;
    private LatLng mCurrentPosition;
    private DialogMapListener mListener;


    public static MapInfoDialog newInstance(String country, LatLng currentPosition) {
        MapInfoDialog fragment = new MapInfoDialog();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_COUNTRY, country);
        args.putParcelable(Constants.KEY_LOCATION, currentPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCountry = getArguments().getString(Constants.KEY_COUNTRY);
        mCurrentPosition = getArguments().getParcelable(Constants.KEY_LOCATION);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogMapListener) context;
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
                .setPositiveButton(getActivity().getResources().getString(R.string.button_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );

        LayoutInflater i = getActivity().getLayoutInflater();

        View v = i.inflate(R.layout.dialog_map_info, null);

        mTextCountry = (TextView) v.findViewById(R.id.country_card);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_trips);

        mTextCountry.setCompoundDrawablesWithIntrinsicBounds(0, 0, Utils.getFlagIso(getActivity(), mCountry), 0);

        Country country = SqlHandler.getCountryName(mCountry);
        mTextCountry.setText(country.getName());

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new MapDialogAdapter(getActivity(), this, SqlHandler.getTripsByCountry(mCountry), mCurrentPosition);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        b.setView(v);
        return b.create();
    }

    @Override
    public void onClick(RecyclerView.ViewHolder holder, Trip country) {
        mListener.onTripSelected(country);
        dismiss();
    }
}
