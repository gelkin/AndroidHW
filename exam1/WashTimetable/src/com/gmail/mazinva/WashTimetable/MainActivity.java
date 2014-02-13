package com.gmail.mazinva.WashTimetable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


// TODO: Shared pref

public class MainActivity extends Activity {

    EditText name;
    EditText boxNumber;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        name = (EditText) findViewById(R.id.name);
        boxNumber = (Ed2itText) findViewById(R.id.boxNumber);

        name.setText("Мойка у дяди Васи");
        boxNumber.setText("3");

        context = this;

        (findViewById(R.id.setButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(name.getText().toString()) || "".equals(boxNumber.getText().toString()) ) {
                    Toast.makeText(context, "Name and box number cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(view.getContext(), OrderListActivity.class);
                    intent.putExtra("name", name.getText().toString());
                    intent.putExtra("boxNUmber", boxNumber.getText().toString());
                    startActivity(intent);
                }
            }
        });

        /*
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){

            Intent intent = new Intent(this, TutorialOne.class);
            startActivity(intent);
            finish();
        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }
        */

    }
}
