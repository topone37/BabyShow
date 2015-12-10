package com.tp.bsclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
public class MyNewsActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private LinearLayout ll_back;
    private ListView lv_mynews;
    private List<News> data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_mynews);
        initView(); //初始化视图
        initData();//初始化数据
    }

    private void initView() {
        this.ll_back = (LinearLayout) findViewById(R.id.ll_back_mynews);
        this.ll_back.setOnClickListener(this);
        this.lv_mynews = (ListView) findViewById(R.id.lv_mynews);
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
                //设置上点击监听器
                lv_mynews.setOnItemClickListener(MyNewsActivity.this);
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
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String nid = ((TextView) view.findViewById(R.id.tv_nid)).getText().toString();
        Toast.makeText(MyNewsActivity.this, "动态ID" + nid, Toast.LENGTH_SHORT).show();
        //点击跳转到详情页面
        startActivity(new Intent(MyNewsActivity.this, NewsDetailActivity.class).putExtra("nid", nid));
    }
}