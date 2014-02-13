package com.example.WebSearch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ShowImagesActivity extends Activity {

    private Context context;
    private ArrayList<ImageView> imageViews;
    private MyAdapter myAdapter;

    private static final int NUMBER = 10;
    private static final int IMG_PER_PAGE = 8;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translation_images);

        ListView listView = (ListView) findViewById(R.id.images);
        myAdapter = new MyAdapter(this, R.layout.list_item);
        listView.setAdapter(myAdapter);

        String text = getIntent().getStringExtra("text");
        TextView textView = (TextView) findViewById(R.id.textTranslation);
        textView.setText(text);

        imageViews = new ArrayList<ImageView>();
        context = this;
        String enText = getIntent().getStringExtra("enText");
        new ImageURLsFetcher().execute(enText);
    }

    private class ImageURLsFetcher extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            String[] imageURLs = new String[NUMBER];

            int head = 0;
            int offset = 0;
            while (head < NUMBER) {
                try {
                    String text = URLEncoder.encode(params[0], "utf-8");
                    URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
                                              "v=1.0&q=" + text + "&rsz=" + IMG_PER_PAGE +
                                              "&start=" + offset + "&imgsz=small");
                    URLConnection connection = url.openConnection();
                    String line;
                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    JSONObject json = new JSONObject(builder.toString());
                    JSONArray resArray = json.getJSONObject("responseData").getJSONArray("results");
                    String s;
                    for (int i = 0; i < resArray.length() && head < NUMBER; i++) {
                        s = resArray.getJSONObject(i).getString("url");
                        URLConnection imgConnection = (new URL(s)).openConnection();
                        imgConnection.setConnectTimeout(500);
                        imageURLs[head] = s;
                        head++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                offset += IMG_PER_PAGE;
            }
            return imageURLs;
        }

        @Override
        protected void onPostExecute(String[] imageURLs) {
            for (int i = 0; i < imageURLs.length; i++) {
                new ImageDownloader().execute(imageURLs[i]);
            }
        }
    }

    private class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                return (BitmapFactory.decodeStream(new URL(params[0]).openConnection().getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(bitmap);
            imageViews.add(imageView);
            myAdapter.add(imageView);
        }

    }

    private class MyAdapter extends ArrayAdapter<ImageView> {
        public MyAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            return imageViews.get(position);
        }
    }

}
