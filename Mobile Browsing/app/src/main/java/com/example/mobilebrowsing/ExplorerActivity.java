package com.example.mobilebrowsing;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;

public class ExplorerActivity extends AppCompatActivity {
    WebView webView;
    WebSettings webSettings;

    EditText inputSearch;
    ImageView searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eplorer);

        webView = findViewById(R.id.explorer_web_view);
        inputSearch = findViewById(R.id.header_input);
        searchButton = findViewById(R.id.header_search);

        searchButton.setOnClickListener(handleSearchWebView);

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    View.OnClickListener handleSearchWebView = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!inputSearch.getText().toString().isEmpty()) {
                webView.loadUrl(inputSearch.getText().toString());
            }
        }
    };
}