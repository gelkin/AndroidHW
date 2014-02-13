package com.gmail.mazinva.RSSReaderSQL;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class TitlesActivity extends Activity {
    SQLiteAccessor accessor;
    int channelId;


    private BroadcastReceiver channelUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int neededId = bundle.getInt("channelId");
            if (neededId == channelId) {
                fetchData(channelId);
            }
            boolean singleUpdate = bundle.getBoolean("singleUpdate");
            if (singleUpdate) {
                Toast.makeText(getApplicationContext(), R.string.singleUpdateMsg, Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.titles_listview);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        accessor = new SQLiteAccessor(this);
        accessor.open();
        channelId = bundle.getInt("channelId");

        fetchData(channelId);

        (findViewById(R.id.titlesUpdateButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MyService.class);
                intent.putExtra("singleUpdate", true);
                intent.putExtra("channelId", channelId);
                startService(intent);
            }
        });

        ((ListView) findViewById(R.id.titles)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), DescriptionActivity.class);
                intent.putExtra("id", (int) l);
                startActivity(intent);
            }
        });


    }

    private void fetchData(int channelId) {
        try {
            Cursor cursor = accessor.fetchArticlesByChannelId(channelId);
            startManagingCursor(cursor);
            String[] from = new String[]{SQLiteAccessor.KEY_TITLE_ARTICLE};
            int[] to = new int[]{R.id.title};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.title_item, cursor, from, to);
            ((ListView) findViewById(R.id.titles)).setAdapter(adapter);
        } catch (MySQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(channelUpdateReceiver, new IntentFilter(MainActivity.MY_ACTION_UPDATE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(channelUpdateReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessor.close();
    }

}
