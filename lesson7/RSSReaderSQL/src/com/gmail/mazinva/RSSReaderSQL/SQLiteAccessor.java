package com.gmail.mazinva.RSSReaderSQL;

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

    private static final String DATABASE_NAME = "MyRSSDatabase";

    private static final String DATABASE_TABLE_CHANNELS = "channels";
    private static final String DATABASE_TABLE_ARTICLES = "articles";

    public static final String KEY_URL_CHANNEL = "url";
    public static final String KEY_NAME_CHANNEL = "name";
    public static final String KEY_ID_CHANNEL = "_id";
    public static final String KEY_TIME_CHANNEL = "time";

    public static final String KEY_ID_ARTICLE = "_id";
    public static final String KEY_ID_CHANNEL_ARTICLE = "id_channel";
    public static final String KEY_TIME_RECEIVE_ARTICLE = "received";
    public static final String KEY_TITLE_ARTICLE = "title";
    public static final String KEY_DESC_ARTICLE = "description";

    private static final int DATABASE_VERSION = 1;

    private static final String[] DATABASE_CREATE =
            {"CREATE TABLE " + DATABASE_TABLE_CHANNELS +
             " (" + KEY_ID_CHANNEL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             KEY_URL_CHANNEL + " TEXT NOT NULL, " + KEY_NAME_CHANNEL + " TEXT NOT NULL, " +
             KEY_TIME_CHANNEL + " INTEGER);",

             "CREATE TABLE " + DATABASE_TABLE_ARTICLES +
             " (" + KEY_ID_ARTICLE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
             KEY_ID_CHANNEL_ARTICLE + " NOT NULL, " + KEY_TIME_RECEIVE_ARTICLE + " INTEGER, " +
             KEY_TITLE_ARTICLE + " TEXT, " + KEY_DESC_ARTICLE +" TEXT)"
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

    public Cursor fetchAllChannels() throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        return database.query(DATABASE_TABLE_CHANNELS, null, null, null, null, null, KEY_ID_CHANNEL + " asc", null);
    }

    public Cursor fetchChannelById(int id) throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        return database.query(DATABASE_TABLE_CHANNELS, null,
                              KEY_ID_CHANNEL + "=" + id, null, null, null, null);
    }

    public Cursor fetchArticleById(int id) throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        return database.query(DATABASE_TABLE_ARTICLES, null, KEY_ID_ARTICLE + "=" + id, null, null, null, null);
    }

    public Cursor fetchArticlesByChannelId(int channelId) throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        return database.query(DATABASE_TABLE_ARTICLES, null, KEY_ID_CHANNEL_ARTICLE + "=" + channelId,
                              null, null, null, KEY_ID_ARTICLE + " asc", null);
    }

    public void insertChannel(String name, String url) throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_CHANNEL, name);
        values.put(KEY_URL_CHANNEL, url);
        values.put(KEY_TIME_CHANNEL, (new Integer((int) (System.currentTimeMillis()/1000))).toString());
        database.insert(DATABASE_TABLE_CHANNELS, null, values);
    }

    public void updateChannel(int id, String name, String url) throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_CHANNEL, name);
        values.put(KEY_URL_CHANNEL, url);
        values.put(KEY_TIME_CHANNEL, (new Integer((int)(System.currentTimeMillis()/1000))).toString());
        database.update(DATABASE_TABLE_CHANNELS, values, KEY_ID_CHANNEL + "=" + id, null);
    }

    public void insertArticle(int channelId, String title, String description) throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        ContentValues values = new ContentValues();
        values.put(KEY_ID_CHANNEL_ARTICLE, channelId);
        values.put(KEY_TITLE_ARTICLE, title);
        values.put(KEY_DESC_ARTICLE, description);
        values.put(KEY_TIME_RECEIVE_ARTICLE, (new Integer((int)(System.currentTimeMillis()/1000))).toString());
        database.insert(DATABASE_TABLE_ARTICLES, null, values);
    }

    public void deleteArticles(int channelId) throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        database.delete(DATABASE_TABLE_ARTICLES, KEY_ID_CHANNEL_ARTICLE + "=" + channelId, null);
    }

    public void delete(int id) throws MySQLiteException {
        if(!isOpened){
            throw new MySQLiteException(context.getString(R.string.dbIsNotOpened));
        }
        database.delete(DATABASE_TABLE_CHANNELS, KEY_ID_CHANNEL + "=?", new String[]{(new Integer(id)).toString()});
        database.delete(DATABASE_TABLE_ARTICLES, KEY_ID_CHANNEL_ARTICLE + "=" + id, null);
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
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_ARTICLES);
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_CHANNELS);
            onCreate(db);
        }
    }
}
