package com.example.WebSearch;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

public class TranslationActivity extends Activity {
    TextView textOut;
    URL url;
    String text;
    String calling;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translation_images);
        calling = "https://translate.yandex.net/api/v1.5/tr.json/translate?lang=en-ru&key=" +
                  "trnsl.1.1.20131003T083136Z.93467e8308f15ba0.6f7c26deb3b324d5cf803d43513497cb571b4891&text=";
        textOut = (TextView) findViewById(R.id.textTranslation);
        text = getIntent().getStringExtra("str");
        calling += text;
        new Task().execute(text);
    }

    private class Task extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(calling);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.connect();
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                String current = buffReader.readLine();
                int n = current.length();
                current = current.substring(36, n - 3);
                return current;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null) {
                result = "NO TRANSLATION. SORRY";
            }
            textOut.setText(text + "  ->(ru) " + result);
            /*
            findViewById(R.id.showImagesButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ShowImagesActivity.class);
                    intent.putExtra("enText", text);
                    System.out.println("TRRRASNSLATION:::::: " + text);
                    startActivity(intent);
                }
            });
            */
        }
    }
}