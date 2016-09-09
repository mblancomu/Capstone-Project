package com.manuelblanco.capstonestage2.db;

import android.database.sqlite.SQLiteDatabase;

import com.manuelblanco.capstonestage2.providers.ContractTripsProvider;

/**
 * Created by manuel on 17/07/16.
 */
public class TripsDB {



    private static final String DATABASE_CREATE = "create table "
            + ContractTripsProvider.TABLE_TRIPS
            + "("
            + ContractTripsProvider.ColumnsTrips._ID + " integer primary key autoincrement, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_IDTRIP + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_ROUTESID + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_TITLE + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_RATE + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_IMAGE + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_TYPE + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_COORDS + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_COUNTRY + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_DESC + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_USER + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_COMMENTS + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_HASROUTES + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_CONTINENT + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_FAVORITE + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_CREATED + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_VOTES + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_VOTED + " text, "
            + ContractTripsProvider.ColumnsTrips.COLUMN_UPDATED + " text " +
            ");";

    public static final String[] FIELDS = {ContractTripsProvider.ColumnsTrips._ID, ContractTripsProvider.ColumnsTrips.COLUMN_IDTRIP,
            ContractTripsProvider.ColumnsTrips.COLUMN_ROUTESID, ContractTripsProvider.ColumnsTrips.COLUMN_TITLE,
            ContractTripsProvider.ColumnsTrips.COLUMN_RATE, ContractTripsProvider.ColumnsTrips.COLUMN_IMAGE,
            ContractTripsProvider.ColumnsTrips.COLUMN_TYPE,
            ContractTripsProvider.ColumnsTrips.COLUMN_COORDS, ContractTripsProvider.ColumnsTrips.COLUMN_COUNTRY,
            ContractTripsProvider.ColumnsTrips.COLUMN_DESC, ContractTripsProvider.ColumnsTrips.COLUMN_USER,
            ContractTripsProvider.ColumnsTrips.COLUMN_COMMENTS,
            ContractTripsProvider.ColumnsTrips.COLUMN_HASROUTES,
            ContractTripsProvider.ColumnsTrips.COLUMN_CONTINENT,ContractTripsProvider.ColumnsTrips.COLUMN_FAVORITE,
            ContractTripsProvider.ColumnsTrips.COLUMN_CREATED,ContractTripsProvider.ColumnsTrips.COLUMN_VOTES,
            ContractTripsProvider.ColumnsTrips.COLUMN_VOTED, ContractTripsProvider.ColumnsTrips.COLUMN_UPDATED};

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {

        database.execSQL("DROP TABLE IF EXISTS " + ContractTripsProvider.TABLE_TRIPS);
        onCreate(database);

    }
}
