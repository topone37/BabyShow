package com.tp.bsclient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.tp.bsclient.R;
import com.tp.bsclient.po.Advertise;
import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * 图片轮播适配器
 */
public class ImgAdapter extends BaseAdapter {

    private Context _context;
    private List<Advertise> imgList;
    private FinalBitmap finalImageLoader;

    public ImgAdapter(Context context, List<Advertise> imgList) {
        _context = context;
        this.imgList = imgList;
        finalImageLoader = FinalBitmap.create(context);
        finalImageLoader.configLoadingImage(R.drawable.loading);
        finalImageLoader.configLoadfailImage(R.drawable.fail);

    }

    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public Object getItem(int position) {

        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            ImageView imageView = new ImageView(_context);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ScaleType.FIT_XY);
            imageView.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            convertView = imageView;
            viewHolder.imageView = (ImageView) convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (imgList.size() != 0) {
            finalImageLoader.display(viewHolder.imageView, imgList.get(position % imgList.size()).getUrl());
        }
        return convertView;
    }

    public void AddDtata(List<Advertise> data) {
        for (Advertise str : data) {
            imgList.add(str);
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView imageView;
    }
}
