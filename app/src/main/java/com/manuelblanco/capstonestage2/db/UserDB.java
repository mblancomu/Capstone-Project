package com.manuelblanco.capstonestage2.db;

import android.database.sqlite.SQLiteDatabase;

public class UserDB {

    public static final String COLUMN_ID = "_id";
    public static final  String COLUMN_USER = "username";
    public static final  String COLUMN_STATE = "state";
    public static final String TABLE_USER = "user";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_USER
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_USER + " text, "
            + COLUMN_STATE + " text " +
            ");";

    public static final String[] FIELDS = {COLUMN_ID,COLUMN_USER,COLUMN_STATE};

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(database);

    }
}