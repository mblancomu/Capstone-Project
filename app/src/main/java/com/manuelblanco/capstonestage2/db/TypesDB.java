package com.manuelblanco.capstonestage2.db;

import android.database.sqlite.SQLiteDatabase;

public class TypesDB {

    public static final String COLUMN_ID = "_id";
    public static final  String COLUMN_TYPE = "type";
    public static final String TABLE_TYPES = "types";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_TYPES
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TYPE + " text " +
            ");";

    public static final String[] FIELDS = {COLUMN_ID,COLUMN_TYPE};

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPES);
        onCreate(database);

    }
}