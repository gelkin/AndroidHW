package com.gmail.mazinva.RSSReaderService;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class DescriptionsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);
        Bundle bundle = getIntent().getExtras();

        String tmp;
        WebView titleWebView = (WebView) findViewById(R.id.titleWebView);
        tmp = bundle.getString("title");
        if (tmp != null) {
            tmp = "<b>" + tmp + "</b>";
            titleWebView.loadData(tmp, "text/html;charset=UTF-8", null);
        }

        WebView descriptionWebView = (WebView) findViewById(R.id.descriptionWebView);
        tmp = bundle.getString("description");
        if (tmp != null) {
            descriptionWebView.loadData(tmp, "text/html;charset=UTF-8", null);
        }
    }
}
