package com.tp.bsclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.tp.bsclient.R;

/**
 * Created by Administrator on 2015/11/23.
 */
public class AboutActivity extends Activity {
    private LinearLayout ll_back_about;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_about);
        ll_back_about = (LinearLayout) findViewById(R.id.ll_back_about);
        ll_back_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}