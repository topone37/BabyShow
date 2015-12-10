package com.tp.bsclient.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tp.bsclient.R;
import com.tp.bsclient.util.UrlConst;

/**
 * Created by Administrator on 2015/11/17.
 */
public class RegistActivity extends Activity implements View.OnClickListener {
    private LinearLayout ll_back_regist;
    private EditText edit_name;
    private EditText edit_pass;
    private ProgressDialog pd_login;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_regist);
        initView();
    }

    private void initView() {
        ll_back_regist = (LinearLayout) findViewById(R.id.ll_back_regist);
        ll_back_regist.setOnClickListener(this);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_pass = (EditText) findViewById(R.id.edit_pass);
        // 设置对话框信息
        this.pd_login = new ProgressDialog(this);
        this.pd_login.setMessage("注册中，请稍候...");
    }


    public void regist(View v) {

        String username = edit_name.getText().toString().trim();
        String password = edit_pass.getText().toString().trim();

        //拿到用户名和密码
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("name", username);
        params.addBodyParameter("pass", password);

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "UserServlet?action=regist", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                pd_login.dismiss();
                String result = responseInfo.result;
                //
                if ("1".equals(result)) {
                    //
                    Toast.makeText(RegistActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(RegistActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                pd_login.dismiss();
                Toast.makeText(RegistActivity.this, "网络异常" + s, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back_regist:
                this.finish();
                break;
            default:
                break;
        }
    }


}