package com.tp.bsclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.tp.bsclient.R;

/**
 * Created by Administrator on 2015/11/27.
 */
public class MoreActivity extends Activity implements View.OnClickListener {
    private LinearLayout ll_back_more;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //去掉标题栏
        setContentView(R.layout.activity_more);
        initView();

    }

    private void initView() {
        ll_back_more = (LinearLayout) findViewById(R.id.ll_back_more);
        ll_back_more.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back_more:
                this.finish();
                break;
            default:
                break;
        }
    }

}