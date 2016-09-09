package com.manuelblanco.capstonestage2.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fenjuly.mylibrary.SpinnerLoader;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.CountriesActivity;
import com.manuelblanco.capstonestage2.activities.DetailActivity;
import com.manuelblanco.capstonestage2.adapters.MainListTripsAdapter;
import com.manuelblanco.capstonestage2.base.BaseFragment;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.model.Country;
import com.manuelblanco.capstonestage2.providers.ContractTripsProvider;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.manuelblanco.capstonestage2.utils.Validator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 6/08/16.
 */
public class MainSingleListTrips extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        MainListTripsAdapter.OnItemClickListener {

    @BindView(R.id.recycler_grid)
    RecyclerView recyclerView;
    @BindView(R.id.progressbar)
    SpinnerLoader mProgress;
    @BindView(R.id.back_progress)
    View mBackProgress;
    @BindView(R.id.empty_text)
    TextView mEmpty;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;

    private String mType;
    private String mCountry;
    private String mSelectedType;
    private LinearLayoutManager mLayoutManager;
    private MainListTripsAdapter mAdapter;
    private String isFavorite;
    private String TAG = PageListTripsFragment.class.getSimpleName();

    public static MainSingleListTrips newInstance(String country, String type) {
        MainSingleListTrips fragment = new MainSingleListTrips();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_COUNTRY, country);
        args.putString(Constants.ITEM_MENU, type);
        fragment.setArguments(args);
        return fragment;
    }

    public static MainSingleListTrips newInstance(String country, String type, String selectedType) {
        MainSingleListTrips fragment = new MainSingleListTrips();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_COUNTRY, country);
        args.putString(Constants.ITEM_MENU, type);
        args.putString(Constants.SELECTED_TYPE, selectedType);
        fragment.setArguments(args);
        return fragment;
    }

    public static MainSingleListTrips newInstance(String type) {
        MainSingleListTrips fragment = new MainSingleListTrips();
        Bundle args = new Bundle();
        args.putString(Constants.ITEM_MENU, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        mType = getArguments().getString(Constants.ITEM_MENU);
        mSelectedType = getArguments().getString(Constants.SELECTED_TYPE);
        isFavorite = "true";

        if (mType.equals(Constants.FRAGMENT_COUNTRIES)){

            mCountry = getArguments().getString(Constants.KEY_COUNTRY);
            Country country = SqlHandler.getCountryName(mCountry);

            if (!Utils.isTablet(getActivity())) {

                ((CountriesActivity) getActivity()).getSupportActionBar().setTitle(country.getName());
                ((CountriesActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page_list, container, false);
        ButterKnife.bind(this,v);

        mSwipeRefresh.setEnabled(false);
        mProgress.setVisibility(View.VISIBLE);
        mBackProgress.setVisibility(View.VISIBLE);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new MainListTripsAdapter(getActivity(), this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (mType.equals(Constants.FRAGMENT_COUNTRIES)) {
            return new CursorLoader(getActivity(), ContractTripsProvider.CONTENT_URI, null,
                    ContractTripsProvider.ColumnsTrips.COLUMN_COUNTRY + "='" + mCountry + "'", null,
                    ContractTripsProvider.ColumnsTrips.COLUMN_COUNTRY + " DESC");
        }else if (mType.equals(Constants.FRAGMENT_FAVORITES)){
            return new CursorLoader(getActivity(), ContractTripsProvider.CONTENT_URI, null,
                    ContractTripsProvider.ColumnsTrips.COLUMN_FAVORITE + "='" + isFavorite + "'", null,
                    ContractTripsProvider.ColumnsTrips.COLUMN_TITLE + " DESC");
        }else {
            return new CursorLoader(getActivity(), ContractTripsProvider.CONTENT_URI, null,
                    ContractTripsProvider.ColumnsTrips.COLUMN_TYPE + "='" + mSelectedType + "'", null,
                    ContractTripsProvider.ColumnsTrips.COLUMN_TITLE + " DESC");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter != null && data.getCount() > 0) {
            mAdapter.swapCursor(data);
        }else {
            mEmpty.setVisibility(View.VISIBLE);
        }
        mProgress.setVisibility(View.GONE);
        mBackProgress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onClick(RecyclerView.ViewHolder holder, String idTrip) {

        if (Utils.isTablet(getActivity())){
            final android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentLauncher.launchDetailFragment(fm, idTrip,Constants.FRAGMENT_DETAIL,true);
        }else {
            final Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(Constants.KEY_IDTRIP, idTrip);
            ActivityTransitionLauncher.with(getActivity()).from(((MainListTripsAdapter.MainViewHolder) holder).viewPhoto).launch(intent);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (!Utils.isTablet(getActivity()) && mType.equals(Constants.FRAGMENT_COUNTRIES)){
            ((CountriesActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources()
                    .getString(R.string.item_countries));
        }
    }
}
