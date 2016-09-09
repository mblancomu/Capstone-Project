package com.manuelblanco.capstonestage2.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fenjuly.mylibrary.SpinnerLoader;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.adapters.RecommendsAdapter;
import com.manuelblanco.capstonestage2.base.BaseFragment;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.model.Recommend;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.views.MarginItemDecoration;
import com.manuelblanco.capstonestage2.views.SimpleDividerItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 25/08/16.
 */
public class RecommendsFragment extends BaseFragment {

    @BindView(R.id.recycler_grid)
    RecyclerView mRecyclerView;
    @BindView(R.id.back_progress)
    View mBackProgress;
    @BindView(R.id.progressbar)
    SpinnerLoader mProgress;
    @BindView(R.id.empty_text)
    TextView mEmptyText;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;

    private int mPosition;
    private String mIdTrip;
    private LinearLayoutManager mLayoutManager;
    private RecommendsAdapter mAdapter;

    public static RecommendsFragment newInstance(String idTrip, int position) {
        RecommendsFragment fragment = new RecommendsFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_IDTRIP, idTrip);
        args.putInt(Constants.KEY_ORIGIN_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mIdTrip = getArguments().getString(Constants.KEY_IDTRIP);
        mPosition = getArguments().getInt(Constants.KEY_ORIGIN_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page_list, container, false);
        ButterKnife.bind(this, v);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mSwipeRefresh.setEnabled(false);

        if (getTypeOfRecommend() == null || getTypeOfRecommend().size() == 0){

            mBackProgress.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            mEmptyText.setVisibility(View.VISIBLE);

        }else {

            mProgress.setVisibility(View.VISIBLE);
            mBackProgress.setVisibility(View.VISIBLE);

            mAdapter = new RecommendsAdapter(getActivity(), getTypeOfRecommend());

            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        }

        return v;
    }

    private ArrayList<Recommend> getTypeOfRecommend() {
        ArrayList<Recommend> recommends;

        if (mPosition == 1) {
            recommends = SqlHandler.getRecommendsByType(Constants.TYPE_SLEEP, mIdTrip);
        } else {
            recommends = SqlHandler.getRecommendsByType(Constants.TYPE_EAT, mIdTrip);
        }

        mProgress.setVisibility(View.GONE);
        mBackProgress.setVisibility(View.GONE);

        return recommends;
    }

}
