package com.manuelblanco.capstonestage2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eftimoff.viewpagertransformers.CubeOutTransformer;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.adapters.MainTripsPagerAdapter;
import com.manuelblanco.capstonestage2.base.BaseFragment;
import com.manuelblanco.capstonestage2.syncadapter.SyncUtils;
import com.manuelblanco.capstonestage2.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 16/07/16.
 */
public class MainFragment extends BaseFragment {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tablayout)
    TabLayout mTabs;

    private Constants.AdapterTypes mType;

    public static MainFragment newInstance(Constants.AdapterTypes tabType) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constants.TAB_TYPE, tabType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        if (getArguments() == null){
            mType = Constants.AdapterTypes.TYPE_TRIPS;
        }else{
            mType = (Constants.AdapterTypes) getArguments().getSerializable(Constants.TAB_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);

        MainTripsPagerAdapter mAdapter = new MainTripsPagerAdapter(getActivity().getSupportFragmentManager(),
                getActivity(), 3);
        viewPager.setAdapter(mAdapter);
        viewPager.setPageTransformer(true, new CubeOutTransformer());

        initTabListeners();
        viewPager.setOffscreenPageLimit(3);

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

}
