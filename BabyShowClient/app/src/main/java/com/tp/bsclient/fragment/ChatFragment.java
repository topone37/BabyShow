package com.tp.bsclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tp.bsclient.R;
import com.tp.bsclient.activity.AddFriendsActivity;
import com.tp.bsclient.adapter.FriendListAdapter;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.util.UrlConst;
import com.tp.bsclient.view.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;

import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2015/11/6.
 */
public class ChatFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout sr_chat;
    private EditText edit_search;
    private ListView lv_chat;
    private JSONArray friends;
    private RadioGroup rg_chat;
    private LinearLayout ll_search_bar;
    private TextView tv_add_friend;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        //初始化页面
        initView(view);

        //初始化数据
        initFriendData();

        return view;
    }

    private void initView(View view) {
        this.ll_search_bar = (LinearLayout) view.findViewById(R.id.ll_search_bar);
        this.ll_search_bar.setOnClickListener(this);
        this.sr_chat = (SwipeRefreshLayout) view.findViewById(R.id.sr_chat);
        this.sr_chat.setOnRefreshListener(this);
        this.lv_chat = (ListView) view.findViewById(R.id.lv_chat);
        this.lv_chat.setOnItemClickListener(this);

        this.tv_add_friend = (TextView) view.findViewById(R.id.tv_add_friend);
        this.tv_add_friend.setOnClickListener(this);
        this.rg_chat = (RadioGroup) view.findViewById(R.id.rg_chat);
        ((RadioButton) this.rg_chat.getChildAt(1)).setChecked(true);
        this.rg_chat.setOnCheckedChangeListener(this);
    }

    private void initFriendData() {
        if (MyApp.users == null) {
            Toast.makeText(getActivity(), "你已经处于离线状态，请重新登录！", Toast.LENGTH_SHORT).show();
            return;
        }
        //初始化数据
        //访问网络拿到数据
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", "" + MyApp.users.getUid());

        httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "FriendsServlet?action=find", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                sr_chat.setRefreshing(false);
                String result = responseInfo.result;
                if ("".equals(result.trim())) {
                    Toast.makeText(getActivity(), "你还么有好友，再去添加", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        friends = new JSONArray(result);
                        //装载数据到适配器上
                        FriendListAdapter adapter = new FriendListAdapter(getActivity(), friends);
                        //设置适配器
                        lv_chat.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_chat_huihua:
                break;
            case R.id.rb_chat_friend:
                break;
            default:
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_friend:
                startActivity(new Intent(getActivity(), AddFriendsActivity.class));
                break;
            case R.id.ll_search_bar:
                startActivity(new Intent(getActivity(), AddFriendsActivity.class));
                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RongIM.getInstance().startPrivateChat(getActivity(), ((TextView) view.findViewById(R.id.item_uname)).getText().toString(), "聊天");
    }

    @Override
    public void onRefresh() {
        initFriendData();
    }

}