package com.tp.bsclient.adapter;

import android.content.Context;
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
 * Created by Administrator on 2015/12/15.
 */
public class FriendsAdapter extends BaseAdapter {
    private Context context;
    private JSONArray array;

    public FriendsAdapter(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.length();
    }

    public void removeAll() {
        array = new JSONArray();
        notifyDataSetChanged();
    }

    public void addData(JSONArray array) {
        this.array = array;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        try {
            return array.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_friends, null);
            holder = new ViewHolder();
            holder.head = (CircleImageView) convertView.findViewById(R.id.f_head);
            holder.nickname = (TextView) convertView.findViewById(R.id.f_nickname);
            holder.intro = (TextView) convertView.findViewById(R.id.f_intro);
            holder.uid = (TextView) convertView.findViewById(R.id.f_uid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        JSONObject object = array.optJSONObject(position);
        if (!"".equals(object.optString("head"))) {
            MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + object.optString("head"), holder.head, MyApp.options);
        } else {
            holder.head.setImageResource(R.drawable.img01);
        }

        if (!"".equals(object.optString("nickname"))) {
            holder.nickname.setText(object.optString("nickname"));
        } else {
            holder.nickname.setText("无名氏");
        }
        if (!"".equals(object.optString("intro"))) {
            holder.intro.setText(object.optString("intro"));
        } else {
            holder.intro.setText("这家伙太懒了，什么也没有留下...");
        }
        holder.uid.setText(object.optInt("uid") + "");

        return convertView;
    }

    private static class ViewHolder {
        private CircleImageView head;
        private TextView nickname;
        private TextView intro;
        private TextView uid;
    }
}
