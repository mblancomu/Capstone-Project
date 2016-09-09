package com.manuelblanco.capstonestage2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.base.BaseFragment;
import com.manuelblanco.capstonestage2.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 25/08/16.
 */
public class InfoFragment extends BaseFragment {

    @BindView(R.id.tv_description_info)
    TextView mTvDescription;
    private String mDescription;

    public static InfoFragment newInstance(String description) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mDescription = getArguments().getString(Constants.KEY_DESCRIPTION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_info, container, false);
        ButterKnife.bind(this, v);

        mTvDescription.setText(mDescription);

        return v;
    }
}
