package com.tp.bsclient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tp.bsclient.R;
import com.tp.bsclient.adapter.CommentAdapter;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.util.UrlConst;
import com.tp.bsclient.view.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/23.
 */
public class NewsDetailActivity extends Activity implements View.OnClickListener {
    private LinearLayout ll_back;//返回


    private CircleImageView head;//头像
    private TextView nickname;//昵称
    private TextView intro;//简介
    private TextView content;//内容

    private ImageView img1;//图片1
    private ImageView img2;//图片2
    private ImageView img3;//图片3
    private ImageView img4;//图片4
    private ImageView img5;//图片5
    private ImageView img6;//图片6

    private LinearLayout ll_col;//收藏点击区域
    private LinearLayout ll_comment; //评论点击区域
    private LinearLayout ll_zan;//赞点击区域
    private TextView tv_colnum;
    private TextView tv_comnum;
    private TextView tv_zannum;
    private CheckBox cb_zan;//赞 图标
    private CheckBox cb_col;//收藏 图标


    private TextView date; //日期
    private String nid; //动态ID


    private LinearLayout ll_comment_hide;
    private EditText edit_comment;
    private Button btn_comment;
    private ListView lv_comment;
    private CommentAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_newsdetail);
        MyApp.imageLoader = MyApp.imageLoader.getInstance();

        initView();
        initData();
    }


    private void initView() {
        View view = getLayoutInflater().inflate(R.layout.list_head_newsdetail, null);


        head = (CircleImageView) view.findViewById(R.id.head_detail); //用户头像
        intro = (TextView) view.findViewById(R.id.tv_intro_detail); //用户简介信息
        nickname = (TextView) view.findViewById(R.id.tv_nickname_detail); //昵称
        content = (TextView) view.findViewById(R.id.tv_content_detail);//内容
        date = (TextView) view.findViewById(R.id.tv_date_detail);//日期

        img1 = (ImageView) view.findViewById(R.id.img1_detail); //图片1
        img2 = (ImageView) view.findViewById(R.id.img2_detail); //图片2
        img3 = (ImageView) view.findViewById(R.id.img3_detail);//图片3
        img4 = (ImageView) view.findViewById(R.id.img4_detail); //图片4
        img5 = (ImageView) view.findViewById(R.id.img5_detail); //图片6
        img6 = (ImageView) view.findViewById(R.id.img6_detail);//图片7
        cb_zan = (CheckBox) view.findViewById(R.id.cb_zan); //赞图标
        cb_col = (CheckBox) view.findViewById(R.id.cb_col); //收藏图标

        tv_colnum = (TextView) view.findViewById(R.id.tv_col_num); //分享数
        tv_comnum = (TextView) view.findViewById(R.id.tv_comment_num);//评论数
        tv_zannum = (TextView) view.findViewById(R.id.tv_zan_num);//点赞数

        ll_comment_hide = (LinearLayout) findViewById(R.id.ll_comment_hide); //隐藏的评论框
        edit_comment = (EditText) findViewById(R.id.edit_comment); //评论内容编辑框
        btn_comment = (Button) findViewById(R.id.btn_comment);//发送评论按钮
        btn_comment.setOnClickListener(this); //点击事件 联网提交评论

        //初始化三个 并绑定监听事件
        this.ll_col = (LinearLayout) view.findViewById(R.id.ll_col); //分享按钮

        this.ll_col.setOnClickListener(this);

        this.ll_comment = (LinearLayout) view.findViewById(R.id.ll_comment); //评论按钮

        this.ll_comment.setOnClickListener(this);

        this.ll_zan = (LinearLayout) view.findViewById(R.id.ll_zan); //赞按钮

        this.ll_zan.setOnClickListener(this);

        this.ll_back = (LinearLayout) view.findViewById(R.id.ll_back_newsdetail);//返回图标

        this.ll_back.setOnClickListener(this);
        lv_comment = (ListView) findViewById(R.id.lv_comment); //评论列表
        lv_comment.addHeaderView(view);
        //根据动态列表点击传入的动态id
        Intent intent = getIntent();
        nid = intent.getStringExtra("nid");
        if (MyApp.users == null) {
            Toast.makeText(NewsDetailActivity.this, "你已经处于离线状态，请稍后重试!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void initData() {
        //访问网络 查出对应ID的动态详情
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", nid);
        params.addBodyParameter("uid", MyApp.users.getUid() + "");
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "NewsServlet?action=getNewsById", params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //各种设置
                try {
                    JSONObject object = new JSONObject(responseInfo.result);
                    if (object.optString("head") != null) {
                        MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + object.getString("head"), head, MyApp.options);//头像异步加载
                    } else {
                        Log.d("tp", "头像获取失败");
                    }
                    nickname.setText(object.optString("nickname"));//用户昵称
                    intro.setText(object.optString("intro"));//动态简介
                    content.setText(object.optString("content"));//动态内容
                    date.setText(object.optString("date"));//动态日期
                    tv_colnum.setText(object.optString("colnum"));//分享 数量
                    tv_comnum.setText(object.optString("comnum"));//评论数量
                    tv_zannum.setText(object.optString("zannum")); //赞的数量
                    if (object.optString("zanstatue").equals("yes")) {
                        //赞 设置为选中 状态
                        cb_zan.setChecked(true);
                    } else {
                        cb_zan.setChecked(false);
                    }
                    if (object.optString("colstatue").equals("yes")) {
                        //col 设置为选中 状态
                        cb_col.setChecked(true);
                    } else {
                        cb_col.setChecked(false);
                    }

                    //图片  ( 三张 )
                    JSONObject _object = object.getJSONObject("img");
                    if (!"".equals(_object.optString("img1"))) {
                        MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + _object.optString("img1"), img1, MyApp.options);
                        img1.setTag(_object.optString("img1"));
                        img1.setOnClickListener(new ImgOCL());
                    } else {
                        img1.setImageResource(R.drawable.shape_trans_style);
                        img1.setVisibility(View.GONE);
                    }

                    if (!"".equals(_object.optString("img2"))) {
                        MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + _object.optString("img2"), img2, MyApp.options);
                        img2.setTag(_object.optString("img2"));
                        img2.setOnClickListener(new ImgOCL());
                    } else {
                        img2.setImageResource(R.drawable.shape_trans_style);
                        img2.setVisibility(View.GONE);
                    }
                    if (!"".equals(_object.optString("img3"))) {
                        MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + _object.optString("img3"), img3, MyApp.options);
                        img3.setTag(_object.optString("img3"));
                        img3.setOnClickListener(new ImgOCL());
                    } else {
                        Log.v("tp", "img3没有");
                        img3.setImageResource(R.drawable.shape_trans_style);
                        img3.setVisibility(View.GONE);
                    }
                    if (!"".equals(_object.optString("img4"))) {
                        MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + _object.optString("img4"), img4, MyApp.options);
                        img4.setTag(_object.optString("img4"));
                        img4.setOnClickListener(new ImgOCL());
                    } else {
                        Log.v("tp", "img4没有");
                        img4.setImageResource(R.drawable.shape_trans_style);
                        img4.setVisibility(View.GONE);
                    }
                    if (!"".equals(_object.optString("img5"))) {
                        MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + _object.optString("img5"), img5, MyApp.options);
                        img5.setTag(_object.optString("img5"));
                        img5.setOnClickListener(new ImgOCL());
                    } else {
                        Log.v("tp", "img5没有");
                        img5.setImageResource(R.drawable.shape_trans_style);
                        img5.setVisibility(View.GONE);
                    }
                    if (!"".equals(_object.optString("img6"))) {
                        MyApp.imageLoader.displayImage(UrlConst.PHOTO_URL + _object.optString("img6"), img6, MyApp.options);
                        img6.setTag(_object.optString("img6"));
                        img6.setOnClickListener(new ImgOCL());
                    } else {
                        Log.v("tp", "img6没有");
                        img6.setImageResource(R.drawable.shape_trans_style);
                        img6.setVisibility(View.GONE);
                    }


                    JSONArray com_array = object.getJSONArray("com_array"); //得到相关评论的数据

                    //数据 装载适配器
                    adapter = new CommentAdapter(NewsDetailActivity.this, com_array);
                    //设置适配器
                    lv_comment.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.v("tp", "exception " + e.toString());
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(NewsDetailActivity.this, "网络异常" + s, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_col:
                col();
                break;
            case R.id.ll_comment:
                initComment(); //点击评论
                break;
            case R.id.ll_zan:
                zan();
                break;
            case R.id.ll_back_newsdetail:
                finish();
                break;
            case R.id.btn_comment:
                comment();
                break;
            default:
                break;
        }
    }

    private void col() {
        if (MyApp.users != null) {
            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("uid", MyApp.users.getUid() + "");
            params.addBodyParameter("nid", nid);

            httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "NewsServlet?action=col", params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if ("1".equals(responseInfo.result)) {
                        if (cb_col.isChecked()) {
                            cb_col.setChecked(false);
                            tv_colnum.setText(Integer.valueOf(tv_colnum.getText().toString()) - 1 + "");
                        } else {
                            cb_col.setChecked(true);
                            tv_colnum.setText(Integer.valueOf(tv_colnum.getText().toString()) + 1 + "");
                        }


                    } else {
                        Toast.makeText(NewsDetailActivity.this, "操作失败!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(NewsDetailActivity.this, "网络异常，请稍后重试!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(NewsDetailActivity.this, "你已经处于离线状态，请重新登录！", Toast.LENGTH_SHORT).show();
        }
    }

    private void comment() {
        //点击发送按钮之后应该隐藏编辑框
        hideComment();
        //先判断用户ID
        if (MyApp.users == null) {
            Toast.makeText(NewsDetailActivity.this, "你已经处于离线状态,请稍后重试！", Toast.LENGTH_SHORT).show();
            return;
        }
        String content = edit_comment.getText().toString().trim();
        if (content != null && !"".equals(content)) {
            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("content", content);
            params.addBodyParameter("uid", MyApp.users.getUid() + "");
            params.addBodyParameter("nid", nid);
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "NewsServlet?action=comment", params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    //评论成功
                    //将数据 加到评论列表中
                    if ("1".equals(responseInfo.result)) {
                        //评论成功
                        Toast.makeText(NewsDetailActivity.this, "评论成功！", Toast.LENGTH_SHORT).show();
                        initData(); //刷新试图
                    } else {
                        //评论失败
                        Toast.makeText(NewsDetailActivity.this, "评论失败！", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(NewsDetailActivity.this, "网络异常,请稍后重试！", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(NewsDetailActivity.this, "请输入评论内容！", Toast.LENGTH_SHORT).show();
        }

    }

    private void zan() {
        //点赞操作 不可取消
        if (MyApp.users != null) {
            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("uid", MyApp.users.getUid() + "");
            params.addBodyParameter("nid", nid);
            if (!cb_zan.isChecked()) {
                httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "NewsServlet?action=zan", params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if ("1".equals(responseInfo.result)) {
                            cb_zan.setChecked(true);
                            tv_zannum.setText(Integer.valueOf(tv_zannum.getText().toString()) + 1 + "");
                            Toast.makeText(NewsDetailActivity.this, "赞成功!", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(NewsDetailActivity.this, "操作失败!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(NewsDetailActivity.this, "网络异常，请稍后重试!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                //取消
                Toast.makeText(NewsDetailActivity.this, "已经赞过了，不用再赞了", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(NewsDetailActivity.this, "你已经处于离线状态，请重新登录！", Toast.LENGTH_SHORT).show();
        }

    }

    private void initComment() {
        //点击评论按钮 事件
        ll_comment_hide.setVisibility(View.VISIBLE);
        //弹出输入框
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        edit_comment.setFocusableInTouchMode(true);
        edit_comment.setFocusable(true);
        edit_comment.requestFocus();

    }

    private void hideComment() {
        Log.v("tp", "隐藏评论框");
        if (ll_comment_hide.getVisibility() == View.VISIBLE) {
            ll_comment_hide.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) { //监听按下事件
            View v = getCurrentFocus(); //得到当前获取焦点的View
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm != null) {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            } else {
                //隐藏评论框
                hideComment();
            }


            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            //如果点击的是编辑框
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop); //获取当前编辑框的左上角
            int left = leftTop[0];
            int top = leftTop[1];

            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            /**
             * 确定了编辑框区域
             * */


            //判断焦点区域
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false; //不隐藏输入法
            } else {

                return true;
            }
        }
        return false;
    }

    private class ImgOCL implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String url = (String) v.getTag();
            if (url != null) {
                //可以预览
                startActivity(new Intent(NewsDetailActivity.this, PreViewActivity.class).putExtra("pname", url));
            } else {
                return;
            }
        }
    }
}