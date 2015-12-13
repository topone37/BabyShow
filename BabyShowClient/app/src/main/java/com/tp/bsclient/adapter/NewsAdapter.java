package com.tp.bsclient.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tp.bsclient.R;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.util.UrlConst;
import com.tp.bsclient.view.CircleImageView;
import com.tp.bsclient.vo.News;

import java.util.List;

/**
 * Created by Administrator on 2015/11/23.
 */
public class NewsAdapter extends BaseAdapter {


    private Context context;
    private List<News> data;
    private int mScreenWidth;

    public NewsAdapter(Context context, List<News> data) {
        this.context = context;
        this.data = data;
        mScreenWidth = this.context.getResources().getDisplayMetrics().widthPixels - 30;

    }

    public void removeAll() {
        //将适配器中的数据清空
        data.clear();
        notifyDataSetChanged();
    }

    public void addData(List<News> list) {
        Log.v("tp", "加上数据到适配器");
        for (int i = 0; i < list.size(); i++) {
            data.add(list.get(i));//将数据一个个加到原始数据的后头
        }
        //刷新适配器
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item_news, null);
            holder = new ViewHolder();
            //各种findViewById ..
            holder.nid = (TextView) view.findViewById(R.id.tv_nid);
            holder.head = (CircleImageView) view.findViewById(R.id.head);
            holder.nickname = (TextView) view.findViewById(R.id.nickname);
            holder.intro = (TextView) view.findViewById(R.id.intro);
            holder.content = (TextView) view.findViewById(R.id.content);
            holder.intro = (TextView) view.findViewById(R.id.intro);

            holder.img1 = (ImageView) view.findViewById(R.id.img1);
            holder.img2 = (ImageView) view.findViewById(R.id.img2);
            holder.img3 = (ImageView) view.findViewById(R.id.img3);
//
            holder.tv_colnum = (TextView) view.findViewById(R.id.tv_col_num_item);
            holder.tv_comnum = (TextView) view.findViewById(R.id.tv_comment_num_item);
            holder.tv_zannum = (TextView) view.findViewById(R.id.tv_zan_num_item);
            holder.iv_col = (ImageView) view.findViewById(R.id.iv_col);
            holder.iv_zan = (ImageView) view.findViewById(R.id.iv_zan);


            holder.date = (TextView) view.findViewById(R.id.tv_date);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //各种设置

        News news = data.get(i);//得到对应的Gson数据
        if (news.getHead() != null && !news.getHead().equals("")) {
            MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + news.getHead(), holder.head, MyApp.options);//头像异步加载
        } else {
            holder.head.setImageResource(R.drawable.defualt_noimg);
        }

        holder.nid.setText(news.getNid() + ""); //动态ID

        if (news.getNickname() != null && !news.getNickname().equals("")) {
            holder.nickname.setText(news.getNickname()); //昵称
        } else {
            holder.nickname.setText("无名氏"); //昵称
        }


        if (news.getIntro() != null && !news.getIntro().equals("")) {
            holder.intro.setText(news.getIntro()); //简介
        } else {
            holder.intro.setText("这家伙很懒,什么都没有留下..."); //简介
        }

        if (news.getContent() != null && !news.getContent().equals("")) {
            holder.content.setText(news.getContent()); //内容
        } else {
            holder.content.setText("什么内容,都没有哦..."); //内容
        }

        if (news.getDate() != null && !news.getDate().equals("")) {
            holder.date.setText(news.getDate()); //动态日期
        } else {
            holder.date.setText("11-23"); //动态日期
        }
        ViewGroup.LayoutParams params = holder.img1.getLayoutParams();
        params.width = mScreenWidth / 3;
        params.height = (int) (mScreenWidth / 3 / 0.9);
        holder.img1.setLayoutParams(params);
        holder.img2.setLayoutParams(params);
        holder.img3.setLayoutParams(params);
        //有图片
        List<String> imgs = news.getImgs();//获取对应图片
        if (imgs == null || imgs.size() == 0) {
            holder.img1.setVisibility(View.GONE);
            holder.img2.setVisibility(View.GONE);
            holder.img3.setVisibility(View.GONE);
        } else if (imgs.size() == 1) {
            //只有一张图 图片大小 放大点

            holder.img1.setVisibility(View.VISIBLE);
            holder.img2.setVisibility(View.GONE);
            holder.img3.setVisibility(View.GONE);
            MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + imgs.get(0), holder.img1, MyApp.options);
        } else if (imgs.size() == 2) {
            holder.img1.setVisibility(View.VISIBLE);
            holder.img2.setVisibility(View.VISIBLE);
            holder.img3.setVisibility(View.GONE);
            MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + imgs.get(0), holder.img1, MyApp.options);
            MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + imgs.get(1), holder.img2, MyApp.options);
        } else if (imgs.size() == 3) {
            holder.img1.setVisibility(View.VISIBLE);
            holder.img2.setVisibility(View.VISIBLE);
            holder.img3.setVisibility(View.VISIBLE);
            MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + imgs.get(0), holder.img1, MyApp.options);
            MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + imgs.get(1), holder.img2, MyApp.options);
            MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + imgs.get(2), holder.img3, MyApp.options);
        }
//        //初始化相关数据
        if (news.isColStatue()) {
            holder.iv_col.setImageResource(R.drawable.btn_col_sel);
        } else {
            holder.iv_col.setImageResource(R.drawable.btn_col);
        }
        if (news.isZanStatue()) {
            holder.iv_zan.setImageResource(R.drawable.btn_zan_sel);
        } else {
            holder.iv_zan.setImageResource(R.drawable.btn_zan);
        }

        holder.tv_colnum.setText(news.getColNum() + "");
        holder.tv_comnum.setText(news.getComNum() + "");
        holder.tv_zannum.setText(news.getZanNum() + "");
        return view;
    }

    private static class ViewHolder {
        public TextView nid;//动态ID
        public CircleImageView head;//头像
        public TextView nickname;//昵称
        public TextView intro;//简介
        public TextView content;//内容

        public ImageView img1;//图片1
        public ImageView img2;//图片2
        public ImageView img3;//图片3

        public TextView date; //日期

        public TextView tv_colnum;
        public TextView tv_comnum;
        public TextView tv_zannum;
        public ImageView iv_zan;//赞 图标
        public ImageView iv_col;//收藏 图标


    }


}
