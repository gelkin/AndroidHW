package com.gmail.mazinva.RSSReaderSQL;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class MainActivity extends Activity {
    SQLiteAccessor accessor;
    
    public static final String MY_ACTION_UPDATE = "com.gmail.mazinva.ACTION.UPDATE";
    
    public static final int EDIT_ID = Menu.FIRST;
    public static final int DELETE_ID = Menu.FIRST + 1;

    private void startAlarm(int millis){
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("task", "UPDATE");
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_NO_CREATE);
        if(pendingIntent == null){
            pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                      System.currentTimeMillis() + millis,
                                      millis,
                                      pendingIntent);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startAlarm(15 * 60 * 1000);

        accessor = new SQLiteAccessor(this);
        accessor.open();
        fetchData();

        ListView listView = (ListView)findViewById(R.id.channels);
        registerForContextMenu(listView);
        registerReceiver(new MyReceiver(), new IntentFilter(MainActivity.MY_ACTION_UPDATE));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), TitlesActivity.class);
                intent.putExtra("channelId", (int) l);
                startActivity(intent);
            }
        });

        (findViewById(R.id.addButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddChannelActivity.class);
                startActivityForResult(intent, 777);
            }
        });

        (findViewById(R.id.updateAllButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MyService.class);
                intent.putExtra("updateAll", true);
                startService(intent);
            }
        });

        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0, EDIT_ID, 0, R.string.editChannel);
                contextMenu.add(0, DELETE_ID, 0, R.string.deleteChannel);
            }
        });

    }

    @Override
    public boolean onMenuItemSelected (int featureId, MenuItem item){
        try {
            switch (item.getItemId()){
                case EDIT_ID:
                    Intent intent = new Intent(this, AddChannelActivity.class);
                    intent.putExtra("id", (int)((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).id);
                    startActivityForResult(intent, 777);
                    break;
                case DELETE_ID:
                    accessor.delete((int)((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).id);
                    break;
            }
        } catch (MySQLiteException e) {
            e.printStackTrace();
        }

        fetchData();
        return true;
    }

    private void fetchData() {
        try {
            Cursor cursor = accessor.fetchAllChannels();
            startManagingCursor(cursor);
            String[] from = new String[]{SQLiteAccessor.KEY_NAME_CHANNEL};
            int[] to = new int[]{R.id.channel};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.channel_item, cursor, from, to);
            ((ListView) findViewById(R.id.channels)).setAdapter(adapter);
        } catch (MySQLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        accessor.close();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            Bundle bundle = data.getExtras();
            try {
                if(bundle.getInt("id") == 0){
                    accessor.insertChannel(bundle.getString("name"), bundle.getString("url"));
                } else {
                    accessor.updateChannel(bundle.getInt("id"), bundle.getString("name"), bundle.getString("url"));
                }
                Intent intent = new Intent(this, MyService.class);
                intent.putExtra("task", "UPDATE");
                startService(intent);
            } catch (MySQLiteException e) {
                e.printStackTrace();
            }
            fetchData();
        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            boolean updateAll = bundle.getBoolean("updateAll");
            if(updateAll){
                Toast.makeText(getApplicationContext(), R.string.updateAllMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
