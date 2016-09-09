package com.manuelblanco.capstonestage2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.base.BaseFragment;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.listeners.VotesListener;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.network.BackendlessHandler;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.DialogUtils;
import com.manuelblanco.capstonestage2.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 25/08/16.
 */
public class VotesFragment extends BaseFragment {

    @BindView(R.id.ib_vote)
    ImageButton mVote;
    @BindView(R.id.ratingbar_vote)
    RatingBar mRatingBar;
    @BindView(R.id.container_for_vote)
    LinearLayout mForVote;
    @BindView(R.id.container_with_voted)
    LinearLayout mWithVote;
    @BindView(R.id.tv_assignated_score)
    TextView mAssignatedScore;

    private String mRate;
    private String mNewVote;
    private String previousVote;
    private VotesListener mListener;
    private String mVoteState;
    private String previousRate;

    private String mIdTrip;

    public static VotesFragment newInstance(String idTrip) {
        VotesFragment fragment = new VotesFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_IDTRIP, idTrip);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mIdTrip = getArguments().getString(Constants.KEY_IDTRIP);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (VotesListener) context;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_votes, container, false);
        ButterKnife.bind(this, v);

        previousVote = SqlHandler.getPreviousVote(mIdTrip);
        mVoteState = SqlHandler.getVotedState(mIdTrip);
        previousRate = SqlHandler.getRate(mIdTrip);

        if (previousVote == null || previousVote.equals("")) {
            previousVote = String.valueOf(0);
        }

        if (mVoteState == null || mVoteState.equals("")) {
            mVoteState = "false";
        }

        if (previousRate == null || previousRate.equals("")) {
            previousRate = "0.0";
        }

        if (mVoteState.equals("true")) {
            mForVote.setVisibility(View.GONE);
            mWithVote.setVisibility(View.VISIBLE);

            mAssignatedScore.setText(previousRate);
        } else {
            mForVote.setVisibility(View.VISIBLE);
            mWithVote.setVisibility(View.GONE);

            mVote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    vote();
                }
            });

            mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                    getRating(v);
                }
            });
        }

        return v;
    }

    public void vote() {

        mVote.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.expand_button));

        int i = Integer.parseInt(previousVote.trim());
        i = i + 1;
        mNewVote = String.valueOf(i);
    }

    public void getRating(float value) {
        String rating = String.valueOf(value);
        mRate = rating;

        if (Utils.isNetworkAvailable(getActivity())) {
            updateRate();
        } else {
            DialogUtils.launchInfoDialog(getActivity(), getString(R.string.problem_network));
        }
    }

    private void updateRate() {
        Trip trip = new Trip();
        trip.setId_trip(mIdTrip);
        trip.setVotes(mNewVote);
        trip.setRate(mRate);
        trip.setVoted("true");

        SqlHandler.updateRateTrip(trip);
        BackendlessHandler.updateRateTrip(trip, mIdTrip, getActivity());
        mListener.onRateChange(mNewVote, mRate);

        mForVote.setVisibility(View.GONE);
        mWithVote.setVisibility(View.VISIBLE);
        mAssignatedScore.setText(mRate);
    }
}

