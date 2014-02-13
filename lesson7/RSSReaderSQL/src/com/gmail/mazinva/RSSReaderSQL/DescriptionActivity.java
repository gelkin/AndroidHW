package com.gmail.mazinva.RSSReaderSQL;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.webkit.WebView;

public class DescriptionActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("id");
        SQLiteAccessor accessor = new SQLiteAccessor(this);
        accessor.open();
        try {
            Cursor cursor = accessor.fetchArticleById(id);
            cursor.moveToNext();
            String tmp;
            // Title:
            tmp = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAccessor.KEY_TITLE_ARTICLE));
            tmp = "<b>" + tmp + "</b>";
            ((WebView) findViewById(R.id.titleWebView)).loadData(tmp, "text/html;charset=UTF-8", null);

            // Description:
            tmp = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAccessor.KEY_DESC_ARTICLE));
            ((WebView) findViewById(R.id.descriptionWebView)).loadData(tmp, "text/html; charset=UTF-8", null);
        } catch (MySQLiteException e) {
            e.printStackTrace();
        }
    }
}
