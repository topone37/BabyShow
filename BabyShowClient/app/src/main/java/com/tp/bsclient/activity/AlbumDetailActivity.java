package com.tp.bsclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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
import com.tp.bsclient.adapter.PhotoTimeAdapter;
import com.tp.bsclient.util.UrlConst;

import org.json.JSONArray;
import org.json.JSONException;

public class AlbumDetailActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private int aid;
    private ListView lv_photo;
    private JSONArray array;
    private LinearLayout ll_back_album;
    private TextView tv_noimg;
    private TextView tv_add_p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉 标题栏
        Intent intent = getIntent();
        //根据传递过来的相册ID 获取相片
        aid = intent.getIntExtra("aid", 1);
        Log.e("tp", "aid=>>>>>>>>>>>" + aid);
        setContentView(R.layout.activity_album_detail);
        initView();
        initData();
    }

    private void initView() {
        //初始化试图
        ll_back_album = (LinearLayout) findViewById(R.id.ll_back_album);
        ll_back_album.setOnClickListener(this);
        lv_photo = (ListView) findViewById(R.id.gv_photo);
        lv_photo.setOnItemClickListener(this);
        tv_noimg = (TextView) findViewById(R.id.tv_noimg);
        tv_add_p = (TextView) findViewById(R.id.tv_add);
        tv_add_p.setOnClickListener(this);


    }

    private void initData() {
        //初始化数据
        //根据获取的相册ID获取相册图片
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("aid", aid + "");
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "AlbumServlet?action=getPhoto", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //获取相片成功
                String rs = responseInfo.result;
                if (!"".equals(rs)) {
                    try {
                        //准备数据
                        array = new JSONArray(rs);
                        if (array.length() != 0) {
                            tv_noimg.setVisibility(View.GONE);
                            //有数据 才去 装载适配器
                            //装载适配器
                            PhotoTimeAdapter adapter = new PhotoTimeAdapter(AlbumDetailActivity.this, array);
                            //设置适配器
                            lv_photo.setAdapter(adapter);
                        } else {
                            tv_noimg.setVisibility(View.VISIBLE);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(AlbumDetailActivity.this, "网络异常，获取信息异常！" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //点击了图片
        try {
            String[] imgnames = new String[array.length()];
            //将有所有图片传入
            for (int i = 0; i < array.length(); i++) {
                imgnames[i] = array.getJSONObject(i).optString("pname");

            }
            //跳转到图片预览页
            //将 图片名字传入
            startActivity(new Intent(AlbumDetailActivity.this, PreViewActivity.class).putExtra("pname", imgnames).putExtra("curr", position));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(AlbumDetailActivity.this, "图片无法获取！", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back_album:
                finish();
                break;
            case R.id.tv_add:
                //添加动态
                startActivity(new Intent(AlbumDetailActivity.this, AddPhotoActivity.class).putExtra("aid", aid));
                break;
            default:
                break;
        }
    }


}
