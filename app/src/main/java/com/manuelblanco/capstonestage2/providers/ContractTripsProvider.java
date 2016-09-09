package com.manuelblanco.capstonestage2.providers;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by manuel on 23/07/16.
 */
public class ContractTripsProvider {

    public static class ColumnsTrips implements BaseColumns {

        private ColumnsTrips() {
        }

        public static final String COLUMN_IDTRIP = "id_trip";
        public static final String COLUMN_ROUTESID = "routes_id";
        public static final String COLUMN_RATE = "rate";
        public static final String COLUMN_IMAGE = "photo";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_COORDS = "coords";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_DESC = "description";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_COMMENTS = "comments";
        public static final String COLUMN_HASROUTES = "hasroutes";
        public static final String COLUMN_CONTINENT = "continent";
        public static final String COLUMN_FAVORITE = "favorite";
        public static final String COLUMN_CREATED = "created";
        public static final String COLUMN_VOTES = "votes";
        public static final String COLUMN_VOTED = "voted";
        public static final String COLUMN_UPDATED = "updated";

    }

    public static final String TABLE_TRIPS = "trips";

    public final static String AUTHORITY = "com.manuelblanco.capstonestage2.providers.TripsProvider";

    public final static String SINGLE_MIME =
            "vnd.android.cursor.item/vnd." + AUTHORITY + TABLE_TRIPS;

    public final static String MULTIPLE_MIME =
            "vnd.android.cursor.dir/vnd." + AUTHORITY + TABLE_TRIPS;

    public final static Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + TABLE_TRIPS);

    public static final UriMatcher uriMatcher;

    public static final int ALLROWS = 1;

    public static final int SINGLE_ROW = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TABLE_TRIPS, ALLROWS);
        uriMatcher.addURI(AUTHORITY, TABLE_TRIPS + "/#", SINGLE_ROW);
    }
}
