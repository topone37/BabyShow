package com.tp.bsclient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tp.bsclient.R;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.util.UrlConst;
import com.tp.bsclient.view.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/23.
 */
public class FriendListAdapter extends BaseAdapter {
    private Context context;
    private JSONArray array;

    public FriendListAdapter(Context context, JSONArray array) {
        this.context = context;
        this.array = array;


    }

    @Override
    public int getCount() {
        return array.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return array.get(i);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_friend, null);
            holder = new ViewHolder();
            //各种findViewById ..
            holder.uid = (TextView) view.findViewById(R.id.item_uid);
            holder.uname = (TextView) view.findViewById(R.id.item_uname);
            holder.status = (TextView) view.findViewById(R.id.tv_status);
            holder.head = (CircleImageView) view.findViewById(R.id.item_head);
            holder.nickname = (TextView) view.findViewById(R.id.item_nickname);
            holder.intro = (TextView) view.findViewById(R.id.item_intro);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //各种设置
        try {
            JSONObject object = array.getJSONObject(i);
            holder.uid.setText(object.optString("uid"));
            holder.uname.setText(object.optString("uname"));


            String status = object.optString("status");
            Log.v("tp", "status" + status);
            if ("1".equals(status)) {
                holder.status.setText("[在线]");
                holder.status.setTextColor(Color.parseColor("#00FFFF"));
            } else {
                holder.status.setText("[离线]");
                holder.status.setTextColor(Color.parseColor("#777777"));
            }
            if (!"".equals(object.optString("head"))) {
                MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + object.getString("head"), holder.head, MyApp.options);//头像异步加载
            } else {
                holder.head.setImageResource(R.drawable.defualt_noimg);
            }

            if ("".equals(object.optString("nickname").trim())) {
                holder.nickname.setText("无名氏");
            } else {
                holder.nickname.setText(object.optString("nickname"));
            }
            if ("".equals(object.optString("intro").trim())) {
                holder.intro.setText("这家伙太懒了,什么也没有留下...");
            } else {
                holder.intro.setText(object.optString("intro"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    private static class ViewHolder {
        public TextView uid;
        public TextView uname;
        public TextView status;
        public CircleImageView head;
        public TextView nickname;
        public TextView intro;

    }
}
