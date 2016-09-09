package com.manuelblanco.capstonestage2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.TypesActivity;
import com.manuelblanco.capstonestage2.adapters.MainListTypesAdapter;
import com.manuelblanco.capstonestage2.base.BaseFragment;
import com.manuelblanco.capstonestage2.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 25/07/16.
 */
public class PageListTypesFragment extends BaseFragment implements MainListTypesAdapter.OnItemClickListener {

    @BindView(R.id.recycler_grid)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;

    private int mPosition;
    private LinearLayoutManager mLayoutManager;
    private MainListTypesAdapter mAdapter;
    private String TAG = PageListTripsFragment.class.getSimpleName();

    public static PageListTypesFragment newInstance(int tabPosition) {
        PageListTypesFragment fragment = new PageListTypesFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.TAB_POSITION, tabPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt(Constants.TAB_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page_list, container, false);
        ButterKnife.bind(this, v);

        mSwipeRefresh.setEnabled(false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        String[] types = getResources().getStringArray(R.array.trips_types);
        mAdapter = new MainListTypesAdapter(getActivity(), this, types);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(RecyclerView.ViewHolder holder, String type) {

        final Intent intent = new Intent(getActivity(), TypesActivity.class);
        intent.putExtra(Constants.SELECTED_TYPE, type);
        startActivity(intent);
    }
}
