package com.manuelblanco.capstonestage2.fragments;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fenjuly.mylibrary.SpinnerLoader;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.account.GenericAccountService;
import com.manuelblanco.capstonestage2.activities.DetailActivity;
import com.manuelblanco.capstonestage2.adapters.MainListTripsAdapter;
import com.manuelblanco.capstonestage2.base.BaseFragment;
import com.manuelblanco.capstonestage2.listeners.LoadDataListener;
import com.manuelblanco.capstonestage2.network.BackendlessHandler;
import com.manuelblanco.capstonestage2.providers.ContractTripsProvider;
import com.manuelblanco.capstonestage2.syncadapter.SyncService;
import com.manuelblanco.capstonestage2.syncadapter.SyncUtils;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.DialogUtils;
import com.manuelblanco.capstonestage2.utils.Utils;
import com.manuelblanco.capstonestage2.utils.UtilsView;
import com.manuelblanco.capstonestage2.utils.Validator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by manuel on 17/07/16.
 */
public class PageListTripsFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>,
MainListTripsAdapter.OnItemClickListener, LoadDataListener{

    @BindView(R.id.recycler_grid)
    RecyclerView recyclerView;
    @BindView(R.id.progressbar)
    SpinnerLoader mProgress;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.back_progress)
    View mBackProgress;

    /**
     * Handle to a SyncObserver. The ProgressBar element is visible until the SyncObserver reports
     * that the sync is complete.
     *
     * <p>This allows us to delete our SyncObserver once the application is no longer in the
     * foreground.
     */
    private Object mSyncObserverHandle;

    private int mPosition;
    private LinearLayoutManager mLayoutManager;
    private MainListTripsAdapter mAdapter;
    private String TAG = PageListTripsFragment.class.getSimpleName();

    public static PageListTripsFragment newInstance(int tabPosition) {
        PageListTripsFragment fragment = new PageListTripsFragment();
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

    /**
     * Create SyncAccount at launch, if needed.
     *
     * <p>This will create a new account with the system for our application, register our
     * {@link SyncService} with it, and establish a sync schedule.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page_list, container, false);
        ButterKnife.bind(this,v);

        mProgress.setVisibility(View.VISIBLE);
        mBackProgress.setVisibility(View.VISIBLE);
        mSwipeRefresh.setColorSchemeColors( R.color.s1, R.color.s2, R.color.s3, R.color.s4);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new MainListTripsAdapter(getActivity(),this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        initSwipeListener();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(mPosition, null, this);

        mSyncStatusObserver.onStatusChanged(0);

        // Watch for sync state changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
        SyncUtils.TriggerRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), ContractTripsProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter != null) {
            mAdapter.swapCursor(data);
        }
        mProgress.setVisibility(View.GONE);
        mBackProgress.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //mAdapter.swapCursor(null);
    }

    @Override
    public void onClick(RecyclerView.ViewHolder holder, String idTrip) {

        if (!Utils.isTablet(getActivity())) {
            final Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(Constants.KEY_IDTRIP, idTrip);
            View view = ((MainListTripsAdapter.MainViewHolder) holder).viewPhoto;
            ActivityTransitionLauncher.with(getActivity()).from(view).launch(intent);
        }else{
            DetailFragment detailFragment = (DetailFragment) getActivity().getSupportFragmentManager().findFragmentByTag(Constants.FRAGMENT_DETAIL);
            if(detailFragment != null)
                getActivity().getSupportFragmentManager().beginTransaction().remove(detailFragment).commit();
            final android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentLauncher.launchDetailFragment(fm, idTrip,
                    Constants.FRAGMENT_DETAIL,true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getLoaderManager().destroyLoader(0);
            if (mAdapter != null) {
                mAdapter.swapCursor(null);
                mAdapter = null;
            }
        } catch (Throwable localThrowable) {
        }
    }

    private void initSwipeListener() {

        mSwipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        if (Utils.isNetworkAvailable(getActivity())){
                            BackendlessHandler backendlessHandler = new BackendlessHandler(getActivity(),
                                    PageListTripsFragment.this);
                            backendlessHandler.initLoadData(false);
                        }else {
                            mSwipeRefresh.setRefreshing(false);
                            DialogUtils.launchInfoDialog(getActivity(),getString(R.string.problem_network));
                        }
                    }
                }
        );
    }

    /**
     * Crfate a new anonymous SyncStatusObserver. It's attached to the app's ContentResolver in
     * onResume(), and removed in onPause(). If status changes, it sets the state of the Refresh
     * button. If a sync is active or pending, the Refresh button is replaced by an indeterminate
     * ProgressBar; otherwise, the button itself is displayed.
     */
    private SyncStatusObserver mSyncStatusObserver = new SyncStatusObserver() {
        /** Callback invoked with the sync adapter status changes. */
        @Override
        public void onStatusChanged(int which) {
            getActivity().runOnUiThread(new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {
                    // Create a handle to the account that was created by
                    // SyncService.CreateSyncAccount(). This will be used to query the system to
                    // see how the sync status has changed.
                    Account account = GenericAccountService.GetAccount(SyncUtils.ACCOUNT_TYPE);
                    if (account == null) {
                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(
                            account, ContractTripsProvider.AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(
                            account, ContractTripsProvider.AUTHORITY);
                    if(syncActive || syncPending){
                        UtilsView.showSnackBar((CoordinatorLayout)getActivity().findViewById(R.id.main_coordinator),
                                getString(R.string.updating_trips), getResources().getColor(R.color.colorAccent));

                    };
                }

            });
        }

    };

    @Override
    public void onSuccessLoadData() {
        mSwipeRefresh.setRefreshing(false);
        UtilsView.showSnackBar((CoordinatorLayout)getActivity().findViewById(R.id.main_coordinator),
                getString(R.string.updating_trips), getResources().getColor(R.color.colorAccent));
    }
}

