package com.gmail.mazinva.RSSReaderService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;

public class TitlesActivity extends Activity {
    EventReceiver receiver;

    private class EventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            List<RSSItem> items = (List<RSSItem>) intent.getSerializableExtra("items");
            if (items == null) {
                Toast.makeText(getApplicationContext(), R.string.NothingToShow, 1).show();
            }
            showResult(items);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        receiver = new EventReceiver();
        IntentFilter filter = new IntentFilter("com.gmail.mazinva.ACTION.sendResult");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(receiver, filter);

        setContentView(R.layout.titles_listview);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("task", "SET_URL");
        intent.putExtra("url", url);

        startService(intent);
    }

    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void showResult(List<RSSItem> items) {
        ListView listView = (ListView) findViewById(R.id.listView);
        if (items != null) {
            MyArrayAdapter adapter = new MyArrayAdapter(this, items);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(adapterView.getContext(), DescriptionsActivity.class);
                    RSSItem item = (RSSItem) adapterView.getAdapter().getItem(i);
                    intent.putExtra("title", item.title);
                    intent.putExtra("description", item.description);
                    startActivity(intent);
                }
            });
        } else {
            finish();
        }
    }

    class MyArrayAdapter extends ArrayAdapter<RSSItem> {
        private Context context;
        List<RSSItem> items;

        public MyArrayAdapter(Context context, List<RSSItem> items) {
            super(context, R.layout.title_item);
            this.context = context;
            this.items = items;
        }

        @Override
        public RSSItem getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            RSSItem item = items.get(position);
            if (view == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.title_item, null);
            }
            TextView textView = (TextView) view.findViewById(R.id.title);
            if (item.title == null) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setText(item.title);
            }

            return view;
        }
    }
}
