package com.manuelblanco.capstonestage2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private String name;
    private static DataBaseHelper sInstance;
    private static String TAG = DataBaseHelper.class.getSimpleName();

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
        this.name = name;
    }

    public static synchronized DataBaseHelper getInstance(Context context, String name, SQLiteDatabase.CursorFactory factory,
                                                          int version) {

        if (sInstance == null) {
            sInstance = new DataBaseHelper(context, name, factory, version);

        }
        return sInstance;
    }

   /* private DataBaseHelper(Context context,String database, int dataversion) {
        super(context, database, null, dataversion);
    }*/

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        TripsDB.onCreate(sqLiteDatabase);
        CountriesDB.onCreate(sqLiteDatabase);
        RecommendsDB.onCreate(sqLiteDatabase);
        UpdateDB.onCreate(sqLiteDatabase);
        CountriesISO.loadCountriesISO(sqLiteDatabase);
        TripsTypesDB.onCreate(sqLiteDatabase);
        UserDB.onCreate(sqLiteDatabase);
        TypesDB.onCreate(sqLiteDatabase);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        TripsDB.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        CountriesDB.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        RecommendsDB.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        UpdateDB.onUpgrade(sqLiteDatabase, oldVersion, newVersion);

    }

}
