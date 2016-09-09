package com.manuelblanco.capstonestage2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fenjuly.mylibrary.SpinnerLoader;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.adapters.CountriesAdapter;
import com.manuelblanco.capstonestage2.base.BaseFragment;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.listeners.DetailTabletListener;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.manuelblanco.capstonestage2.utils.Validator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 28/07/16.
 */
public class PageCountriesFragment extends BaseFragment implements
        CountriesAdapter.OnItemClickListener {

    @BindView(R.id.recycler_grid)
    RecyclerView recyclerView;
    @BindView(R.id.progressbar)
    SpinnerLoader mProgress;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;

    private int mPosition;
    private String mContinent;
    private LinearLayoutManager mLayoutManager;
    private CountriesAdapter mAdapter;
    private String TAG = PageListTripsFragment.class.getSimpleName();
    private DetailTabletListener mListener;

    public static PageCountriesFragment newInstance(int tabPosition,String continent) {
        PageCountriesFragment fragment = new PageCountriesFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.TAB_POSITION, tabPosition);
        args.putString(Constants.KEY_CONTINENT,continent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPosition = getArguments().getInt(Constants.TAB_POSITION);
        mContinent = getArguments().getString(Constants.KEY_CONTINENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page_list, container, false);
        ButterKnife.bind(this, v);

        if (Utils.isTablet(getActivity())){
            getActivity().findViewById(R.id.view_false_toolbar).setVisibility(View.VISIBLE);
        }

        mSwipeRefresh.setEnabled(false);
        ArrayList<String> countriesList = SqlHandler.getCountriesByRegion(mContinent);

        Set<String> hs = new HashSet<>();
        hs.addAll(countriesList);
        countriesList.clear();
        countriesList.addAll(hs);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new CountriesAdapter(getActivity(), this, countriesList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (Utils.isTablet(getActivity())){
            try {
                mListener = (DetailTabletListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement IFragmentToActivity");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void launchCountriesFragment(String country, String tag) {

        MainSingleListTrips fr = MainSingleListTrips.newInstance(country, Constants.FRAGMENT_COUNTRIES);

        android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.container_main, fr, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onClick(RecyclerView.ViewHolder holder, int trips, String country) {
        launchCountriesFragment(country, Constants.FRAGMENT_DETAIL_COUNTRY);
        if (Utils.isTablet(getActivity())){
            List<Trip> trip = SqlHandler.getTripsByCountry(country);
            final android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentLauncher.launchDetailFragment(fm, Validator.isIdTripFromListValid(getActivity(),trip),"",true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (Utils.isTablet(getActivity())){
            getActivity().findViewById(R.id.view_false_toolbar).setVisibility(View.GONE);
        }
    }
}
