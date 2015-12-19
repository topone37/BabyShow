package com.tp.bsclient.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tp.bsclient.R;

/**
 * Created by Administrator on 2015/11/27.
 */
public class FeedbackActivity extends Activity implements View.OnClickListener, TextWatcher {
    private LinearLayout ll_back_feedback;
    private Button btn_commit_feedback;
    private EditText edit_name;
    private EditText edit_email;
    private EditText edit_content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_feedback);
        initView();
    }

    private void initView() {
        ll_back_feedback = (LinearLayout) findViewById(R.id.ll_back_feedback);
        ll_back_feedback.setOnClickListener(this);
        btn_commit_feedback = (Button) findViewById(R.id.btn_commit_feedback);
        btn_commit_feedback.setOnClickListener(this);
        btn_commit_feedback.setActivated(false);
        btn_commit_feedback.setClickable(false);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_content = (EditText) findViewById(R.id.edit_content);
        edit_content.addTextChangedListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back_feedback:
                this.finish();
                break;
            default:
                Toast.makeText(FeedbackActivity.this, "反馈成功！", Toast.LENGTH_SHORT).show();
                this.finish();
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) { //监听按下事件
            View v = getCurrentFocus(); //得到当前获取焦点的View
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            //如果点击的是编辑框
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop); //获取当前编辑框的左上角
            int left = leftTop[0];
            int top = leftTop[1];

            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            /**
             * 确定了编辑框区域
             * */


            //判断焦点区域
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false; //不隐藏输入法
            } else {

                return true;
            }
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.v("tp", "beforeTextChanged >" + s);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.v("tp", "onTextChanged >" + s + s.length());
        if (s.length() > 15) {
            btn_commit_feedback.setActivated(true);
            btn_commit_feedback.setClickable(true);
        } else {
            btn_commit_feedback.setActivated(false);
            btn_commit_feedback.setClickable(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.v("tp", "afterTextChanged >" + s);
    }
}