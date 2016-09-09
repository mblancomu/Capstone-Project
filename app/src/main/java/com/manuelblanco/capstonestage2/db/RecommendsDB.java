package com.manuelblanco.capstonestage2.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by manuel on 18/08/16.
 */
public class RecommendsDB {

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IDTRIP = "id_trip";
    public static final  String COLUMN_NAME = "name";
    public static final  String COLUMN_PHONE = "phone";
    public static final  String COLUMN_DESCRIPTION = "description";
    public static final  String COLUMN_COORDS = "coords";
    public static final  String COLUMN_TYPE = "type";
    public static final  String COLUMN_WEBSITE = "website";
    public static final  String COLUMN_RESOURCE = "resource";
    public static final String TABLE_RECOMMENDS = "recommends";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_RECOMMENDS
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_IDTRIP + " text, "
            + COLUMN_NAME + " text, "
            + COLUMN_PHONE + " text, "
            + COLUMN_DESCRIPTION + " text, "
            + COLUMN_COORDS + " text, "
            + COLUMN_TYPE + " text, "
            + COLUMN_WEBSITE + " text, "
            + COLUMN_RESOURCE + " text " +
            ");";

    public static final String[] FIELDS = {COLUMN_ID, COLUMN_IDTRIP,COLUMN_NAME,COLUMN_PHONE,COLUMN_DESCRIPTION,
    COLUMN_COORDS,COLUMN_TYPE,COLUMN_WEBSITE,COLUMN_RESOURCE};

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECOMMENDS);
        onCreate(database);

    }


}
