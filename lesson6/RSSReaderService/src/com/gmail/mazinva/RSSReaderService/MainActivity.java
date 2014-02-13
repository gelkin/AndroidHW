package com.gmail.mazinva.RSSReaderService;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

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
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("task", "UPDATE");

        // Updating each 60 seconds
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60000, 60000, pendingIntent);


        Button button = (Button) findViewById(R.id.button);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                                                R.layout.url_item,
                                                                сhannels);
        ListView listView = (ListView) findViewById(R.id.channels);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), TitlesActivity.class);
                String text = (String) adapterView.getAdapter().getItem(i);
                intent.putExtra("url", text);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), TitlesActivity.class);
                String text = ((EditText) findViewById(R.id.editText)).getText().toString();
                intent.putExtra("url", text);
                startActivity(intent);
            }
        });
    }
}
