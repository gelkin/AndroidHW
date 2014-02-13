package com.gmal.mazinva.Colloq2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SubjectsProvider {

    private DatabaseHelper helper;
    private SQLiteDatabase database;
    private Context context;

    static final String DATABASE_NAME = "Subjects";
    static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLE_SUBJECTS = "subjects";
    public static final String KEY_NAME_SUBJECT = "name";
    public static final String KEY_ID_SUBJECT = "_id";

    private static final String[] DATABASE_CREATE =
            {"CREATE TABLE " + DATABASE_TABLE_SUBJECTS + " (" + KEY_ID_SUBJECT + " INTEGER PRIMARY KEY AUTOINCREMENT, "
             + KEY_NAME_SUBJECT + " TEXT NOT NULL);"};
    
    public SubjectsProvider(Context context) {
        this.context = context;
    }

    public void open(){
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
    }

    public void close(){
        helper.close();
    }

    public Cursor fetchAllFeeds() {
        return database.query(DATABASE_TABLE_SUBJECTS, null, null, null, null, null, KEY_ID_SUBJECT + " asc", null);
    }

    public Cursor fetchSUBJECTById(int id) {
        return database.query(DATABASE_TABLE_SUBJECTS, null, KEY_ID_SUBJECT + "=" + (new Integer(id)).toString(), null, null, null, null);
    }

    public void insertSUBJECT(String name) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_SUBJECT, name);
        database.insert(DATABASE_TABLE_SUBJECTS, null, values);
    }

    public void updateSUBJECT(int id, String name) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_SUBJECT, name);
        database.update(DATABASE_TABLE_SUBJECTS, values, KEY_ID_SUBJECT + "=" + (new Integer(id)).toString(), null);
    }

    public void delete (int id) {
        database.delete(DATABASE_TABLE_SUBJECTS, KEY_ID_SUBJECT + "=?", new String[] {(new Integer(id)).toString()});
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            for(int i = 0; i < DATABASE_CREATE.length; ++i){
                Log.d("TABLE CREATE", DATABASE_CREATE[i]);
                db.execSQL(DATABASE_CREATE[i]);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SUBJECTS);
            onCreate(db);
        }
    }
}
