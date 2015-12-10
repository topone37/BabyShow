package com.tp.bsclient.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import com.tp.bsclient.R;

import java.util.List;

/**
 * Created by Administrator on 2015/11/20.
 */
public class PostAdapter extends PagerAdapter {
    private Context context;
    private List<View> views;
    private int[] imgs = new int[]{R.drawable.post01, R.drawable.post02, R.drawable.post03};

    public PostAdapter(Context context, List<View> views) {
        this.context = context;
        this.views = views;

    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == obj;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //不移除对象 因为已经在instantiateItem处理了
        //container.removeView(views.get(Integer.MAX_VALUE % (views.size())));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //对ViewPager页号求模取出View列表中要显示的项
        View view = views.get(position);
        ((ImageView) view).setImageResource(imgs[position]); //根据位置取出轮播图片
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp = view.getParent();
        if (vp != null) {
            ViewGroup parent = (ViewGroup) vp;
            parent.removeView(view);
        }
        container.addView(view);
        //add listeners here if necessary
        return view;
    }
}
