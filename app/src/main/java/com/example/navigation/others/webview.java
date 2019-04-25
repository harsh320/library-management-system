package com.example.navigation.others;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.navigation.R;

public class webview extends AppCompatActivity {
String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Intent intent = getIntent();
        name = intent.getStringExtra("EXTRA_MESSAGE");
        WebView mywebview = (WebView) findViewById(R.id.webView);
        mywebview.loadUrl(name);
        mywebview.setWebViewClient(new webclient());
    }
}
