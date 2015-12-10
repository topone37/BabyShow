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
 * Created by Administrator on 2015/12/5.
 */
public class CommentAdapter extends BaseAdapter {
    private JSONArray array;
    private Context mContext;


    public CommentAdapter(Context mContext, JSONArray array) {
        this.array = array;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return array.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return array.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            //压出列表项 每一项布局
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_comment, null);
            holder = new ViewHolder();
            holder.chead = (CircleImageView) convertView.findViewById(R.id.chead);
            holder.cnickname = (TextView) convertView.findViewById(R.id.cnickname);
            holder.cdate = (TextView) convertView.findViewById(R.id.cdate);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //将信息设置上去
        try {
            JSONObject object = array.getJSONObject(position);
            //头像
            if (!"".equals(object.optString("chead"))) {
                MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + object.optString("chead"), holder.chead, MyApp.options);
            } else {
                holder.chead.setImageResource(R.drawable.defualt_noimg);
            }
            //昵称
            holder.cnickname.setText(object.optString("cnickname"));
            //日期
            holder.cdate.setText(object.optString("cdate"));
            //内容
            holder.content.setText(object.optString("content"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }


    private static class ViewHolder {
        CircleImageView chead;
        TextView cnickname;
        TextView cdate;
        TextView content;
    }
}
