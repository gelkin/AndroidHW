package com.gmail.mazinva.BestWeather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        helper = new WeatherDbOpenHelper(this);
        table = new WeatherTable(helper.getWritableDatabase());

        (findViewById(R.id.addCityButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddCityActivity.class);
                startActivity(intent);
            }
        });

        (findViewById(R.id.refreshAllButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MyService.class);
                intent.putExtra("refreshAll", true);
                startService(intent);
            }
        });

        ListView listView = (ListView) findViewById(R.id.citiesToAdd);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                                                android.R.layout.simple_list_item_2,
                                                                R.id.cities);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), WeatherActivity.class);
                intent.putExtra("cityId", true);
                startActivity(intent);
            }
        });
    }

}
