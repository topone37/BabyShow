package com.tp.bsclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tp.bsclient.R;
import com.tp.bsclient.activity.AboutActivity;
import com.tp.bsclient.activity.FeedbackActivity;
import com.tp.bsclient.activity.MoreActivity;
import com.tp.bsclient.activity.MyCollectsActivity;
import com.tp.bsclient.activity.MyNewsActivity;
import com.tp.bsclient.activity.UserInfoActivity;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.util.UrlConst;
import com.tp.bsclient.view.CircleImageView;

/**
 * Created by Administrator on 2015/11/6.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {
    private RelativeLayout rl_userinfo;
    private RelativeLayout rl_mynews;
    private RelativeLayout rl_mycol;
    private RelativeLayout rl_feedback;
    private RelativeLayout rl_about;
    private RelativeLayout rl_more;
    private CircleImageView head;
    private TextView nickname;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        //初始化界面 并设置上监听事件
        nickname = (TextView) view.findViewById(R.id.set_nickname);
        if (MyApp.users != null) {
            nickname.setText(MyApp.users.getNickname());
        } else {
            nickname.setText("无名氏");
        }
        head = (CircleImageView) view.findViewById(R.id.head);
        rl_userinfo = (RelativeLayout) view.findViewById(R.id.rl_userinfo);
        rl_userinfo.setOnClickListener(this);
        rl_mynews = (RelativeLayout) view.findViewById(R.id.rl_mynews);
        rl_mynews.setOnClickListener(this);
        rl_mycol = (RelativeLayout) view.findViewById(R.id.rl_mycol);
        rl_mycol.setOnClickListener(this);
        rl_feedback = (RelativeLayout) view.findViewById(R.id.rl_feedback);
        rl_feedback.setOnClickListener(this);
        rl_about = (RelativeLayout) view.findViewById(R.id.rl_about);
        rl_about.setOnClickListener(this);
        rl_more = (RelativeLayout) view.findViewById(R.id.rl_more);
        rl_more.setOnClickListener(this);
        if (MyApp.users != null) {
            MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + MyApp.users.getHead(), head, MyApp.options);//头像异步加载
        } else {
            head.setImageResource(R.drawable.head_default);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_userinfo:
                //个人信息设置
                if (MyApp.users != null) {
                    startActivity(new Intent(getActivity(), UserInfoActivity.class));
                } else {
                    Toast.makeText(getActivity(), "用户处于离线状态，请重新登录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_mynews:
                //我的动态
                startActivity(new Intent(getActivity(), MyNewsActivity.class));
                break;
            case R.id.rl_mycol:
                //我的收藏
                startActivity(new Intent(getActivity(), MyCollectsActivity.class));
                break;
            case R.id.rl_feedback:
                //反馈
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case R.id.rl_more:
                //更多
                startActivity(new Intent(getActivity(), MoreActivity.class));
                break;
            case R.id.rl_about:
                //更多
                startActivity(new Intent(getActivity(), AboutActivity.class));
                break;

            default:
                break;
        }
    }
}
