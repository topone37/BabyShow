package com.tp.bsclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
 * 首启页面
 */

public class StartActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //得到SharedPreferences 判断标志
                SharedPreferences sp = getSharedPreferences("app", MODE_PRIVATE);
                //判断 是不是第一次 默认是第一次启动
                if (sp.getBoolean("isFirst", true)) {
                    //如果是第一次使用，跳转到欢迎页
                    startActivity(new Intent(StartActivity.this, WelcomeActivity.class));
                    finish();
                } else {
                    SharedPreferences user_sp = getSharedPreferences("user", MODE_PRIVATE);
                    if (user_sp.contains("name") && user_sp.contains("pass")) {
                        String name = user_sp.getString("name", "");
                        String pass = user_sp.getString("pass", "");

                        if ("".equals(name) || "".equals(pass)) {
                            Toast.makeText(StartActivity.this, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            RequestParams params = new RequestParams();
                            params.addBodyParameter("uname", name);
                            params.addBodyParameter("upass", pass);
                            HttpUtils httpUtils = new HttpUtils();
                            httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "UserServlet?action=login", params, new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> responseInfo) {
                                    String result = responseInfo.result.trim();
                                    if (!"".equals(result)) {
                                        Toast.makeText(StartActivity.this, "登录成功 ", Toast.LENGTH_LONG).show();
                                        //登录成功 将 User存入全局变量中
                                        MyApp.users = (Users) GsonUtil.fromJson(result, Users.class);
                                        startActivity(new Intent(StartActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    //如果不是第一次 直接进入主界面
                                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                                    finish();
                                }
                            });

                        }
                    } else {
                        //如果不是第一次 直接进入主界面
                        startActivity(new Intent(StartActivity.this, LoginActivity.class));
                        finish();
                    }

                }
            }
        }, 100);


    }
}