package com.tp.bsclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.tp.bsclient.R;
import com.tp.bsclient.adapter.PhotoTimeAdapter;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.po.Users;
import com.tp.bsclient.util.UriUtil;
import com.tp.bsclient.util.UrlConst;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AlbumDetailActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final int CAMERA_REQUEST_CODE = 0x784;
    File sdCard = Environment.getExternalStorageDirectory();
    File photoDir = new File(sdCard, "photo_cache");// 缓存根目录
    File file;// 缓存根目录
    Uri uri;// 缓存根目录
    private int aid;
    private ListView lv_photo;
    private JSONArray array;
    private LinearLayout ll_back_album;
    private TextView tv_noimg;
    private TextView tv_add_p;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉 标题栏
        Intent intent = getIntent();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Toast.makeText(AlbumDetailActivity.this, "照片添加成功！", Toast.LENGTH_SHORT).show();
                    //刷新数据
                    initData();
                } else {
                    Toast.makeText(AlbumDetailActivity.this, "照片添加失败！", Toast.LENGTH_SHORT).show();
                }
            }
        };
        //根据传递过来的相册ID 获取相片
        aid = intent.getIntExtra("aid", 1);
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

        String imgName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
        // 指定存放图片的地址 (以日期为文件名称)
        file = new File(photoDir, imgName);
        // 将照相机返回图片的存放地址转换成Uri
        uri = Uri.fromFile(file);

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
                Toast.makeText(AlbumDetailActivity.this, "网络异常，获取信息异常！" + s, Toast.LENGTH_SHORT).show();
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
                takePhoto();
                break;
            default:
                break;
        }
    }

    private void takePhoto() {
        //调用相机拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // // 将相机的拍照的图片存放到指定URI
        if (uri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            try {
                if (uri != null) {
                    String path = UriUtil.getRealFilePath(AlbumDetailActivity.this, uri);
                    if (file.length() > 0) {
                        new Thread(new SendThread()).start();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SendThread implements Runnable {

        @Override
        public void run() {
            // 获取一个HttpClient对象
            HttpClient client = new DefaultHttpClient();

            // 定义一个HttpPost对象
            HttpPost httpPost = new HttpPost(UrlConst.BASE_URL + "AlbumServlet?action=addPhoto");
            try {
                // 定义一个 MultipartEntity 对象 用于封装传递对象
                //用户添加的动态
                MultipartEntity entity = new MultipartEntity();
                if (file.exists() && file.length() > 0) {
                    FileBody fileBody = new FileBody(file);
                    // 将图片添加到该对象中
                    entity.addPart("file", fileBody);
                } else {
                    return;
                }

                //用户ID  ->此处应该判断用户是否已经离线 或者未登录
                Users users = ((MyApp) getApplication()).users;
                if (users == null) {
                    //离线装载
                    Toast.makeText(AlbumDetailActivity.this, "你已经处于离线状态,请重新登录！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AlbumDetailActivity.this, LoginActivity.class));
                } else {
                    int uid = users.getUid();
                    StringBody stringBody01 = new StringBody(uid + "",
                            Charset.forName("utf-8"));
                    entity.addPart("uid", stringBody01);
                    //属于哪个相册
                    StringBody stringBody02 = new StringBody(aid + "",
                            Charset.forName("utf-8"));
                    entity.addPart("aid", stringBody02);
                }

                httpPost.setEntity(entity);
                HttpResponse httpResponse = client.execute(httpPost);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    int result = Integer.parseInt(EntityUtils.toString(httpResponse
                            .getEntity()));
                    // result 1 (成功) 0 图片 不合格 -1 上传异常失败
                    if (result == 1) {
                        handler.sendEmptyMessage(1);
                    } else {
                        handler.sendEmptyMessage(0);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                System.out.println("<<<<<<<<<<<<异常");
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("<<<<<<<<<<<<异常");
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            }

        }
    }
}
