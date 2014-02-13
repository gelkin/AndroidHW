package com.gmail.mazinva.WashTimetable;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Font;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AddOrderActivity extends Activity {
    SQLiteAccessor accessor;

    ArrayList<Integer> times = new ArrayList<Integer>();
    ArrayList<String> timesFormed = new ArrayList<String>();

    EditText brand;
    EditText color;
    EditText telephone;
    EditText time;

    Spinner spinner;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_order);

        accessor = new SQLiteAccessor(this);
        accessor.open();

        try {
            Cursor cursor = accessor.fetchAllTimes();
            while(cursor.moveToNext()) {
                times.add(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteAccessor.KEY_TIME_TIMES)));
            }
            Collections.sort(times);
            for (int i = 0; i < times.size(); i++) {
                timesFormed.add(Order.timeToString(times.get(i)));
            }
        } catch (MySQLiteException e) {
            e.printStackTrace();
        }

        context = this;

        brand = (EditText) findViewById(R.id.brand);
        color = (EditText) findViewById(R.id.color);
        telephone = (EditText) findViewById(R.id.telNumber);
        time = (EditText) findViewById(R.id.time);

        spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, timesFormed);
        spinner.setAdapter(adapter);

        ArrayAdapter<String> array = (ArrayAdapter) spinner.getAdapter();
        // Remove selected element in the current spinner from adapter
        array.remove();
        // Set adapter again
        spinner.setAdapter(array);

        (findViewById(R.id.addButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(brand.getText().toString()) ||
                    "".equals(color.getText().toString()) ||
                    "".equals(telephone.getText().toString()) ||
                    "".equals(time.getText().toString())) {

                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    new AddOrderTask().execute(new Order(brand.getText().toString(),
                                                         color.getText().toString(),
                                                         telephone.getText().toString(),
                                                         time.getText().toString()))
                }
            }
        });
    }

    private class AddOrderTask extends AsyncTask<Order, Void, Order> {

        @Override
        protected Order doInBackground(Order... params) {
            try {


                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Order result) {

        }
    }


    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent,
                                   View view, int pos, long id) {
            ArrayAdapter<String> array = (ArrayAdapter) spinner.getAdapter();
            int boxNumber = accessor.deleteTimeById((int) id);
            array.remove(id);
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }


}