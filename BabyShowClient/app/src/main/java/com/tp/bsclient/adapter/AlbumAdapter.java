package com.tp.bsclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tp.bsclient.R;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.util.UrlConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/23.
 */
public class AlbumAdapter extends BaseAdapter {
    private Context context;
    private JSONArray albums;
    private int mScreenWidth;


    public AlbumAdapter(Context context, JSONArray albums) {
        this.context = context;
        this.albums = albums;
        mScreenWidth = this.context.getResources().getDisplayMetrics().widthPixels;


    }


    @Override
    public int getCount() {
        return albums.length();
    }

    @Override
    public Object getItem(int i) {
        try {
            return albums.get(i);
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
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.grid_item_album, null);

            //各种findViewByID
            holder.tv_aid = (TextView) view.findViewById(R.id.tv_aid);
            holder.tv_aname = (TextView) view.findViewById(R.id.tv_aname);
            holder.iv_album = (ImageView) view.findViewById(R.id.iv_album);
            holder.fl_file = (FrameLayout) view.findViewById(R.id.fl_file);


            ViewGroup.LayoutParams params = holder.fl_file.getLayoutParams();
            params.width = mScreenWidth / 3;
            params.height = mScreenWidth / 3;
            holder.fl_file.setLayoutParams(params);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //各种set
        try {
            JSONObject object = albums.getJSONObject(i);
            holder.tv_aid.setText(object.optString("aid"));//相册名字
            holder.tv_aname.setText(object.optString("aname"));//相册名字

            //相册封面
            if (!"".equals(object.optString("aphoto"))) {
                MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + object.optString("aphoto"), holder.iv_album, MyApp.options); //相册封面
            } else {

                holder.iv_album.setImageResource(R.drawable.defualt_noimg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    private static class ViewHolder {
        public FrameLayout fl_file;
        public TextView tv_aid;
        public TextView tv_aname;
        public ImageView iv_album;
    }
}
