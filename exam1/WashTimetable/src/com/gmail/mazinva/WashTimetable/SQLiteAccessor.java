package com.gmail.mazinva.WashTimetable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SQLiteAccessor {

    private MyDBHelper helper;
    private SQLiteDatabase database;
    private Context context;
    private boolean isOpened;

    private static final String DATABASE_NAME = "MyWashTimetableDB";

    private static final String DATABASE_TABLE_ORDERS = "orders";
    private static final String DATABASE_TABLE_TIMES = "times";

    public static final String KEY_BRAND_ORDER = "brand";
    public static final String KEY_COLOR_ORDER = "name";
    public static final String KEY_TELEPHONE_ORDER = "tel";
    public static final String KEY_TIME_ORDER = "time";
    public static final String KEY_ID_ORDER = "_id";

    // BOX stored as string. Example: "1234" - time accesseable from  boxes 1, 2, 3, 4
    public static final String KEY_BOX_TIMES = "brand";
    public static final String KEY_TIME_TIMES = "time";
    public static final String KEY_ID_TIMES = "_id";

    private static final int DATABASE_VERSION = 1;

    private static final String[] DATABASE_CREATE =
            {"CREATE TABLE " + DATABASE_TABLE_ORDERS +
                    " (" + KEY_ID_ORDER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_BRAND_ORDER + " TEXT NOT NULL, " + KEY_COLOR_ORDER + " TEXT NOT NULL, " +
                    KEY_TELEPHONE_ORDER + " TEXT NOT NULL, " + KEY_TIME_ORDER + " INTEGER);",

                    "CREATE TABLE " + DATABASE_TABLE_TIMES +
                            " (" + KEY_ID_TIMES + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_TIME_TIMES + " INTEGER, " + KEY_BOX_TIMES + " TEXT NOT NULL, "
            };

    public SQLiteAccessor(Context context){
        this.context = context;
        isOpened = false;
    }

    public void open(){
        helper = new MyDBHelper(context);
        database = helper.getWritableDatabase();
        isOpened = true;
    }

    public void close(){
        isOpened = false;
        helper.close();
    }

    public Cursor fetchAllOrders() throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        return database.query(DATABASE_TABLE_ORDERS, null, null, null, null, null, null, null);
    }

    public Cursor fetchAllTimes() throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        return database.query(DATABASE_TABLE_TIMES, null, null, null, null, null, null, null);
    }

    public Cursor fetchOrderById(int id) throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        return database.query(DATABASE_TABLE_ORDERS, null,
                              KEY_ID_ORDER + "=" + id, null, null, null, null);
    }

    public void addOrder(String brand, String color, String telephone, int time) throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        ContentValues values = new ContentValues();
        values.put(KEY_BRAND_ORDER, brand);
        values.put(KEY_COLOR_ORDER, color);
        values.put(KEY_TELEPHONE_ORDER, telephone);
        values.put(KEY_TIME_ORDER, time);
        database.insert(DATABASE_TABLE_ORDERS, null, values);
    }

    public void updateOrder(int id, String name, String url) throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_ORDER, name);
        values.put(KEY_URL_ORDER, url);
        values.put(KEY_TIME_ORDER, (new Integer((int)(System.currentTimeMillis()/1000))).toString());
        database.update(DATABASE_TABLE_ORDERS, values, KEY_ID_ORDER + "=" + id, null);
    }

    public int deleteTimeById(int id) throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        database.fetchTimeById(DATABASE_TABLE_TIMES, KEY_ID_ORDER + "=?", new String[]{(new Integer(id)).toString()});
    }

    public Cursor fetchTimeById(int time) throws MySQLiteException {
        if(!isOpened) {
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        database.query(DATABASE_TABLE_TIMES, null,
                                KEY_TIME_TIMES + "=" + time, null, null, null, null);
    }

    public void delete(int id) throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        database.delete(DATABASE_TABLE_ORDERS, KEY_ID_ORDER + "=?", new String[]{(new Integer(id)).toString()});
    }

    private static class MyDBHelper extends SQLiteOpenHelper {

        MyDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for(int i = 0; i < DATABASE_CREATE.length; i++){
                db.execSQL(DATABASE_CREATE[i]);
                Log.d("TABLE CREATED", DATABASE_CREATE[i]);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ORDERS);
            onCreate(db);
        }
    }
}

