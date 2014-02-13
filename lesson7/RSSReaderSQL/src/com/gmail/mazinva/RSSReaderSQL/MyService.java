package com.gmail.mazinva.RSSReaderSQL;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.util.List;

public class MyService extends IntentService {

    private MySAXParser parser = new MySAXParser();
    SQLiteAccessor accessor;

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        accessor = new SQLiteAccessor(this);
        accessor.open();

        Bundle bundle = intent.getExtras();
        boolean updateAll = bundle.getBoolean("updateAll");
        boolean singleUpdate = bundle.getBoolean("singleUpdate");

        Cursor cursor;
        try {
            if (singleUpdate) {
                cursor = accessor.fetchChannelById(bundle.getInt("channelId"));
                try {
                    cursor.moveToFirst();
                    updateSingleChannel(cursor);

                    Intent broadcastIntent = new Intent(MainActivity.MY_ACTION_UPDATE);
                    broadcastIntent.putExtra("singleUpdate", true);
                    sendBroadcast(broadcastIntent);
                }  finally {
                    cursor.close();
                }
            } else {
                cursor = accessor.fetchAllChannels();
                try {
                    while(cursor.moveToNext()){
                        updateSingleChannel(cursor);
                    }
                    if (updateAll) {
                        Intent broadcastIntent = new Intent(MainActivity.MY_ACTION_UPDATE);
                        broadcastIntent.putExtra("updateAll", true);
                        sendBroadcast(broadcastIntent);
                    }
                } finally {
                    cursor.close();
                }
            }

        } catch (MySQLiteException e) {
            e.printStackTrace();
        }
    }

    private void updateSingleChannel(Cursor cursor) throws MySQLiteException {
        String url = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAccessor.KEY_URL_CHANNEL));
        int channelId = cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteAccessor.KEY_ID_CHANNEL));
        List<RSSItem> items;
        try {
            items = parser.parse((new URL(url).openStream()));
            accessor.deleteArticles(channelId);
            for (int i = 0; i < items.size(); i++) {
                accessor.insertArticle(channelId, items.get(i).title, items.get(i).description);
            }

            Intent broadcastIntent = new Intent(MainActivity.MY_ACTION_UPDATE);
            broadcastIntent.putExtra("channelId", channelId);
            sendBroadcast(broadcastIntent);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

