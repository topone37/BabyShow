package com.tp.bsclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    private int bg[] = new int[]{R.drawable.shape_pic_style_01, R.drawable.shape_pic_style_02, R.drawable.shape_pic_style_03,
            R.drawable.shape_pic_style_04, R.drawable.shape_pic_style_05, R.drawable.shape_pic_style_06, R.drawable.shape_pic_style_07};

    private int w = 0;

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
            holder.tv_pweek = (TextView) convertView.findViewById(R.id.tv_pweek);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置上图片

        try {
            JSONObject object = array.getJSONObject(position);

            String padate = object.optString("pdate");
            holder.tv_pdate.setText(padate);

            holder.tv_pweek.setText(convertTime(object.getInt("pweek")));
            holder.iv_photo.setBackgroundResource(bg[w - 1]);

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

    private String convertTime(int i) {
        switch (i) {
            case 1:
                w = 1;
                return "星期一";
            case 2:
                w = 2;
                return "星期二";
            case 3:
                w = 3;
                return "星期三";
            case 4:
                w = 4;
                return "星期四";
            case 5:
                w = 5;
                return "星期五";
            case 6:
                w = 6;
                return "星期六";
            case 7:
                w = 7;
                return "星期日";
            default:
                return "未知";
        }
    }

    private static class ViewHolder {
        public ImageView iv_photo; //显示图片
        public TextView tv_pdate; //显示图片
        public TextView tv_pweek; //显示图片

    }
}
