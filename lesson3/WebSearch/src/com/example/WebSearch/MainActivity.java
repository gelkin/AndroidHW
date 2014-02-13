package com.example.WebSearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
    Intent intent;
    EditText source;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        intent = new Intent(this, TranslationActivity.class);
        source = (EditText) findViewById(R.id.editText);

        (findViewById(R.id.translateButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(source.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Текст не может быть пустым", Toast.LENGTH_SHORT).show();
                    return;
                }

                System.out.println((source.getText().toString()));

                intent.putExtra("str", source.getText().toString());
                startActivity(intent);
            }
        });
    }


}
