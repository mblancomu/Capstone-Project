package com.manuelblanco.capstonestage2.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.manuelblanco.capstonestage2.app.AppController;
import com.manuelblanco.capstonestage2.db.DataBaseHelper;
import com.manuelblanco.capstonestage2.utils.Constants;

/**
 * Created by manuel on 17/07/16.
 */
public class TripsProvider extends ContentProvider{

    @Override
    public boolean onCreate() {

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {

        SQLiteDatabase db = AppController.getmSqlHandler().sqLiteDatabase;

        int match = ContractTripsProvider.uriMatcher.match(uri);

        Cursor c;

        switch (match) {
            case ContractTripsProvider.ALLROWS:
                // Consultando todos los registros
                c = db.query(ContractTripsProvider.TABLE_TRIPS, strings,
                        s, strings1,
                        null, null, s1);
                c.setNotificationUri(
                        getContext().getContentResolver(),
                        ContractTripsProvider.CONTENT_URI);
                break;
            case ContractTripsProvider.SINGLE_ROW:
                long tripID = ContentUris.parseId(uri);
                c = db.query(ContractTripsProvider.TABLE_TRIPS, strings,
                        ContractTripsProvider.ColumnsTrips._ID + " = " + tripID,
                        strings1, null, null, s1);
                c.setNotificationUri(
                        getContext().getContentResolver(),
                        ContractTripsProvider.CONTENT_URI);
                break;
            default:
                throw new IllegalArgumentException("URI not supported: " + uri);
        }
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (ContractTripsProvider.uriMatcher.match(uri)) {
            case ContractTripsProvider.ALLROWS:
                return ContractTripsProvider.MULTIPLE_MIME;
            case ContractTripsProvider.SINGLE_ROW:
                return ContractTripsProvider.SINGLE_MIME;
            default:
                throw new IllegalArgumentException("Unknown trip: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        if (ContractTripsProvider.uriMatcher.match(uri) != ContractTripsProvider.ALLROWS) {
            throw new IllegalArgumentException("URI Unknown : " + uri);
        }
        ContentValues contentValues;
        if (values != null) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }

        SQLiteDatabase db = AppController.getmSqlHandler().sqLiteDatabase;
        long rowId = db.insert(ContractTripsProvider.TABLE_TRIPS,
                null, contentValues);
        if (rowId > 0) {
            Uri uri_actividad =
                    ContentUris.withAppendedId(
                            ContractTripsProvider.CONTENT_URI, rowId);
            getContext().getContentResolver().
                    notifyChange(uri_actividad, null);
            return uri_actividad;
        }
        throw new SQLException("Error to inserted row in : " + uri);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {

        SQLiteDatabase db = AppController.getmSqlHandler().sqLiteDatabase;

        int match = ContractTripsProvider.uriMatcher.match(uri);
        int affected;

        switch (match) {
            case ContractTripsProvider.ALLROWS:
                affected = db.delete(ContractTripsProvider.TABLE_TRIPS,
                        s,
                        strings);
                break;
            case ContractTripsProvider.SINGLE_ROW:
                long tripId = ContentUris.parseId(uri);
                affected = db.delete(ContractTripsProvider.TABLE_TRIPS,
                        ContractTripsProvider.ColumnsTrips._ID + "=" + tripId
                                + (!TextUtils.isEmpty(s) ?
                                " AND (" + s + ')' : ""),
                        strings);
                getContext().getContentResolver().
                        notifyChange(uri, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown trip: " +
                        uri);
        }
        return affected;
    }

    @Override
    public int update(Uri uri, ContentValues values, String s, String[] strings) {
        SQLiteDatabase db = AppController.getmSqlHandler().sqLiteDatabase;
        int affected;
        switch (ContractTripsProvider.uriMatcher.match(uri)) {
            case ContractTripsProvider.ALLROWS:
                affected = db.update(ContractTripsProvider.TABLE_TRIPS, values,
                        s, strings);
                break;
            case ContractTripsProvider.SINGLE_ROW:
                String tripId = uri.getPathSegments().get(1);
                affected = db.update(ContractTripsProvider.TABLE_TRIPS, values,
                        ContractTripsProvider.ColumnsTrips._ID + "=" + tripId
                                + (!TextUtils.isEmpty(s) ?
                                " AND (" + s + ')' : ""),
                        strings);
                break;
            default:
                throw new IllegalArgumentException("URI unknown: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affected;
    }
}
