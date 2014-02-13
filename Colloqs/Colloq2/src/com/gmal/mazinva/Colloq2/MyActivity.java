package com.gmal.mazinva.Colloq2;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MyActivity extends Activity {

    SubjectsProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        provider = new SubjectsProvider(this);
        fillData();
    }

    public void fillData() {
        try {
            Cursor cursor = provider.fetchAllFeeds();
            startManagingCursor(cursor);
            String[] from = new String[]{SubjectsProvider.KEY_NAME_SUBJECT};
            int[] to = new int[]{R.id.textView};
            SimpleCursorAdapter adapter =
                    new SimpleCursorAdapter(this, R.layout.listrow, cursor, from, to);
            ((ListView)findViewById(R.id.listView)).setAdapter(adapter);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }
}
