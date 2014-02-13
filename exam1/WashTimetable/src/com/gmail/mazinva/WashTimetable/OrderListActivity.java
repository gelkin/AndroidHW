package com.gmail.mazinva.WashTimetable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OrderListActivity extends Activity {

    private String name;
    private int boxNumber;
    private Order[] orders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("name");
        boxNumber = Integer.parseInt(bundle.getString("boxNumber"));

        (findViewById(R.id.addOrderButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddOrderActivity.class);
                // intent.putExtra("boxNUmber", boxNumber.getText().toString());
                startActivity(intent);
            }
        });
    }
}
