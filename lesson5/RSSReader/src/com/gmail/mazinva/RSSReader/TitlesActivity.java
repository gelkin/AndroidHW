package com.gmail.mazinva.RSSReader;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TitlesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.titles_listview);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        if (url == null) {
            Toast.makeText(getApplicationContext(), "URL address is empty", 1).show();
        } else {
            RSSFetcher fetcher = new RSSFetcher();
            fetcher.execute(url);
        }
    }

    private class RSSFetcher extends AsyncTask<String, Void, List<RSSItem>> {
        private static final int KILOBYTE = 1000;
        private String cause;

        @Override
        protected List<RSSItem> doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                inputStream = (new URL(strings[0])).openStream();
                String encoding;
                byte[] buf = new byte[KILOBYTE];
                int count = KILOBYTE;
                for (int i = 0; i < KILOBYTE; i++) {
                    int ch = inputStream.read();
                    if (ch == -1) {
                        count = i;
                        break;
                    }
                    buf[i] = (byte) ch;
                }

                byte[] newBuf = new byte[count];
                System.arraycopy(buf, 0, newBuf, 0, count);
                String s = new String(newBuf);
                Pattern pattern = Pattern.compile("encoding=\"(.*?)\"");

                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    encoding = matcher.group(1);
                } else {
                    encoding = null;
                }

                MySAXParser parser = new MySAXParser(new SequenceInputStream(new ByteArrayInputStream(newBuf),
                                                                             inputStream),
                                                     encoding);
                List<RSSItem> items = parser.parse();
                return items;

            } catch (MalformedURLException e) {
                cause = "Wrong URL";
                return null;

            } catch (SAXException e) {
                cause = "RSS-channel is lost, empty or contains errors";
                return null;
            } catch (UnsupportedEncodingException e) {
                cause = "RSS-channel is lost, empty or contains errors";
                return null;
            } catch (IOException e) {
                cause = "Channel reading error";
                return null;
            } catch (ParserConfigurationException e) {
                cause = "Channel reading error";
                return null;
            } catch (Exception e) {
                cause = "Channel reading error";
                return null;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(List<RSSItem> items) {
            if (getCause() != null) {
                Toast.makeText(getApplicationContext(), getCause(), 1).show();
            } else {
                showTitles(items);
            }
        }

        public String getCause() {
            return cause;
        }
    }

    void showTitles(List<RSSItem> items) {
        ListView listView = (ListView) findViewById(R.id.listView);
        if (items != null) {
            MyArrayAdapter adapter = new MyArrayAdapter(TitlesActivity.this, items);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(adapterView.getContext(), DescriptionsActivity.class);
                    RSSItem item = (RSSItem) adapterView.getAdapter().getItem(i);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("description", item.getDescription());
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Nothing to show =(", 1).show();
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
            if (item.getTitle() == null) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setText(item.getTitle());
            }

            return view;
        }
    }
}
