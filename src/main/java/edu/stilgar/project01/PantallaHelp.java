package edu.stilgar.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class PantallaHelp extends AppCompatActivity {

    WebView helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_help);
        helper = (WebView) findViewById(R.id.helpWeb);
        helper.loadUrl("file:///android_asset/helper.html");
    }
}
