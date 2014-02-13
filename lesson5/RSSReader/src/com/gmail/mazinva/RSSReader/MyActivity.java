package com.gmail.mazinva.RSSReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;


public class MyActivity extends Activity {

    public static final String[] сhannels = {
            "http://lenta.ru/rss",
            "http://stackoverflow.com/feeds/tag/android",
            "http://habrahabr.ru/rss",
            "http://bash.im/rss"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button button = (Button) findViewById(R.id.button);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                                                R.layout.url_item,
                                                                сhannels);
        ListView listView = (ListView) findViewById(R.id.channels);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str = (String) adapterView.getAdapter().getItem(i);
                ((EditText) findViewById(R.id.editText)).setText(str, TextView.BufferType.EDITABLE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = ((EditText) findViewById(R.id.editText)).getText().toString();
                Intent intent = new Intent(view.getContext(), TitlesActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }
}

