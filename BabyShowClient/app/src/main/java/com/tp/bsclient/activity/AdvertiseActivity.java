package com.tp.bsclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import com.tp.bsclient.R;

/**
 * Created by Administrator on 2015/11/28.
 */
public class AdvertiseActivity extends Activity {

    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_advertime);

        Intent intent = getIntent();//得到活动详情的地址
        String url = intent.getStringExtra("_url");
        webView = (WebView) findViewById(R.id.web_advertise);
        webView.loadUrl(url);

    }
}