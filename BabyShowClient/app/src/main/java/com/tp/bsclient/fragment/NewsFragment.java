package com.tp.bsclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.tp.bsclient.activity.AddNewsActivity;
import com.tp.bsclient.activity.NewsDetailActivity;
import com.tp.bsclient.adapter.NewsAdapter;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.util.GsonUtil;
import com.tp.bsclient.util.UrlConst;
import com.tp.bsclient.view.XListView;
import com.tp.bsclient.view.XListView.IXListViewListener;
import com.tp.bsclient.vo.News;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/6.
 */
public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, IXListViewListener {
    private final static int INIT_CODE = 0x666;
    private final static int REFRESH_CODE = 0x056;
    private final static int LOAD_CODE = 0x676;
    private XListView xlv_news;//动态列表
    private View view; //当前Fragment对应的视图
    private List<News> news;//动态数据
    private TextView tv_add;
    private int currpage = 1; //当前页
    private NewsAdapter adapter;
    private int flag = INIT_CODE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_news, container, false);
        this.tv_add = (TextView) this.view.findViewById(R.id.tv_add);
        this.tv_add.setOnClickListener(this);
        this.xlv_news = (XListView) this.view.findViewById(R.id.lv_news);

        this.xlv_news.setPullLoadEnable(true);
        this.xlv_news.setPullRefreshEnable(true);

        this.xlv_news.setXListViewListener(this);//下拉刷新 上拉加载事件
        this.xlv_news.setOnItemClickListener(this);//列表项点击事件

        /***********初始化*********/
        flag = INIT_CODE;
        currpage = 1;
        getData();
        //初始状态 拉取第一页数据
        return this.view;

    }

    private void getData() {
        if (MyApp.users == null) {
            return;
        }
        //初始化视图时先来第一页数据
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("currPage", currpage + ""); //将当前页传入
        params.addBodyParameter("uid", MyApp.users.getUid() + ""); //将当前页传入
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "NewsServlet?action=getAll", params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //每次访问网络拉取数据
                Type type = new TypeToken<List<News>>() {
                }.getType();
                news = new ArrayList<>(); //初始化空间
                news = (List<News>) GsonUtil.fromJson(responseInfo.result, type); //获取数据
                switch (flag) {
                    case INIT_CODE:
                        //初始化阶段
                        //将数据装载到适配器
                        adapter = new NewsAdapter(getActivity(), news); //初始化
                        //设置适配器
                        xlv_news.setAdapter(adapter);
                        break;
                    case REFRESH_CODE:
                        //下拉刷新
                        //清空适配器
                        adapter.removeAll();
                        adapter.addData(news);
                        break;
                    case LOAD_CODE:
                        if (news.size() != 0) {
                            //更多数据
                            adapter.addData(news);
                        }
                        break;
                }

                if (news.size() > 0) {
                    xlv_news.setPullLoadEnable(true);
                } else {
                    xlv_news.setPullLoadEnable(false);
                }
                Log.v("tp", "拉取的数据>>>" + news.size());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), "网络异常,请稍后重试!" + s, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String nid = ((TextView) view.findViewById(R.id.tv_nid)).getText().toString();

        startActivity(new Intent(getActivity(), NewsDetailActivity.class).putExtra("nid", nid));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_col:
                Toast.makeText(getActivity(), "点击分享按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_comment:
                Toast.makeText(getActivity(), "点击评论按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_zan:
                Toast.makeText(getActivity(), "点击赞按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_add:
                startActivity(new Intent(getActivity(), AddNewsActivity.class));
                break;

            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        currpage = 1;
        flag = REFRESH_CODE;
        getData();
        xlv_news.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        currpage++;
        flag = LOAD_CODE;
        getData();
        xlv_news.stopLoadMore();
    }
}