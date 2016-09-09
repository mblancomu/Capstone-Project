package com.manuelblanco.capstonestage2.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by manuel on 30/07/16.
 */
public class CountriesDB {

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ISO = "iso";
    public static final  String COLUMN_NAME = "name";
    public static final String TABLE_COUNTRIES = "countries";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_COUNTRIES
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ISO + " text, "
            + COLUMN_NAME + " text " +
            ");";

    public static final String[] FIELDS = {COLUMN_ID, COLUMN_ISO,COLUMN_NAME};

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);
        onCreate(database);

    }
}
