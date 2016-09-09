package com.manuelblanco.capstonestage2.db;

import android.database.sqlite.SQLiteDatabase;

public class TripsTypesDB {

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IDTRIP = "id_trip";
    public static final  String COLUMN_TYPE = "type";
    public static final String TABLE_TRIPSTYPES = "tripstypes";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_TRIPSTYPES
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_IDTRIP + " text, "
            + COLUMN_TYPE + " text " +
            ");";

    public static final String[] FIELDS = {COLUMN_ID, COLUMN_IDTRIP,COLUMN_TYPE};

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPSTYPES);
        onCreate(database);

    }
}