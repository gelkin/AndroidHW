package com.gmail.mazinva.BestWeather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.URLEncoder;

public class AddCityActivity extends Activity {

    EditText cityName;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city);

        context = this;
        cityName= (EditText) findViewById(R.id.cityName);

        (findViewById(R.id.findButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(cityName.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                new FindCitiesTask().execute(cityName.getText().toString());
            }
        });


        ListView listView = (ListView)findViewById(R.id.citiesToAdd);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);

                // TODO: add new city to db and send further its name

                intent.putExtra("channelId", (int) l);
                startActivity(intent);
            }
        });
    }

    private class FindCitiesTask extends AsyncTask<String, Void, String> {

        String[] cities;
        String chosenCity = "";

        private static final String API_KEY = "9r8bamvx7utesvcyxb993ggm";
        private static final String API_URL =
                "http://api.worldweatheronline.com/free/v1/search.ashx?format=json&num_of_results=20&key=" + API_KEY;

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = API_URL + "&q=" + URLEncoder.encode(params[0]);
                HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(url));
                HttpEntity httpEntity = httpResponse.getEntity();
                String json = EntityUtils.toString(httpEntity, "UTF-8");

                String result = "";
                JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
                if (object.has("search_api")) {
                    json = object.getString("search_api");
                    object = (JSONObject) new JSONTokener(json).nextValue();
                    JSONArray array = new JSONArray(object.getString("result"));
                    for (int id = 0; id < array.length(); id++) {
                        object = array.getJSONObject(id);
                        if (id > 0) {
                            result += "|";
                        }
                        String city = new JSONArray(object.getString("areaName")).getJSONObject(0).getString("value");
                        String region = object.has("region") ? (", " + new JSONArray(object.getString("region")).getJSONObject(0).getString("value")) : "";
                        String country = object.has("country") ? (", " + new JSONArray(object.getString("country")).getJSONObject(0).getString("value")) : "";
                        result += city + region + country;
                    }
                }

                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            TextView state = (TextView) findViewById(R.id.addState);
            if (result == null) {
                state.setText(R.string.searchError);
            } else
            if ("".equals(result)) {
                state.setText(R.string.searchEmpty);
            } else {
                state.setText("");
                cities = result.split("\\|");
                ListView listView = (ListView) findViewById(R.id.citiesToAdd);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                                                                        android.R.layout.simple_list_item_2,
                                                                        cities);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        chosenCity = cities[position];
                        SQLiteAccessor accessor = new SQLiteAccessor(context);
                        accessor.open();
                        accessor.addCity(chosenCity);
                        accessor.close();

                        fin();
                    }
                });
            }
        }
    }

    private void fin() {
        finish();
    }
}
