package com.gmail.mazinva.RSSReaderSQL;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class AddChannelActivity extends Activity {
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_channel);
        Intent intent = getIntent();
        bundle = intent.getExtras();

        SQLiteAccessor helper = new SQLiteAccessor(this);
        helper.open();
        if(bundle != null && bundle.getInt("id") != 0){
            try {
                Cursor cursor = helper.fetchChannelById(bundle.getInt("id"));
                cursor.moveToNext();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAccessor.KEY_NAME_CHANNEL));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(SQLiteAccessor.KEY_URL_CHANNEL));
                ((TextView)findViewById(R.id.channelURL)).setText(url);
                ((TextView)findViewById(R.id.displayingChannelName)).setText(name);
            } catch (MySQLiteException e) {
                e.printStackTrace();
            }
        }

        (findViewById (R.id.addChannelButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = ((EditText)findViewById(R.id.channelURL)).getText().toString().trim();
                String name = ((EditText)findViewById(R.id.displayingChannelName)).getText().toString().trim();
                if("".equals(url)){
                    Toast.makeText(view.getContext(), R.string.emptyURL, Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    URL check = new URL(url);
                    check.toURI();
                } catch (MalformedURLException e) {
                    Toast.makeText(view.getContext(), R.string.malformedURL, Toast.LENGTH_LONG).show();
                    return;
                } catch (URISyntaxException e) {
                    Toast.makeText(view.getContext(), R.string.malformedURL, Toast.LENGTH_LONG).show();
                    return;
                }
                if("".equals(name)){
                    Toast.makeText(view.getContext(), R.string.emptyName, Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("url", url);
                intent.putExtra("name", name);
                if(bundle != null && bundle.getInt("id") != 0){
                    intent.putExtra("id", bundle.getInt("id"));
                }
                setResult(777, intent);
                finish();
            }
        });
    }
}
