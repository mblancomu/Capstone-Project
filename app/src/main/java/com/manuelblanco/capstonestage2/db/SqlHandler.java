package com.manuelblanco.capstonestage2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.manuelblanco.capstonestage2.model.Country;
import com.manuelblanco.capstonestage2.model.Recommend;
import com.manuelblanco.capstonestage2.model.Route;
import com.manuelblanco.capstonestage2.model.Trip;
import com.manuelblanco.capstonestage2.model.TripsTypesItem;
import com.manuelblanco.capstonestage2.model.User;
import com.manuelblanco.capstonestage2.providers.ContractTripsProvider;
import com.manuelblanco.capstonestage2.utils.Constants;
import com.manuelblanco.capstonestage2.utils.StringsUtils;
import com.manuelblanco.capstonestage2.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class SqlHandler {

    static DataBaseHelper dbHelper;
    private static String TAG = SqlHandler.class.getSimpleName();
    public SQLiteDatabase sqLiteDatabase;
    private String imsiId = null;
    private Context context;

    public SqlHandler(Context context) {
        dbHelper = DataBaseHelper.getInstance(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.sqLiteDatabase = dbHelper.getWritableDatabase();
        this.context = context;
    }

    public static void populateTripsTypesDB() {

        try {

            String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS;

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    List<String> types = new ArrayList<>();
                    types = StringsUtils.convertStringToArrayList(cursor.getString(6));

                    for (int i = 0; i < types.size(); i++) {
                        TripsTypesItem tripsTypes = new TripsTypesItem();
                        tripsTypes.setIdTrip(cursor.getString(1));
                        tripsTypes.setType(types.get(i).toString());

                        putTripsTypes(tripsTypes);

                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public static List<String> getTypesByTripId(String idTrip) {
        List<String> types = new ArrayList<>();
        try {

            String selectQuery = "SELECT  * FROM " + TripsTypesDB.TABLE_TRIPSTYPES + " where id_trip='" + idTrip + "'";

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    types.add(cursor.getString(2));

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

        return types;
    }

    public static List<TripsTypesItem> getItemsForType(String types){
        List<TripsTypesItem> listTypes = new ArrayList<>();
        try {

            String selectQuery = "SELECT  * FROM " + TripsTypesDB.TABLE_TRIPSTYPES + " where type IN ('" + types + "')";

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    TripsTypesItem tripItem = new TripsTypesItem();
                    tripItem.setIdTrip(cursor.getString(1));
                    tripItem.setType(cursor.getString(2));

                    listTypes.add(tripItem);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

        return listTypes;

    }


    public static void putTripsTypes(TripsTypesItem tripsTypes) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TripsTypesDB.COLUMN_IDTRIP, tripsTypes.getIdTrip());
        values.put(TripsTypesDB.COLUMN_TYPE, tripsTypes.getType());
        db.insert(TripsTypesDB.TABLE_TRIPSTYPES, null, values);
    }

    public static void putUserState(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from "+ UserDB.TABLE_USER);
        ContentValues values = new ContentValues();
        values.put(UserDB.COLUMN_USER, user.getUsername());
        values.put(UserDB.COLUMN_STATE, user.getState());
        db.insert(UserDB.TABLE_USER, null, values);
    }

    public static User getUserState() {
        User user = new User();

        try {

            String selectQuery = "SELECT  * FROM " + UserDB.TABLE_USER;

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    user.setUsername(cursor.getString(1));
                    user.setState(cursor.getString(2));

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

        return user;
    }

    public static ArrayList<String> getAllCountries() {

        ArrayList<String> countries = null;

        try {

            String selectQuery = "SELECT  * FROM " + CountriesDB.TABLE_COUNTRIES;

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            countries = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    countries.add(cursor.getString(2));

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

        return countries;
    }

    public static ArrayList<Recommend> getAllRecommends() {

        ArrayList<Recommend> recommends = null;

        try {

            String selectQuery = "SELECT  * FROM " + RecommendsDB.TABLE_RECOMMENDS;

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            recommends = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {

                    Recommend recommend = new Recommend();

                    recommend.setIdTrip(cursor.getString(1));
                    recommend.setName(cursor.getString(2));
                    recommend.setPhone(cursor.getString(3));
                    recommend.setDescription(cursor.getString(4));
                    recommend.setCoords(cursor.getString(5));
                    recommend.setType(cursor.getString(6));
                    recommend.setWebsite(cursor.getString(7));
                    recommend.setResource(cursor.getString(8));

                    recommends.add(recommend);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

        return recommends;
    }

    public static ArrayList<Recommend> getRecommendsByType(String type, String idTrip) {

        ArrayList<Recommend> recommends = null;

        try {

            String selectQuery = "SELECT  * FROM " + RecommendsDB.TABLE_RECOMMENDS + " where type='" + type + "' AND id_trip='" + idTrip + "'";
            Log.e("TAG", "query: " + selectQuery);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            recommends = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {

                    Recommend recommend = new Recommend();

                    recommend.setIdTrip(cursor.getString(1));
                    recommend.setName(cursor.getString(2));
                    recommend.setPhone(cursor.getString(3));
                    recommend.setDescription(cursor.getString(4));
                    recommend.setCoords(cursor.getString(5));
                    recommend.setType(cursor.getString(6));
                    recommend.setWebsite(cursor.getString(7));
                    recommend.setResource(cursor.getString(8));

                    recommends.add(recommend);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

        return recommends;
    }

    public static List<Trip> getAllTrips() {

        List<Trip> trips = null;
        try {

            String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS;

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            trips = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {

                    Trip trip = new Trip();
                    trip.setId_trip(cursor.getString(1));
                    trip.setTitle(cursor.getString(3));
                    trip.setRoutes_id(cursor.getString(2));
                    trip.setCountry(cursor.getString(8));
                    trip.setUser(cursor.getString(10));
                    trip.setType(cursor.getString(6));
                    trip.setComments(cursor.getString(11));
                    trip.setCoords(cursor.getString(7));
                    trip.setDescription(cursor.getString(9));
                    trip.setPhoto(cursor.getString(5));
                    trip.setRate(cursor.getString(4));
                    trip.setHasRoute(cursor.getString(12));
                    trip.setContinent(cursor.getString(13));
                    trip.setFavorite(cursor.getString(14));
                    trip.setCreated(cursor.getString(15));
                    trip.setVotes(cursor.getString(16));
                    trip.setVoted(cursor.getString(17));
                    trip.setUpdated(cursor.getString(18));
                    trips.add(trip);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return trips;
    }

    public static Country getCountryName(String id) {
        Country country = null;

        String selectQuery = "SELECT  * FROM " + CountriesDB.TABLE_COUNTRIES + " where iso='" + id + "'";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        country = new Country();
        if (cursor != null) {

            cursor.moveToFirst();

            country.setIso(cursor.getString(1));
            country.setName(cursor.getString(2));

        }

        cursor.close();

        return country;
    }

    public static Country getCountryCode(String name) {
        Country country = null;

        String selectQuery = "SELECT  * FROM " + CountriesDB.TABLE_COUNTRIES + " where name='" + name + "'";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        country = new Country();
        if (cursor != null) {

            cursor.moveToFirst();

            country.setIso(cursor.getString(1));
            country.setName(cursor.getString(2));

        }

        cursor.close();

        return country;
    }

    public static int getCountryTrips(String id) {
        String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where country='" + id + "'";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;

        if (cursor != null) {

            count = cursor.getCount();

        }

        cursor.close();

        return count;
    }

    public static int getCountTrips() {
        String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;

        if (cursor != null) {

            count = cursor.getCount();

        }

        cursor.close();

        return count;
    }

    public static int getCountTripsTypes() {
        String selectQuery = "SELECT  * FROM " + TripsTypesDB.TABLE_TRIPSTYPES;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;

        if (cursor != null) {

            count = cursor.getCount();

        }

        cursor.close();

        return count;
    }

    public static int getFavoritesTrips() {
        String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where favorite=true";

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;

        if (cursor != null) {

            count = cursor.getCount();

        }

        cursor.close();

        return count;
    }

    public static List<Trip> getAllFavorites() {

        List<Trip> trips = null;
        try {

            String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where favorite= 'true'";

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            trips = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {

                    Trip trip = new Trip();
                    trip.setId_trip(cursor.getString(1));
                    trip.setTitle(cursor.getString(3));
                    trip.setRoutes_id(cursor.getString(2));
                    trip.setCountry(cursor.getString(8));
                    trip.setUser(cursor.getString(10));
                    trip.setType(cursor.getString(6));
                    trip.setComments(cursor.getString(11));
                    trip.setCoords(cursor.getString(7));
                    trip.setDescription(cursor.getString(9));
                    trip.setPhoto(cursor.getString(5));
                    trip.setRate(cursor.getString(4));
                    trip.setHasRoute(cursor.getString(12));
                    trip.setContinent(cursor.getString(13));
                    trip.setFavorite(cursor.getString(14));
                    trip.setCreated(cursor.getString(15));
                    trip.setVotes(cursor.getString(16));
                    trip.setVoted(cursor.getString(17));
                    trip.setUpdated(cursor.getString(18));
                    trips.add(trip);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return trips;
    }

    public static List<Trip> getTripsByCountry(String id) {

        List<Trip> trips = null;
        try {

            String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where country='" + id + "'";

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            trips = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {

                    Trip trip = new Trip();
                    trip.setId_trip(cursor.getString(1));
                    trip.setTitle(cursor.getString(3));
                    trip.setRoutes_id(cursor.getString(2));
                    trip.setCountry(cursor.getString(8));
                    trip.setUser(cursor.getString(10));
                    trip.setType(cursor.getString(6));
                    trip.setComments(cursor.getString(11));
                    trip.setCoords(cursor.getString(7));
                    trip.setDescription(cursor.getString(9));
                    trip.setPhoto(cursor.getString(5));
                    trip.setRate(cursor.getString(4));
                    trip.setHasRoute(cursor.getString(12));
                    trip.setContinent(cursor.getString(13));
                    trip.setFavorite(cursor.getString(14));
                    trip.setCreated(cursor.getString(15));
                    trip.setVotes(cursor.getString(16));
                    trip.setVoted(cursor.getString(17));
                    trip.setUpdated(cursor.getString(18));
                    trips.add(trip);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return trips;
    }

    public static List<Trip> getTripsByTypeForMarkers(List<TripsTypesItem> types, String area) {
        ArrayList<String> tripsSelected = new ArrayList<>();
        for (TripsTypesItem tripItem: types){
            tripsSelected.add(tripItem.getIdTrip());
        }

        String tripsTypes = Utils.typesForSqlite(tripsSelected);

        List<Trip> trips = null;
        try {

            String selectQuery;

            if (area.equals("*")) {
                selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where id_trip IN ('" + tripsTypes + "')";
            } else {
                selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where id_trip IN ('" + tripsTypes + "') AND country='" + area + "'";
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            trips = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {

                    Trip trip = new Trip();
                    trip.setId_trip(cursor.getString(1));
                    trip.setTitle(cursor.getString(3));
                    trip.setRoutes_id(cursor.getString(2));
                    trip.setCountry(cursor.getString(8));
                    trip.setUser(cursor.getString(10));
                    trip.setType(cursor.getString(6));
                    trip.setComments(cursor.getString(11));
                    trip.setCoords(cursor.getString(7));
                    trip.setDescription(cursor.getString(9));
                    trip.setPhoto(cursor.getString(5));
                    trip.setRate(cursor.getString(4));
                    trip.setHasRoute(cursor.getString(12));
                    trip.setContinent(cursor.getString(13));
                    trip.setFavorite(cursor.getString(14));
                    trip.setCreated(cursor.getString(15));
                    trip.setVotes(cursor.getString(16));
                    trip.setVoted(cursor.getString(17));
                    trip.setUpdated(cursor.getString(18));
                    trips.add(trip);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return trips;
    }

    public static List<Trip> getTripsByType(String type, String area) {

        List<Trip> trips = null;
        try {

            String selectQuery;

            if (area.equals("*")) {
                selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where type='" + type + "'";
            } else {
                selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where type IN " + type + " AND country='" + area + "'";
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            trips = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {

                    Trip trip = new Trip();
                    trip.setId_trip(cursor.getString(1));
                    trip.setTitle(cursor.getString(3));
                    trip.setRoutes_id(cursor.getString(2));
                    trip.setCountry(cursor.getString(8));
                    trip.setUser(cursor.getString(10));
                    trip.setType(cursor.getString(6));
                    trip.setComments(cursor.getString(11));
                    trip.setCoords(cursor.getString(7));
                    trip.setDescription(cursor.getString(9));
                    trip.setPhoto(cursor.getString(5));
                    trip.setRate(cursor.getString(4));
                    trip.setHasRoute(cursor.getString(12));
                    trip.setContinent(cursor.getString(13));
                    trip.setFavorite(cursor.getString(14));
                    trip.setCreated(cursor.getString(15));
                    trip.setVotes(cursor.getString(16));
                    trip.setVoted(cursor.getString(17));
                    trip.setUpdated(cursor.getString(18));
                    trips.add(trip);

                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return trips;
    }

    public static Trip getTrip(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Trip trip = null;

        String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where id_trip='" + id + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {

            cursor.moveToFirst();

            trip = new Trip();
            trip.setId_trip(cursor.getString(1));
            trip.setTitle(cursor.getString(3));
            trip.setRoutes_id(cursor.getString(2));
            trip.setCountry(cursor.getString(8));
            trip.setUser(cursor.getString(10));
            trip.setType(cursor.getString(6));
            trip.setComments(cursor.getString(11));
            trip.setCoords(cursor.getString(7));
            trip.setDescription(cursor.getString(9));
            trip.setPhoto(cursor.getString(5));
            trip.setRate(cursor.getString(4));
            trip.setHasRoute(cursor.getString(12));
            trip.setContinent(cursor.getString(13));
            trip.setFavorite(cursor.getString(14));
            trip.setCreated(cursor.getString(15));
            trip.setVotes(cursor.getString(16));
            trip.setVoted(cursor.getString(17));
            trip.setUpdated(cursor.getString(18));

        }

        cursor.close();

        return trip;
    }

    public static Country getCountry(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Country country = null;

        Cursor cursor = db.query(CountriesDB.TABLE_COUNTRIES, new String[]{CountriesDB.COLUMN_ISO,
                }, CountriesDB.COLUMN_ISO + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {

            cursor.moveToFirst();

            country = new Country();
            country.setIso(cursor.getString(1));
            country.setName(cursor.getString(2));
        }

        cursor.close();

        return country;
    }

    public static String getPreviousVote(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String previousVote = "";
        String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where id_trip='" + id + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {

            cursor.moveToFirst();

            previousVote = cursor.getString(16);
        }

        cursor.close();

        return previousVote;
    }

    public static String getIdTrip(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String idTrip = "";
        String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where _id='" + id + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {

            cursor.moveToFirst();

            idTrip = cursor.getString(1);
        }

        cursor.close();

        return idTrip;
    }

    public static String getFavoriteState(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String previousVote = "";
        String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where id_trip='" + id + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {

            cursor.moveToFirst();

            previousVote = cursor.getString(14);
        }

        cursor.close();

        return previousVote;
    }

    public static ArrayList<String> getCountriesByRegion(String region) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> countries = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where continent='" + region + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {

                    countries.add(cursor.getString(8));

                } while (cursor.moveToNext());
            }
        }

        cursor.close();

        return countries;
    }

    public static ArrayList<String> getTripsName() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<String> tripsName = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {

                    tripsName.add(cursor.getString(3));

                } while (cursor.moveToNext());
            }
        }

        cursor.close();

        return tripsName;
    }

    public static String getVotedState(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String previousVote = "";
        String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where id_trip='" + id + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {

            cursor.moveToFirst();

            previousVote = cursor.getString(17);
        }

        cursor.close();

        return previousVote;
    }

    public static String getRate(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String rate = "";
        String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where id_trip='" + id + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {

            cursor.moveToFirst();

            rate = cursor.getString(4);
        }

        cursor.close();

        return rate;
    }

    public static int getNumberOfTypes(String type) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int types = 0;
        String selectQuery = "SELECT  * FROM " + ContractTripsProvider.TABLE_TRIPS + " where type='" + type + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {

            cursor.moveToFirst();

            types = cursor.getCount();
        }

        cursor.close();

        return types;
    }

    public static String getLastUpdated() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String lastUpdated = "";
        String selectQuery = "SELECT  * FROM " + UpdateDB.TABLE_UPDATED;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {

            cursor.moveToFirst();

            lastUpdated = cursor.getString(1);
        }

        cursor.close();

        return lastUpdated;
    }

    /* * Verify if exist a register. This method is duplicate more late, but with differents params.
     * We can remove this method more later or remain here like a auxiliar method.*/
    public static boolean verification(String _username, String TABLE_NAME, String KEY_USERNAME) throws SQLException {
        int count = -1;
        SQLiteDatabase sqldb = dbHelper.getReadableDatabase();
        Cursor c = null;
        try {
            String query = "SELECT COUNT(*) FROM "
                    + TABLE_NAME + " WHERE " + KEY_USERNAME + " = ?";
            c = sqldb.rawQuery(query, new String[]{_username});
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            return count > 0;
        } finally {
            if (c != null) {
                c.close();
            }
        }
    }

    // Verify if exist a item. With this method check all rows ofDB for find the item or not.
    public static boolean checkidExitsorNot(String tablename, String rowname, String id) {
        String queryf = "select * from " + tablename + " where " + rowname + "='" + id + "'";
        SQLiteDatabase sqldb = dbHelper.getReadableDatabase();
        Cursor c = sqldb.rawQuery(queryf, null);
        if (c.getCount() == 0) {
            c.close();
            sqldb.close();
            return false;
        } else {
            c.close();
            sqldb.close();
            return true;
        }
    }

    /**
     * Method for execute a specific query in a DB. Insert a query string, and this is executed.
     *
     * @param query
     */


    public void executeQuery(String query) {
        try {
            if (sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }

            sqLiteDatabase = dbHelper.getWritableDatabase();
            sqLiteDatabase.execSQL(query);
        } catch (Exception e) {
            System.out.println("DATABASE ERROR " + e);
        }
    }

    /**
     * General method for all queries in the DB.This select a particular query, but isnt used in the
     * app because exist a specific query for each table.
     *
     * @param query
     * @return
     */


    public Cursor selectQuery(String query) {
        Cursor c1 = null;
        try {
            if (sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
            sqLiteDatabase = dbHelper.getWritableDatabase();
            c1 = sqLiteDatabase.rawQuery(query, null);

        } catch (Exception e) {
            System.out.println("DATABASE ERROR " + e);
        }
        return c1;
    }

    public void putTrip(Trip trip) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_IDTRIP, trip.getId_trip());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_TITLE, trip.getTitle());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_ROUTESID, trip.getRoutes_id());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_TYPE, trip.getType());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_COUNTRY, trip.getCountry());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_USER, trip.getUser());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_COORDS, trip.getCoords());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_IMAGE, trip.getPhoto());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_DESC, trip.getDescription());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_RATE, trip.getRate());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_HASROUTES, trip.getHasRoute());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_COMMENTS, trip.getComments());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_CONTINENT, trip.getContinent());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_FAVORITE, trip.getFavorite());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_CREATED, trip.getCreated());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_VOTES, trip.getVotes());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_VOTED, trip.getVoted());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_UPDATED, trip.getUpdated());
        db.insert(ContractTripsProvider.TABLE_TRIPS, null, values);
        db.close();
    }

    public static void putListTrip(ArrayList<Trip> trips) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Trip trip : trips) {
            ContentValues values = new ContentValues();
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_IDTRIP, trip.getId_trip());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_ROUTESID, trip.getRoutes_id());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_TITLE, trip.getTitle());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_RATE, trip.getRate());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_IMAGE, trip.getPhoto());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_TYPE, trip.getType());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_COORDS, trip.getCoords());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_COUNTRY, trip.getCountry());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_DESC, trip.getDescription());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_USER, trip.getUser());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_HASROUTES, trip.getHasRoute());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_COMMENTS, trip.getComments());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_CONTINENT, trip.getContinent());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_FAVORITE, trip.getFavorite());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_CREATED, trip.getCreated());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_VOTES, trip.getVotes());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_VOTED, trip.getVoted());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_UPDATED, trip.getUpdated());
            db.insert(ContractTripsProvider.TABLE_TRIPS, null, values);
        }
    }

    public static void putListRecommend(ArrayList<Recommend> recommends) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Recommend recommend : recommends) {
            ContentValues values = new ContentValues();

            values.put(RecommendsDB.COLUMN_IDTRIP, recommend.getIdTrip());
            values.put(RecommendsDB.COLUMN_NAME, recommend.getName());
            values.put(RecommendsDB.COLUMN_PHONE, recommend.getPhone());
            values.put(RecommendsDB.COLUMN_COORDS, recommend.getCoords());
            values.put(RecommendsDB.COLUMN_DESCRIPTION, recommend.getDescription());
            values.put(RecommendsDB.COLUMN_TYPE, recommend.getType());
            values.put(RecommendsDB.COLUMN_WEBSITE, recommend.getWebsite());
            values.put(RecommendsDB.COLUMN_RESOURCE, recommend.getResource());
            db.insert(RecommendsDB.TABLE_RECOMMENDS, null, values);
        }
    }

    public static void putLastUpdate(String date) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UpdateDB.COLUMN_UPDATED, date);
        db.insert(UpdateDB.TABLE_UPDATED, null, values);
    }

    public static void updateListTrip(ArrayList<Trip> trips) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (Trip trip : trips) {
            ContentValues values = new ContentValues();
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_IDTRIP, trip.getId_trip());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_ROUTESID, trip.getRoutes_id());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_TITLE, trip.getTitle());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_RATE, trip.getRate());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_IMAGE, trip.getPhoto());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_TYPE, trip.getType());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_COORDS, trip.getCoords());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_COUNTRY, trip.getCountry());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_DESC, trip.getDescription());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_USER, trip.getUser());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_HASROUTES, trip.getHasRoute());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_COMMENTS, trip.getComments());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_CONTINENT, trip.getContinent());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_FAVORITE, trip.getFavorite());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_CREATED, trip.getCreated());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_VOTES, trip.getVotes());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_VOTED, trip.getVoted());
            values.put(ContractTripsProvider.ColumnsTrips.COLUMN_UPDATED, trip.getUpdated());
            db.update(ContractTripsProvider.TABLE_TRIPS, values, ContractTripsProvider.ColumnsTrips.COLUMN_IDTRIP + " = ?",
                    new String[]{String.valueOf(trip.getId_trip())});
        }
    }

    public static void updateLastUpdate(String date) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UpdateDB.COLUMN_UPDATED, date);
        db.update(UpdateDB.TABLE_UPDATED, values, null, null);
    }

    public static void updateFavoriteTrip(Trip trip, String isFavorite) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_FAVORITE, isFavorite);
        db.update(ContractTripsProvider.TABLE_TRIPS, values, ContractTripsProvider.ColumnsTrips.COLUMN_IDTRIP + " = ?",
                new String[]{String.valueOf(trip.getId_trip())});
    }

    public static void updateRateTrip(Trip trip) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_VOTES, trip.getVotes());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_RATE, trip.getRate());
        values.put(ContractTripsProvider.ColumnsTrips.COLUMN_VOTED, trip.getVoted());
        db.update(ContractTripsProvider.TABLE_TRIPS, values, ContractTripsProvider.ColumnsTrips.COLUMN_IDTRIP + " = ?",
                new String[]{String.valueOf(trip.getId_trip())});
    }

    public static void clearTable(String table) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(table, null, null);
    }

    public void closeDDBB() {
        sqLiteDatabase.close();
    }

    // For verify if exist a table in the DB. Pass the name of the table and open this DB.

    public boolean isTableExists(String tableName, boolean openDb) {
        if (openDb) {
            if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
                sqLiteDatabase = dbHelper.getReadableDatabase();
            }

            if (!sqLiteDatabase.isReadOnly()) {
                sqLiteDatabase.close();
                sqLiteDatabase = dbHelper.getReadableDatabase();
            }
        }

        Cursor cursor = sqLiteDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public static ArrayList<String> getColumnList(String column, String table, int columnNumber) {
        String countQuery = "SELECT * FROM '" + table + "' GROUP BY " + column;
        ArrayList<String> countries = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor.moveToFirst()) {

            do {

                countries.add(cursor.getString(columnNumber));

            } while (cursor.moveToNext());
        }
        cursor.close();

        return countries;

    }

    public static int getColumnCount(String column, String table, int columnNumber) {
        String countQuery = "SELECT count('" + column + "') FROM '" + table + "' GROUP BY '" + column + "' ORDER BY count('" + column + "') DESC";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;

    }

    public static int getCount(String table) {
        String countQuery = "SELECT * FROM " + table;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
}
