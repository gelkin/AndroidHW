package com.gmail.mazinva.BestWeather;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteAccessor {

    private MyDBHelper helper;
    private SQLiteDatabase database;
    private final Context context;

    private static final String DATABASE_NAME = "BestWeatherDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String CITIES_TABLE = "cities";
    private static final String LAST_TABLE = "last";

    public static final String KEY_ID = "_id";
    public static final String KEY_CITY = "city";
    public static final String KEY_LAST = "last";

    private static final String CREATE_CITIES =
            "create table if not exists " + CITIES_TABLE + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + KEY_CITY + " text not null)";

    private static final String CREATE_LAST =
            "create table if not exists " + LAST_TABLE + " ("
                    + KEY_ID + " integer primary key autoincrement, "
                    + KEY_LAST + " integer not null)";

    private static final String REMOVE_CITIES =
            "drop table if exists " + CITIES_TABLE;

    private static final String REMOVE_LAST =
            "drop table if exists " + LAST_TABLE;


    public SQLiteAccessor(Context context) {
        this.context = context;
    }

    // TODO: checkout "init"
    private void init() {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_LAST, -1);
        database.insert(LAST_TABLE, null, initialValues);
    }

    public SQLiteAccessor open() throws SQLException {
        helper = new MyDBHelper(context);
        database = helper.getWritableDatabase();
        init();
        return this;
    }

    public void close() {
        database.close();
        helper.close();
    }

    public long addCity(String city) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CITY, city);
        return database.insert(CITIES_TABLE, null, initialValues);
    }

    // TODO: look down
/*
    public boolean updateCity(long rowID, String city) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_CITY, city);
        return database.update(CITIES_TABLE, newValues, KEY_ID + "=" + rowID, null) > 0;
    }
*/

    public boolean deleteCity(long rowID) {
        return database.delete(CITIES_TABLE, KEY_ID + "=" + rowID, null) > 0;
    }

    public Cursor fetchCities() {
        return database.query(CITIES_TABLE, new String[] {KEY_ID, KEY_CITY}, null, null, null, null, null);
    }

    public Cursor fetchCity(int cityID) {
        return database.query(CITIES_TABLE, new String[] {KEY_ID, KEY_CITY}, KEY_ID + "=" + cityID, null, null, null, null);
    }

    public boolean setLast(int cityID) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_LAST, cityID);
        return database.update(LAST_TABLE, newValues, null, null) > 0;
    }

    public Cursor getLast() {
        return database.query(LAST_TABLE, new String[] {KEY_LAST}, null, null, null, null, null);
    }

    private static class MyDBHelper extends SQLiteOpenHelper {

        MyDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_CITIES);
            db.execSQL(CREATE_LAST);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(REMOVE_CITIES);
            db.execSQL(REMOVE_LAST);
            onCreate(db);
        }
    }
}
