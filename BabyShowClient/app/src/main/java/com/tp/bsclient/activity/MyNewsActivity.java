package com.tp.bsclient.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tp.bsclient.R;
import com.tp.bsclient.adapter.NewsAdapter;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.util.GsonUtil;
import com.tp.bsclient.util.UrlConst;
import com.tp.bsclient.vo.News;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/23.
 */
public class MyNewsActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    int x;// 屏幕的宽
    int y;// 屏幕的高
    private LinearLayout ll_back;
    private ListView lv_mynews;
    private List<News> data;
    private Dialog dialog;
    private String nid;//动态ID

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_mynews);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        x = dm.widthPixels;// 屏幕的宽
        y = dm.heightPixels;// 屏幕的高
        initView(); //初始化视图
        initData();//初始化数据
    }

    private void initView() {
        this.ll_back = (LinearLayout) findViewById(R.id.ll_back_mynews);
        this.ll_back.setOnClickListener(this);

        this.lv_mynews = (ListView) findViewById(R.id.lv_mynews);
        //设置上点击监听器
        lv_mynews.setOnItemClickListener(this);
        lv_mynews.setOnItemLongClickListener(this);
    }

    private void initData() {
        if (MyApp.users == null) {
            Toast.makeText(MyNewsActivity.this, "你已经处于离线状态,请重新登录！", Toast.LENGTH_SHORT).show();
            return;
        }
        //初始化数据
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("uid", MyApp.users.getUid() + "");
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "NewsServlet?action=getNewsByUid", params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //每次访问网络拉取数据
                Type type = new TypeToken<List<News>>() {
                }.getType();
                data = new ArrayList<>(); //初始化空间
                data = (List<News>) GsonUtil.fromJson(responseInfo.result, type); //获取数据
                //将数据装载到适配器上
                NewsAdapter adapter = new NewsAdapter(MyNewsActivity.this, data);
                //设置适配器
                lv_mynews.setAdapter(adapter);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(MyNewsActivity.this, "网络异常,请稍后重试！", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back_mynews:
                this.finish();
                break;
            case R.id.btn_del:
                dialog.dismiss();
                View mView = getLayoutInflater().inflate(R.layout.dialog_exit, null);
                ((TextView) mView.findViewById(R.id.tv_title)).setText("删除动态");
                ((TextView) mView.findViewById(R.id.tv_message)).setText("你确定删除这个动态么？");
                Button btn_ok = (Button) mView.findViewById(R.id.btn_confirm_dialog);
                Button btn_no = (Button) mView.findViewById(R.id.btn_cancel_dialog);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delNews();
                        dialog.dismiss();
                    }

                });
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog = new Dialog(this, R.style.dialog_exit);
                dialog.getWindow().setLayout((int) (x / 2.2), (int) (y / 1.8));
                dialog.setContentView(mView);
                dialog.show();
                break;
            case R.id.btn_error:
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

    private void delNews() {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("nid", nid + "");
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "NewsServlet?action=del", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String rs = responseInfo.result;
                if ("1".equals(rs)) {
                    //刷新动态信息
                    initData();
                } else {
                    Toast.makeText(MyNewsActivity.this, "删除失败!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(MyNewsActivity.this, "网络异常!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String nid = ((TextView) view.findViewById(R.id.tv_nid)).getText().toString();
        Toast.makeText(MyNewsActivity.this, "动态ID" + nid, Toast.LENGTH_SHORT).show();
        //点击跳转到详情页面
        startActivity(new Intent(MyNewsActivity.this, NewsDetailActivity.class).putExtra("nid", nid));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        nid = ((TextView) view.findViewById(R.id.tv_nid)).getText().toString();
        //根据位置 拿到 动态 ID
        View mView = getLayoutInflater().inflate(R.layout.dialog_re_del, null);
        Button btn_rename = (Button) mView.findViewById(R.id.btn_rename);
        btn_rename.setVisibility(View.GONE);
        Button btn_del = (Button) mView.findViewById(R.id.btn_del);
        btn_del.setTag("动态不想要了，删除");
        Button btn_cancel = (Button) mView.findViewById(R.id.btn_error);
        btn_del.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        dialog = new Dialog(this, R.style.dialog_exit);
        dialog.getWindow().setLayout((int) (x / 2.2), (int) (y / 1.8));
        dialog.setContentView(mView);
        dialog.show();
        return true;
    }
}