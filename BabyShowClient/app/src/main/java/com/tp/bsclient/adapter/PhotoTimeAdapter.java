package com.tp.bsclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tp.bsclient.R;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.util.UrlConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/12/5.
 */
public class PhotoTimeAdapter extends BaseAdapter {
    private Context mContext;
    private JSONArray array;
    private int mScreenWidth;

    public PhotoTimeAdapter(Context mContext, JSONArray array) {
        this.mContext = mContext;
        this.array = array;
        mScreenWidth = this.mContext.getResources().getDisplayMetrics().widthPixels;

    }

    @Override
    public int getCount() {
        return array.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return array.getJSONArray(position);
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

            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_photo, null);
            holder = new ViewHolder();
            holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
            holder.tv_pdate = (TextView) convertView.findViewById(R.id.tv_pdate);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置上图片

        try {
            JSONObject object = array.getJSONObject(position);

            String padate = object.optString("pdate");
            holder.tv_pdate.setText(padate);

            String pname = object.optString("pname");
            if (!"".equals(pname)) {
                MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + pname, holder.iv_photo, MyApp.options); //将图片加载
            } else {
                holder.iv_photo.setImageResource(R.drawable.shape_trans_style);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;

    }

    private static class ViewHolder {
        public ImageView iv_photo; //显示图片
        public TextView tv_pdate; //显示图片

    }
}
