package com.manuelblanco.capstonestage2.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by manuel on 26/08/16.
 */
public class UpdateDB {

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_UPDATED = "last_updated";
    public static final String TABLE_UPDATED= "updated";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_UPDATED
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_UPDATED + " text " +
            ");";

    public static final String[] FIELDS = {COLUMN_ID, COLUMN_UPDATED};

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_UPDATED);
        onCreate(database);

    }
}
