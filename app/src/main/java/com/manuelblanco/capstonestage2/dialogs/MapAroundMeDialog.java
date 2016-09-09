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

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.adapters.MapDialogAroundMeAdapter;
import com.manuelblanco.capstonestage2.listeners.DialogMapListener;
import com.manuelblanco.capstonestage2.model.AroundMe;
import com.manuelblanco.capstonestage2.presenter.MapPresenter;
import com.manuelblanco.capstonestage2.services.GPSTracker;

import java.util.ArrayList;

/**
 * Created by manuel on 6/08/16.
 */
public class MapAroundMeDialog extends DialogFragment implements MapDialogAroundMeAdapter.OnItemClickListener {

    private TextView mTextCountry;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MapDialogAroundMeAdapter mAdapter;
    private MapPresenter presenter;
    private DialogMapListener mListener;
    private ArrayList<AroundMe> pointsNear;
    int newRatio;
    private int mRatio = 1000000;

    private AlertDialog dialog;

    public static MapAroundMeDialog newInstance() {
        MapAroundMeDialog fragment = new MapAroundMeDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MapPresenter(getActivity(), null, new GPSTracker(getActivity()));
        newRatio = mRatio;
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
    public void onResume() {
        super.onResume();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRatio = mRatio + newRatio;
                mAdapter.addMorePoints(presenter.getTripsAroundMe(newRatio));
            }
        });
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setPositiveButton(getActivity().getResources().getString(R.string.button_load_more),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                .setNegativeButton(getActivity().getResources().getString(R.string.button_cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

        LayoutInflater i = getActivity().getLayoutInflater();

        View v = i.inflate(R.layout.dialog_map_info, null);

        mTextCountry = (TextView) v.findViewById(R.id.country_card);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_trips);

        mTextCountry.setText(getString(R.string.aroundme));

        pointsNear = presenter.getTripsAroundMe(mRatio);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new MapDialogAroundMeAdapter(getActivity(), this, pointsNear);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        b.setView(v);

        dialog = b.create();
        return dialog;
    }

    @Override
    public void onClick(RecyclerView.ViewHolder holder, AroundMe point) {
        mListener.onAroundMeSelected(point);
        dismiss();
    }
}
