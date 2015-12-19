package com.tp.bsclient.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tp.bsclient.R;
import com.tp.bsclient.adapter.FriendsAdapter;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.util.UrlConst;

import org.json.JSONArray;
import org.json.JSONException;

public class AddFriendsActivity extends Activity implements View.OnClickListener, EditText.OnEditorActionListener, AdapterView.OnItemClickListener {

    private EditText edit_search;
    private Button btn_search;
    private ProgressDialog pd_login;
    private LinearLayout ll_back_addFriends;
    private ListView lv_search;
    private FriendsAdapter adapter;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfriends);
        initView();
        initData();
    }

    private void initView() {
        // 设置对话框信息
        this.pd_login = new ProgressDialog(this);
        this.pd_login.setMessage("正在添加好友...");

        this.edit_search = (EditText) findViewById(R.id.edit_search);

        this.btn_search = (Button) findViewById(R.id.btn_search);
        this.btn_search.setOnClickListener(this);

        this.ll_back_addFriends = (LinearLayout) findViewById(R.id.ll_back_addFriends);
        this.ll_back_addFriends.setOnClickListener(this);

        this.lv_search = (ListView) findViewById(R.id.lv_search);
        this.lv_search.setOnItemClickListener(this);

    }

    private void initData() {
        adapter = new FriendsAdapter(AddFriendsActivity.this, new JSONArray());
        this.lv_search.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                //启动线程 搜索好友
                String key = edit_search.getText().toString().trim();
                if ("".equals(key)) {
                    Toast.makeText(AddFriendsActivity.this, "请输入关键字进行搜索!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    searchFriends(key);
                }
                break;
            case R.id.ll_back_addFriends:
                finish();
                break;
            default:
                break;
        }
    }


    private void addFriends(String fid) {
        if ("".equals(fid)) {
            Toast.makeText(AddFriendsActivity.this, "请输入好友ID", Toast.LENGTH_SHORT).show();
            return;
        }
        this.pd_login.show();
        if (MyApp.users == null) {
            Toast.makeText(AddFriendsActivity.this, "你已经处于离线状态，请重新登录！", Toast.LENGTH_SHORT).show();
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

    private void searchFriends(String keyword) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("keyword", keyword);
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "FriendsServlet?action=queryByKey", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    JSONArray array = new JSONArray(responseInfo.result);
                    adapter.removeAll();
                    adapter.addData(array);
                } catch (JSONException e) {
                    Toast.makeText(AddFriendsActivity.this, "好友信息获取异常!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(AddFriendsActivity.this, "网络异常!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String fid = ((TextView) view.findViewById(R.id.f_uid)).getText().toString();
        //弹出对话框 是否加对方为好友
        String fname = ((TextView) view.findViewById(R.id.f_nickname)).getText().toString();
        showDialog(fid, fname);
    }

    private void showDialog(final String fid, String fname) {
        LayoutInflater layout = LayoutInflater.from(AddFriendsActivity.this);
        View mView = layout.inflate(R.layout.dialog_exit, null);
        dialog = new AlertDialog.Builder(AddFriendsActivity.this).create();
        dialog.setCanceledOnTouchOutside(true);
        ((TextView) mView.findViewById(R.id.tv_title)).setText("添加好友");
        ((TextView) mView.findViewById(R.id.tv_message)).setText("你确定要添加" + fname + "为好友么?");
        Button btn_cancel = (Button) mView.findViewById(R.id.btn_cancel_dialog);
        Button btn_confirm = (Button) mView.findViewById(R.id.btn_confirm_dialog);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addFriends(fid);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int x = dm.widthPixels;// 屏幕的宽
        int y = dm.heightPixels;// 屏幕的高
        dialog = new Dialog(this, R.style.dialog_exit);
        dialog.getWindow().setLayout((int) (x / 2.2), (int) (y / 1.8));
        dialog.show();
        dialog.setContentView(mView);
        dialog.setCancelable(false);
    }
}
