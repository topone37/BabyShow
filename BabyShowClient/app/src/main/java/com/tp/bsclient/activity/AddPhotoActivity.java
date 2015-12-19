package com.tp.bsclient.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tp.bsclient.R;
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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/11/25.
 */
public class AddPhotoActivity extends Activity implements View.OnClickListener {

    private final static int GALLERY_REQUEST_CODE = 0x106;
    private final static int CAMERA_REQUEST_CODE = 0x107;
    File sdCard = Environment.getExternalStorageDirectory();
    File photoDir = new File(sdCard, "photo_cache");// 缓存根目录
    private String content;
    private PopupWindow pop;//点击添加弹出的popupWindow
    private LinearLayout ll_pop;
    private View view_parent;
    private EditText edit_extra;//内容
    private LinearLayout ll_back_photo; //返回
    private TextView tv_send;//发表按钮
    private Handler handler;
    private ProgressDialog progressDialog;
    private ImageView iv;
    // 存放裁剪后照片的地址Uri
    private Uri uri;

    // 调用相机是 指定的照片名字
    private String imgNames;
    private File file;
    private int aid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 无标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view_parent = getLayoutInflater().inflate(R.layout.activity_addphoto, null);
        setContentView(view_parent);
        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                if (msg.what == 1) {
                    Toast.makeText(AddPhotoActivity.this, "动态添加成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddPhotoActivity.this, MainActivity.class));
                } else if (msg.what == 0) {
                    Toast.makeText(AddPhotoActivity.this, "动态添加失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddPhotoActivity.this, "网络异常,请稍后重试", Toast.LENGTH_SHORT).show();
                }
            }
        };

        initView();

        if (!photoDir.exists()) {
            photoDir.mkdirs();
        }

    }

    private void initView() {
        aid = getIntent().getIntExtra("aid", 0);
        initPopupWindow();//初始化PopupWindow
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setTitle("正在上传");
        this.progressDialog.setMessage("上传中 请稍后....");


        this.ll_back_photo = (LinearLayout) findViewById(R.id.ll_back_photo);
        this.ll_back_photo.setOnClickListener(this);
        this.tv_send = (TextView) findViewById(R.id.tv_add_photo);
        this.tv_send.setOnClickListener(this);
        this.edit_extra = (EditText) findViewById(R.id.edit_extra);
        this.iv = (ImageView) findViewById(R.id.iv_photo);


        imgNames = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
        // 指定存放图片的地址 (以日期为文件名称)
        file = new File(photoDir, imgNames);
        // 将照相机返回图片的存放地址转换成Uri
        uri = Uri.fromFile(file);
        this.iv.setOnClickListener(this);

    }

    private void initPopupWindow() {
        pop = new PopupWindow(AddPhotoActivity.this);

        View view = getLayoutInflater().inflate(R.layout.pop_addnews, null);

        ll_pop = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true);
        pop.setOutsideTouchable(true); //点击空白处显示
        pop.setContentView(view);


        /**
         * 给PopupWindow 的设置上点击事件
         */


        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.rl_parent);//popupWindows
        parent.setOnClickListener(this);

        Button btn_camera = (Button) view.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);

        Button btn_select = (Button) view.findViewById(R.id.btn_select);
        btn_select.setOnClickListener(this);

        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ll_back_photo:
                this.finish();
                break;
            case R.id.tv_add_photo:
                Toast.makeText(AddPhotoActivity.this, "发表", Toast.LENGTH_SHORT).show();
                send();
                break;
            case R.id.rl_parent://点击PopupWindow
                pop.dismiss();
                ll_pop.clearAnimation();
                break;
            case R.id.btn_camera://相机拍照
                takePhoto();
                break;
            case R.id.btn_select: //从相册中选取
                fromGallery();
                break;
            case R.id.btn_cancel:
                pop.dismiss();
                ll_pop.clearAnimation();
                break;
            case R.id.iv_photo:
                //弹出PopupWindow
                ll_pop.startAnimation(AnimationUtils.loadAnimation(AddPhotoActivity.this, R.anim.activity_translate_in));
                pop.showAtLocation(view_parent, Gravity.BOTTOM, 0, 0);
                break;


            default:
                break;
        }
    }

    private void takePhoto() {
        // 拍照
        // 使用意图启动相机拍照
        // 将缓存位置 拿来 存放 拍照的图片 （真实图片）

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // // 将相机的拍照的图片存放到指定URI
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        pop.dismiss();
        ll_pop.clearAnimation();

    }

    private void fromGallery() {
        // 选择是相册中选取
        // 使用意图启动Gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
        pop.dismiss();
        ll_pop.clearAnimation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 如果是从相册中选取照片
        switch (requestCode) {
            case GALLERY_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    uri = data.getData();
                    iv.setImageURI(uri);
                    file = new File(UriUtil.getRealFilePath(AddPhotoActivity.this, uri));
                    if (file.exists()) {
                    }
                } else {
                    Toast.makeText(AddPhotoActivity.this, "没有选择任何照片",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    iv.setImageURI(uri);
                } else {
                    Toast.makeText(AddPhotoActivity.this, "没有选择任何照片！",
                            Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void send() {
        int num = 0;
        //动态内容
        content = edit_extra.getText().toString();
        //判断有几张图片

        if (file.length() != 0) {
            num++;
        }

        if (num == 0) {
            Toast.makeText(AddPhotoActivity.this, "来一张图片吧...", Toast.LENGTH_SHORT).show();
            return;
        } else {
            pop.dismiss();
            ll_pop.clearAnimation();
            progressDialog.show();

            new Thread(new SendThread()).start();
        }

    }

    @Override
    protected void onStop() {
        ImageLoader.getInstance().clearMemoryCache();
        super.onStop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) { //监听按下事件
            View v = getCurrentFocus(); //得到当前获取焦点的View
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
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
                for (int i = 0; i < 6; i++) {
                    if (file.length() != 0) { //如果文件的大小不存在 即 未选择照片
                        FileBody fileBody = new FileBody(file);
                        // 将图片添加到该对象中
                        entity.addPart("file" + i, fileBody);
                    }
                }

                //用户ID  ->此处应该判断用户是否已经离线 或者未登录
                Users users = ((MyApp) getApplication()).users;
                if (users == null) {
                    //离线装载
                    Toast.makeText(AddPhotoActivity.this, "你已经处于离线状态,请重新登录！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddPhotoActivity.this, LoginActivity.class));
                } else {
                    StringBody stringBody01 = new StringBody(aid + "",
                            Charset.forName("utf-8"));
                    entity.addPart("aid", stringBody01);
                    String pcontect = edit_extra.getText().toString().trim();
                    StringBody stringBody02 = new StringBody(pcontect,
                            Charset.forName("utf-8"));
                    entity.addPart("pcontect", stringBody02);
                }


                //用户发表的内容
                StringBody stringBody02 = new StringBody(content,
                        Charset.forName("utf-8"));
                entity.addPart("content", stringBody02);

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