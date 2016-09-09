package com.manuelblanco.capstonestage2.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kogitune.activity_transition.fragment.ExitFragmentTransition;
import com.kogitune.activity_transition.fragment.FragmentTransition;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.activities.DetailActivity;
import com.manuelblanco.capstonestage2.activities.MapsActivity;
import com.manuelblanco.capstonestage2.adapters.DetailPagerAdapter;
import com.manuelblanco.capstonestage2.app.AppController;
import com.manuelblanco.capstonestage2.base.BaseFragment;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.network.BackendlessHandler;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.DateUtils;
import com.manuelblanco.capstonestage2.utils.StringsUtils;
import com.manuelblanco.capstonestage2.utils.URLUtils;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.manuelblanco.capstonestage2.utils.UtilsView;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 24/08/16.
 */
public class DetailFragment extends BaseFragment {

    private String mIdTrip;
    private Trip mTrip;

    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.image_collapsing)
    ImageView mImageMain;
    @BindView(R.id.tv_user)
    TextView mUser;
    @BindView(R.id.tv_date)
    TextView mDate;
    @BindView(R.id.tv_votes)
    TextView mVotes;
    @BindView(R.id.ratingBar_header)
    RatingBar mRates;
    @BindView(R.id.ll_types_tourism)
    LinearLayout mTypes;
    @BindView(R.id.viewpager_detail)
    ViewPager mViewPager;
    @BindView(R.id.tablayout_detail)
    TabLayout mTabs;
    @BindView(R.id.card_header)
    LinearLayout mCardHeader;
    @BindView(R.id.appbar)
    AppBarLayout mAppBar;
    @BindView(R.id.toolbar_detail)
    Toolbar toolbar;
    @BindView(R.id.no_info_trip)
    LinearLayout mNoInfo;

    private Animation mShowFab;
    private Animation mHideFab;
    private ExitFragmentTransition exitFragmentTransition;
    private boolean isFirstAnimation;
    private DetailPagerAdapter mAdapter;
    private Tracker mTracker;


    public static DetailFragment newInstance(String idTrip) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_IDTRIP, idTrip);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        setHasOptionsMenu(true);

        mIdTrip = getArguments().getString(Constants.KEY_IDTRIP);

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AppController application = (AppController) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, v);

        if (mIdTrip.equals(getActivity().getString(R.string.no_data))){
            mNoInfo.setVisibility(View.VISIBLE);
            mFab.setVisibility(View.GONE);
        }else {

            if (!Utils.isTablet(getActivity())) {
                ((DetailActivity) getActivity()).setSupportActionBar(toolbar);
                ((DetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            } else {

                toolbar.inflateMenu(R.menu.detail);
                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem arg0) {
                        switch (arg0.getItemId()) {
                            case R.id.action_share:
                                shareTrip();
                                return true;
                            case R.id.action_map_detail:
                                launchMap();
                                return true;
                        }
                        return false;
                    }
                });
            }

            if (mIdTrip != null && !mIdTrip.isEmpty() || mIdTrip != null && !mIdTrip.equals("")) {
                mTrip = getTripFromDB(mIdTrip);

                initImageAndTitle(mTrip);
                populateHeader(mTrip);
            }

            if (mAdapter != null) {
                mAdapter = null;
            }
            mAdapter = new DetailPagerAdapter(getChildFragmentManager(),
                    getActivity(), 4, mTrip);
            mViewPager.setAdapter(mAdapter);
            mShowFab = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_show);
            mHideFab = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_hide);
            mFab.setImageResource(mTrip.getFavorite().equals("true") ? R.drawable.ic_favorite_select : R.drawable.ic_favorite_normal);

            initTabListeners();

            isFirstAnimation = true;

            initAppBarListener();
            initLisneterFAB();

            exitFragmentTransition = FragmentTransition
                    .with(this)
                    .interpolator(new LinearOutSlowInInterpolator())
                    .to(v.findViewById(R.id.image_collapsing))
                    .start(savedInstanceState);
        }

        return v;
    }

    /**
     * Share the link of a film.
     */
    public void shareTrip() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, mTrip.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, mTrip.getDescription());

        startActivity(Intent.createChooser(share, "Share Trip!"));
    }

    private Trip getTripFromDB(String idTrip) {
        return SqlHandler.getTrip(idTrip);
    }

    private void initImageAndTitle(Trip trip) {

        try {
            Picasso.with(getActivity())
                    .load(URLUtils.getURLImageBackendless(getActivity(), trip.getPhoto()))
                    .placeholder(R.drawable.logo_trip_white_placeholder)
                    .resize(getActivity().getResources().getInteger(R.integer.width_imageview_detail),
                            getActivity().getResources().getInteger(R.integer.height_imageview_detail))
                    .onlyScaleDown()
                    .into(mImageMain);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mCollapsingToolbarLayout.setTitle(trip.getTitle());
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);

    }

    private void populateHeader(Trip trip) {

        mUser.setText(trip.getUser());
        mDate.setText(DateUtils.getOnlyDate(trip.getCreated()));
        mDate.setCompoundDrawablesWithIntrinsicBounds(Utils.getFlagIso(getActivity(), trip.getCountry()), 0, 0, 0);
        mVotes.setText(trip.getVotes());
        mRates.setRating(Float.parseFloat(trip.getRate()));
        putIconsTypes(StringsUtils.convertStringToArrayList(trip.getType()));

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (!Utils.isTablet(getActivity())) {
            inflater.inflate(R.menu.detail, menu);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_share:
                shareTrip();
                return true;
            case R.id.action_map_detail:
                launchMap();
                return true;
            case android.R.id.home:
                exitFragmentTransition.startExitListening();
                getActivity().onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void putIconsTypes(List<String> types) {

        int typesSize = types.size();
        mTypes.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < typesSize; i++) {

            View view = inflater.inflate(R.layout.item_card_types, mTypes, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.iv_type);
            imageView.setImageResource(UtilsView.setIconType(types.get(i).toString(), getActivity()));
            lp.setMargins(0, 0, 0, 0);
            view.setLayoutParams(lp);
            mTypes.addView(view);

        }

    }

    private void initTabListeners() {
        mTabs.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));
        mViewPager.setOffscreenPageLimit(4);
        mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                mViewPager.setCurrentItem(tab.getPosition());
                isFirstAnimation = false;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                isFirstAnimation = false;
            }

            @Override
            public void onPageSelected(int position) {

                isFirstAnimation = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                isFirstAnimation = false;
            }
        });
    }

    public void launchMap() {
        Intent i = new Intent(getActivity(), MapsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle b = new Bundle();
        b.putSerializable(Constants.KEY_COORDS, mTrip);
        i.putExtras(b);
        startActivity(i);
    }

    private void initAppBarListener() {

        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                mFab.animate().translationX(-verticalOffset).setInterpolator(new DecelerateInterpolator(2));

                if (verticalOffset < -256) {
                    isFirstAnimation = true;
                }

                if (verticalOffset == 0 && isFirstAnimation) {
                    mFab.startAnimation(mShowFab);
                }
            }
        });
    }

    private void initLisneterFAB() {

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String isFavorite = "";

                mFab.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.expand_button));
                String favoriteFromDB = SqlHandler.getFavoriteState(mIdTrip);

                if (favoriteFromDB.equals("true")) {
                    isFavorite = "false";
                } else {
                    isFavorite = "true";
                }

                mFab.setImageResource(isFavorite.equals("true") ? R.drawable.ic_favorite_select : R.drawable.ic_favorite_normal);

                SqlHandler.updateFavoriteTrip(mTrip, isFavorite);
                BackendlessHandler.updateFavoriteTrip(isFavorite, mIdTrip, getActivity());
            }
        });
    }

    public void updateVotes(String vote, String rate) {

        mVotes.setText(vote);
        mRates.setRating(Float.parseFloat(rate));
    }

    @Override
    public void onResume() {
        super.onResume();

        mTracker.setScreenName(getString(R.string.detail_screen));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
