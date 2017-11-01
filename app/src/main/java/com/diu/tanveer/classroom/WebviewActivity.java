package com.diu.tanveer.classroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

public class WebviewActivity extends AppCompatActivity {

    WebView wb;
    private Toolbar mSignInToolbar;
    //EditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);


        wb = (WebView)findViewById(R.id.web_view);
        wb.setWebViewClient(new WebViewClient());   // force open this browser
        WebSettings webSettings = wb.getSettings(); // Enable Settings for browser
        webSettings.setJavaScriptEnabled(true);     // Enable javascript

        wb.loadUrl("https://www.facebook.com/groups/DIU.CSE.42th.secA/");

        mSignInToolbar = (Toolbar) findViewById(R.id.signInToolbar);
        setSupportActionBar(mSignInToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void onBackPressed() {
        if(wb.canGoBack()){
            wb.goBack();
            //address.setText(wb.getUrl().toString());
        }
        else
            super.onBackPressed();
    }

    public void Search(View view){
        //wb.loadUrl(address.getText().toString());
    }
}
