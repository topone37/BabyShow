package com.tp.bsclient.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tp.bsclient.R;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.po.Users;
import com.tp.bsclient.util.GsonUtil;
import com.tp.bsclient.util.UrlConst;

/**
 * Created by Administrator on 2015/11/6.
 */
public class LoginActivity extends Activity {
    private EditText edit_uname;
    private EditText edit_upass;
    private ProgressDialog pd_login;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_login);
        initView();
    }


    public void initView() {
        this.edit_uname = (EditText) findViewById(R.id.edit_uname);
        this.edit_upass = (EditText) findViewById(R.id.edit_upass);

        // 设置对话框信息
        this.pd_login = new ProgressDialog(this);
        this.pd_login.setMessage("正在验证用户名和密码...");
    }


    public void login(View v) {
        //登录操作
        //取出用户输入的用户名和密码
        String strName = this.edit_uname.getText().toString().trim();
        String strPass = this.edit_upass.getText().toString().trim();
        if ("".equals(strName) || "".equals(strPass)) {
            Toast.makeText(LoginActivity.this, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            this.pd_login.show();
            RequestParams params = new RequestParams();
            params.addBodyParameter("uname", strName);
            params.addBodyParameter("upass", strPass);
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "UserServlet?action=login", params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    pd_login.dismiss();
                    String result = responseInfo.result.trim();
                    if (!"".equals(result)) {
                        Toast.makeText(LoginActivity.this, "登录成功 ", Toast.LENGTH_LONG).show();
                        //登录成功 将 User存入全局变量中
                        MyApp.users = (Users) GsonUtil.fromJson(result, Users.class);
                        //第一次登录过后将 用户名 密码存入

                        SharedPreferences sp = getSharedPreferences("user", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("name", MyApp.users.getUname());
                        editor.putString("pass", MyApp.users.getUpass());
                        editor.commit();
                        //直接跳转到主界面
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "用户名或者密码不正确", Toast.LENGTH_LONG).show();

                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    pd_login.dismiss();
                    Toast.makeText(LoginActivity.this, "网络异常" + e.toString(), Toast.LENGTH_LONG).show();

                }
            });


        }

    }


    public void goRegist(View v) {
        //跳转到注册页面
        startActivity(new Intent(LoginActivity.this, RegistActivity.class));
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
}