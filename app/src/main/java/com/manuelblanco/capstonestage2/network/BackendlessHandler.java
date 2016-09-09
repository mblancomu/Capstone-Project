package com.manuelblanco.capstonestage2.network;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.backendless.persistence.local.UserTokenStorageFactory;
import com.fenjuly.mylibrary.SpinnerLoader;
import com.manuelblanco.capstonestage2.R;
import com.manuelblanco.capstonestage2.backend.LoadingCallback;
import com.manuelblanco.capstonestage2.db.RecommendsDB;
import com.manuelblanco.capstonestage2.db.SqlHandler;
import com.manuelblanco.capstonestage2.listeners.LoadDataListener;
import com.manuelblanco.capstonestage2.model.Recommend;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.providers.ContractTripsProvider;
import com.manuelblanco.capstonestage2.utils.DateUtils;
import com.manuelblanco.capstonestage2.utils.DialogUtils;
import com.manuelblanco.capstonestage2.utils.Utils;

import java.util.ArrayList;

/**
 * Created by manuel on 23/07/16.
 */
public class BackendlessHandler {

    private Context mContext;
    private BackendlessCollection<Trip> trips;
    private ArrayList<Trip> totalTrips = new ArrayList<>();
    private ArrayList<Trip> updateTrips = new ArrayList<>();
    private ArrayList<Recommend> totalRecommend = new ArrayList<>();
    private LoadDataListener mListener;
    private boolean isValidLogin;

