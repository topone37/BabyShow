package com.tp.bsclient.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tp.bsclient.R;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.util.UrlConst;

public class AddFriendsActivity extends Activity implements View.OnClickListener, EditText.OnEditorActionListener {

    private EditText edti_friendId;
    private Button btn_addFriends;
    private ProgressDialog pd_login;
    private LinearLayout ll_back_addFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriends);
        initView();
    }

    private void initView() {
        // 设置对话框信息
        this.pd_login = new ProgressDialog(this);
        this.pd_login.setMessage("正在添加好友...");
        this.edti_friendId = (EditText) findViewById(R.id.edit_friendid);
        this.btn_addFriends = (Button) findViewById(R.id.btn_add_friends);
        this.btn_addFriends.setOnClickListener(this);
        this.edti_friendId.setOnEditorActionListener(this);
        this.ll_back_addFriends = (LinearLayout) findViewById(R.id.ll_back_addFriends);
        this.ll_back_addFriends.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_friends:
                addFriends();
                break;
            case R.id.ll_back_addFriends:
                finish();
                break;
            default:
                break;
        }
    }


    private void addFriends() {
        String fid = edti_friendId.getText().toString().trim();
        if ("".equals(fid)) {
            Toast.makeText(AddFriendsActivity.this, "请输入好友ID", Toast.LENGTH_SHORT).show();
            return;
        }
        this.pd_login.show();
        if (MyApp.users == null) {
            Toast.makeText(AddFriendsActivity.this, "添加好友 ，状态，请重新登录！", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams params = new RequestParams();
        params.addBodyParameter("id1", MyApp.users.getUid() + "");
        params.addBodyParameter("id2", fid);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "FriendsServlet?action=add", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                pd_login.dismiss();
                String result = responseInfo.result.trim();
                if ("1".equals(result)) {
                    Toast.makeText(AddFriendsActivity.this, "添加成功 ", Toast.LENGTH_LONG).show();
                    //直接跳转到主界面
                    startActivity(new Intent(AddFriendsActivity.this, MainActivity.class));
                    finish();
                } else if ("0".equals(result)) {
                    Toast.makeText(AddFriendsActivity.this, "好友已经存在", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddFriendsActivity.this, "好友添加失败", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                pd_login.dismiss();
                Toast.makeText(AddFriendsActivity.this, "网络异常" + e.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == 0 || actionId == 3 && event != null) {
            Toast.makeText(AddFriendsActivity.this, "搜索好友!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
