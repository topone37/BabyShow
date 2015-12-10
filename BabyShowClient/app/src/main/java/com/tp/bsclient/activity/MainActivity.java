package com.tp.bsclient.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tp.bsclient.R;
import com.tp.bsclient.adapter.ContentPagerAdapter;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.fragment.ChatFragment;
import com.tp.bsclient.fragment.HomeFragment;
import com.tp.bsclient.fragment.NewsFragment;
import com.tp.bsclient.fragment.SettingFragment;
import com.tp.bsclient.util.UrlConst;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * 主页面
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private List<Fragment> fragments;
    private ViewPager vp_home;
    private LinearLayout ll_home;
    private LinearLayout ll_news;
    private LinearLayout ll_chat;
    private LinearLayout ll_setting;

    private ImageView iv_home;
    private ImageView iv_news;
    private ImageView iv_chat;
    private ImageView iv_setting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);

        //连接上融云服务器
        if (MyApp.users != null) {
            RongIM.connect(MyApp.users.getToken(), new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Toast.makeText(MainActivity.this, "Token有误 ，请联系开发人员", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String s) {
                    Toast.makeText(MainActivity.this, "连接融云成功！", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Toast.makeText(MainActivity.this, "异常", Toast.LENGTH_SHORT).show();
                }
            });
        }
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String s) {
                if (MyApp.users != null) {
                    return new UserInfo(MyApp.users.getUname(), "千山暮雪", Uri.parse(UrlConst.PHOTO_URL + MyApp.users.getHead()));
                } else {
                    Toast.makeText(MainActivity.this, "网络异常,用户信息获取失败!", Toast.LENGTH_SHORT).show();
                    return new UserInfo("", "", null);
                }

            }
        }, true);
        //刷新用户信息
        // RongIM.getInstance().refreshUserInfoCache(new UserInfo(MyApp.users.getUname(), "千山暮雪", Uri.parse(UrlConst.PHOTO_URL + MyApp.users.getHead())));
        initView();
    }

    public void initView() {
        //实例化控件
        vp_home = (ViewPager) findViewById(R.id.vp_main);

        ll_home = (LinearLayout) findViewById(R.id.ll_home);
        ll_home.setOnClickListener(this);
        ll_news = (LinearLayout) findViewById(R.id.ll_news);
        ll_news.setOnClickListener(this);
        ll_chat = (LinearLayout) findViewById(R.id.ll_chat);
        ll_chat.setOnClickListener(this);
        ll_setting = (LinearLayout) findViewById(R.id.ll_setting);
        ll_setting.setOnClickListener(this);

        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_news = (ImageView) findViewById(R.id.iv_news);
        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);


        //准备数据
        this.fragments = new ArrayList<Fragment>();
        this.fragments.add(new HomeFragment());
        this.fragments.add(new NewsFragment());
        this.fragments.add(new ChatFragment());
        this.fragments.add(new SettingFragment());
        //将数据装载到适配器
        ContentPagerAdapter adapter = new ContentPagerAdapter(getSupportFragmentManager(), fragments);
        //设置适配器
        this.vp_home.setAdapter(adapter);

        vp_home.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.v("tp", "onPageScrollStateChanged" + i);
                changeSel(i); //下面的导航 同步
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void changeSel(int position) {
        switch (position % 4) {
            case 0:
                iv_home.setImageResource(R.drawable.home_press_icon);
                iv_news.setImageResource(R.drawable.news_icon);
                iv_chat.setImageResource(R.drawable.chat_icon);
                iv_setting.setImageResource(R.drawable.setting_icon);
                break; //选中的是首页
            case 1:
                iv_home.setImageResource(R.drawable.home_icon);
                iv_news.setImageResource(R.drawable.news_press_icon);
                iv_chat.setImageResource(R.drawable.chat_icon);
                iv_setting.setImageResource(R.drawable.setting_icon);
                break; //选中的是首页
            case 2:
                iv_home.setImageResource(R.drawable.home_icon);
                iv_news.setImageResource(R.drawable.news_icon);
                iv_chat.setImageResource(R.drawable.chat_press_icon);
                iv_setting.setImageResource(R.drawable.setting_icon);
                break; //选中的是首页
            case 3:
                iv_home.setImageResource(R.drawable.home_icon);
                iv_news.setImageResource(R.drawable.news_icon);
                iv_chat.setImageResource(R.drawable.chat_icon);
                iv_setting.setImageResource(R.drawable.setting_press_icon);
                break; //选中的是首页
            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                changeSel(0);
                vp_home.setCurrentItem(0);
                break;
            case R.id.ll_news:

                changeSel(1);
                vp_home.setCurrentItem(1);
                break;
            case R.id.ll_chat:
                changeSel(2);
                vp_home.setCurrentItem(2);
                break;
            case R.id.ll_setting:
                changeSel(3);
                vp_home.setCurrentItem(3);
                break;
            default:
                break;


        }
    }
}