    public BackendlessHandler(Context context,LoadDataListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public void initLoadData(boolean sync){

        int data = SqlHandler.getCount(ContractTripsProvider.TABLE_TRIPS);

        if (data == 0){

            if (Utils.isNetworkAvailable(mContext)){
                loadDataToDB(sync);
            } else{
                DialogUtils.launchInfoDialog(mContext,mContext.getString(R.string.first_synch));
            }

        }else{
            if (Utils.isNetworkAvailable(mContext)) {
                getCountTripsFromServer(sync);
            }else {
                DialogUtils.launchInfoDialog(mContext,mContext.getString(R.string.check_updates));
            }
        }
    }

    public void loadDataToDB(final boolean sync){

        int PAGESIZE = 80;
        QueryOptions queryOptions = new QueryOptions();

        final BackendlessDataQuery query = new BackendlessDataQuery( queryOptions );
        query.setPageSize(PAGESIZE);

        Backendless.Data.of( Trip.class ).find(query, new LoadingCallback<BackendlessCollection<Trip>>(mContext)
        {
            @Override
            public void handleResponse( BackendlessCollection<Trip> tripsBackendlessCollection ) {

                insertDataToDBTrips(tripsBackendlessCollection);
                getCountRecommendsFromServer(query, sync);

                super.handleResponse( tripsBackendlessCollection );

            }

            @Override
            public void handleFault(BackendlessFault fault) {
                super.handleFault(fault);
                Toast.makeText(mContext, mContext.getString(R.string.backend_problem), Toast.LENGTH_LONG).show();
            }
        } );
    }

    private void getCountTripsFromServer(final boolean sync){

        Backendless.Persistence.of(Trip.class).find( new AsyncCallback<BackendlessCollection<Trip>>(){
            @Override
            public void handleResponse( BackendlessCollection<Trip> foundTrips )
            {
                verifyDate(foundTrips.getTotalObjects(), sync);
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Toast.makeText(mContext, mContext.getString(R.string.backend_problem), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getCountRecommendsFromServer(final BackendlessDataQuery query, final boolean sync){

        Backendless.Persistence.of(Recommend.class).find( new AsyncCallback<BackendlessCollection<Recommend>>(){
            @Override
            public void handleResponse( BackendlessCollection<Recommend> foundRecommends )
            {
                downloadRecommends(query,foundRecommends.getTotalObjects(), sync);
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                Toast.makeText(mContext, mContext.getString(R.string.backend_problem), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void verifyDate(int countFromServer, final boolean sync){
        String date = SqlHandler.getLastUpdated();
        int dataTrips = SqlHandler.getCount(ContractTripsProvider.TABLE_TRIPS);

        if (countFromServer > dataTrips){

            SqlHandler.clearTable(ContractTripsProvider.TABLE_TRIPS);
            loadDataToDB(sync);

        }else if (dataTrips == countFromServer){

            String whereClause = "updated > '" + date + "'";
            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
            dataQuery.setWhereClause( whereClause );

            Backendless.Persistence.of(Trip.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Trip>>() {
                @Override
                public void handleResponse(BackendlessCollection<Trip> response) {

                    if (response.getData().size() > 0){
                        updateTrips.addAll(response.getData());
                        SqlHandler.putLastUpdate(DateUtils.getActualDate());
                        SqlHandler.updateListTrip(updateTrips);
                    }

                    if (!sync){
                        mListener.onSuccessLoadData();
                    }
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    Toast.makeText(mContext, mContext.getString(R.string.backend_problem), Toast.LENGTH_LONG).show();
                }
            });

        }else{
            if (!sync){
                mListener.onSuccessLoadData();
            }
        }
    }

    private void downloadRecommends(BackendlessDataQuery query, int countFromServer, final boolean sync){

        int dataRecommends = SqlHandler.getCount(RecommendsDB.TABLE_RECOMMENDS);

        if (countFromServer > dataRecommends) {

            Backendless.Data.of(Recommend.class).find(query, new LoadingCallback<BackendlessCollection<Recommend>>(mContext) {
                @Override
                public void handleResponse(BackendlessCollection<Recommend> tripsBackendlessCollection) {

                    insertDataToDBRecommend(tripsBackendlessCollection);

                    if (!sync) {
                        mListener.onSuccessLoadData();
                    }

                    super.handleResponse(tripsBackendlessCollection);

                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    super.handleFault(fault);
                    Toast.makeText(mContext, mContext.getString(R.string.backend_problem), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void insertDataToDBTrips(BackendlessCollection<Trip> tripsBackendlessCollection){

        totalTrips.addAll(tripsBackendlessCollection.getData());

        SqlHandler.putListTrip(totalTrips);
        SqlHandler.putLastUpdate(DateUtils.getActualDate());
    }

    private void insertDataToDBRecommend(BackendlessCollection<Recommend> tripsBackendlessCollection){

        totalRecommend.addAll(tripsBackendlessCollection.getData());

        SqlHandler.putListRecommend(totalRecommend);
    }

    public static void updateRateTrip(final Trip tripReceiver, String idTrip, final Context context)
    {
        String whereClause = "id_trip = '" + idTrip + "'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause );

        Backendless.Persistence.of(Trip.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Trip>>() {
            @Override
            public void handleResponse(BackendlessCollection<Trip> response) {

                ArrayList<Trip> trips = new ArrayList<Trip>();
                trips.addAll(response.getData());

                Trip trip = trips.get(0);
                trip.setRate(tripReceiver.getRate());
                trip.setVotes(tripReceiver.getVotes());

                Backendless.Persistence.of( Trip.class ).save( trip, new AsyncCallback<Trip>() {
                    @Override
                    public void handleResponse( Trip response )
                    {
                        Toast.makeText(context,context.getString(R.string.update_votes),Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Toast.makeText(context, context.getString(R.string.backend_problem), Toast.LENGTH_LONG).show();
                    }
                } );
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, context.getString(R.string.backend_problem), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void updateFavoriteTrip(final String isFavorite, String idTrip, final Context context)
    {
        String whereClause = "id_trip = '" + idTrip + "'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause );

        Backendless.Persistence.of(Trip.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Trip>>() {
            @Override
            public void handleResponse(BackendlessCollection<Trip> response) {

                ArrayList<Trip> trips = new ArrayList<Trip>();
                trips.addAll(response.getData());

                Trip trip = trips.get(0);
                trip.setFavorite(isFavorite);

                Backendless.Persistence.of( Trip.class ).save( trip, new AsyncCallback<Trip>() {
                    @Override
                    public void handleResponse( Trip response )
                    {
                        Toast.makeText(context,isFavorite.equals("true") ? "Added to favorites" :
                                "Removed from favorites",Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Toast.makeText(context, context.getString(R.string.backend_problem), Toast.LENGTH_LONG).show();
                    }
                } );
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(context, context.getString(R.string.backend_problem), Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean verifyIsLogged(){
        String userToken = UserTokenStorageFactory.instance().getStorage().get();

        if( userToken != null && !userToken.equals( "" ) ) {
            return true;
        }else {
            return false;
        }
    }

    public boolean validLogin(){

        AsyncCallback<Boolean> isValidLoginCallback = new AsyncCallback<Boolean>()
        {
            @Override
            public void handleResponse( Boolean response )
            {
               isValidLogin = true;
            }

            @Override
            public void handleFault( BackendlessFault fault )
            {
                isValidLogin = false;
            }

        };

        Backendless.UserService.isValidLogin( isValidLoginCallback );

        return isValidLogin;
    }

}