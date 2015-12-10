package com.tp.bsclient.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import com.tp.bsclient.R;

import java.util.List;

/**
 * Created by Administrator on 2015/11/6.
 */
public class WelcomePagerAdapter extends PagerAdapter {
    private List<View> views;
    private int[] imgs = new int[]{R.drawable.w01, R.drawable.w02, R.drawable.w03};

    public WelcomePagerAdapter(List<View> views) {
        super();
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //动态设置上图片
        views.get(position).setBackgroundResource(imgs[position % 3]);
        ((ViewPager) container).addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(views.get(position));
    }
}
