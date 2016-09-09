package com.manuelblanco.capstonestage2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eftimoff.viewpagertransformers.CubeOutTransformer;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.CountriesActivity;
import com.manuelblanco.capstonestage2.adapters.CountriesPagerAdapter;
import com.manuelblanco.capstonestage2.base.BaseFragment;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.listeners.DetailTabletListener;
import com.manuelblanco.capstonestage2.listeners.DialogPhotoListener;
import com.manuelblanco.capstonestage2.providers.ContractTripsProvider;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 3/08/16.
 */
public class CountriesFragment extends BaseFragment {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tablayout)
    TabLayout mTabs;

    private Constants.AdapterTypes mType;

    public static CountriesFragment newInstance(Constants.AdapterTypes tabType) {
        CountriesFragment fragment = new CountriesFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.TAB_TYPE, tabType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        ((CountriesActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((CountriesActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getResources()
                .getString(R.string.item_countries));

        mType = (Constants.AdapterTypes) getArguments().getSerializable(Constants.TAB_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);

        if (Utils.isTablet(getActivity())){
            getActivity().findViewById(R.id.view_false_toolbar).setVisibility(View.GONE);
        }

        ArrayList<String> continents = SqlHandler.getColumnList(ContractTripsProvider.ColumnsTrips.COLUMN_CONTINENT, ContractTripsProvider.TABLE_TRIPS, 13);
        List<Fragment> fragmentList = buildCountryFragments(continents);
        CountriesPagerAdapter mAdapter = new CountriesPagerAdapter(getChildFragmentManager(),
                getActivity(), continents, fragmentList);
        viewPager.setAdapter(mAdapter);
        viewPager.setPageTransformer(true, new CubeOutTransformer());

        initTabListeners();
        viewPager.setOffscreenPageLimit(10);

        return v;
    }

    private void initTabListeners() {
        mTabs.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private List<android.support.v4.app.Fragment> buildCountryFragments(ArrayList<String> continents) {
        List<Fragment> fragments = new ArrayList<android.support.v4.app.Fragment>();
        for (int i = 0; i < continents.size(); i++) {
            fragments.add(PageCountriesFragment.newInstance(i,continents.get(i).toString()));
        }

        return fragments;
    }

}

