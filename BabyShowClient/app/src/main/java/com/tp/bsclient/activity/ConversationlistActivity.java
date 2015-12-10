package com.tp.bsclient.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.tp.bsclient.R;

public class ConversationlistActivity extends FragmentActivity {

    private LinearLayout ll_back_conversationlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle("聊天中...");
        setContentView(R.layout.activity_conversationlist);
        ll_back_conversationlist = (LinearLayout) findViewById(R.id.ll_back_conversationlist);
        ll_back_conversationlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
