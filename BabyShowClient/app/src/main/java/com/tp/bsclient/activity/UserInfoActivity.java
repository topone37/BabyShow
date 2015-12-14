package com.tp.bsclient.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
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

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Administrator on 2015/11/27.
 */
public class UserInfoActivity extends Activity implements View.OnClickListener, Spinner.OnItemSelectedListener {
    private final static int GALLERY_REQUEST_CODE = 0x106;
    private final static int CAMERA_REQUEST_CODE = 0x107;

    File sdCard = Environment.getExternalStorageDirectory();
    File photoDir = new File(sdCard, "photo_cache");// 缓存根目录

    private LinearLayout ll_back_info;
    private RelativeLayout rl_head;
    private PopupWindow pop;//点击添加弹出的popupWindow
    private LinearLayout ll_pop;
    // 存放裁剪后照片的地址Uri
    private Uri uri = null;
    private View viewParent;
    private Handler handler;

    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private ProgressDialog progressDialog;

    // 调用相机是 指定的照片名字
    private String imgNames = null;
    private File file = null;


    private ImageView civ_head; //头像
    private EditText edit_nickname; //昵称
    private EditText edit_intro;//简介
    private Spinner sn_sex; //性别
    private String sex;
    private TextView tv_save; //保存按钮


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        viewParent = getLayoutInflater().inflate(R.layout.activity_userinfo, null);
        setContentView(viewParent);
        initView(viewParent);
        initData();

        this.handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                if (msg.what == 1) {
                    Toast.makeText(UserInfoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                    //将头像设置上去
                    MyApp.users.setHead(imgNames);//更新当前的用户头像

                    civ_head.setImageURI(uri);

                    //刷新用户信息
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(MyApp.users.getUname(), "千山暮雪", Uri.parse(UrlConst.PHOTO_URL + MyApp.users.getHead())));
                    }

                } else if (msg.what == 0) {
                    Toast.makeText(UserInfoActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserInfoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void initView(View v) {
        imageLoader = ImageLoader.getInstance();
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.shape_trans_style) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.shape_trans_style) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .build(); // 构建完成
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("头像上传中...");
        imgNames = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
        // 指定存放图片的地址 (以日期为文件名称)
        file = new File(photoDir, imgNames);
        // 将照相机返回图片的存放地址转换成Uri
        uri = Uri.fromFile(file);


        //返回按钮
        this.ll_back_info = (LinearLayout) v.findViewById(R.id.ll_back_info);
        this.ll_back_info.setOnClickListener(this);
        //头像
        this.civ_head = (ImageView) v.findViewById(R.id.civ_head);

        this.rl_head = (RelativeLayout) v.findViewById(R.id.rl_head);
        this.rl_head.setOnClickListener(this);
        //昵称
        this.edit_nickname = (EditText) findViewById(R.id.edit_nickname);
        //简介
        this.edit_intro = (EditText) findViewById(R.id.edit_intro);
        //性别
        this.sn_sex = (Spinner) findViewById(R.id.sn_sex);
        this.sn_sex.setOnItemSelectedListener(this);
        //保存
        this.tv_save = (TextView) findViewById(R.id.tv_save);
        this.tv_save.setOnClickListener(this);


        initPopupWindow();
    }


    private void initData() {
        //从全局变量中取出对应信息设置上
        if (MyApp.users != null) {
            //各种设置
            Log.v("tp", "Userinfo Activity head info : " + MyApp.users.getHead());
            imageLoader.displayImage(UrlConst.PHOTO_URL + MyApp.users.getHead(), civ_head, options);//头像异步加载
            this.edit_nickname.setText(MyApp.users.getNickname());
            this.edit_intro.setText(MyApp.users.getIntro());
            if (MyApp.users.getSex().equals("男")) {
                this.sn_sex.setSelection(1);
            }
        } else {
            Toast.makeText(UserInfoActivity.this, "你已经处于离线状态，请重新登录！",
                    Toast.LENGTH_SHORT).show();
        }


    }

    private void initPopupWindow() {
        pop = new PopupWindow(UserInfoActivity.this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back_info:
//                finish();
                startActivity(new Intent(UserInfoActivity.this, MainActivity.class));
                break;

            case R.id.rl_head:
                //弹出PopupWindow
                ll_pop.startAnimation(AnimationUtils.loadAnimation(UserInfoActivity.this, R.anim.activity_translate_in));
                pop.showAtLocation(viewParent, Gravity.BOTTOM, 0, 0);
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
            case R.id.tv_save:
                //更新信息
                save();
                break;
            default:
                break;
        }


    }


    private void takePhoto() {
        // 拍照
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
                    file = new File(UriUtil.getRealFilePath(UserInfoActivity.this, uri)); //头像对应的真实地址
                    new Thread(new UpHead()).start();
                } else {
                    Toast.makeText(UserInfoActivity.this, "相册获取图片失败！",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    //将头像设置上
                    file = new File(UriUtil.getRealFilePath(UserInfoActivity.this, uri)); //头像对应的真实地址
                    new Thread(new UpHead()).start();
                } else {
                    Toast.makeText(UserInfoActivity.this, "相机获取图片失败！",
                            Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(UserInfoActivity.this, getResources().getStringArray(R.array.sex)[position], Toast.LENGTH_SHORT).show();
        sex = getResources().getStringArray(R.array.sex)[position];

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(UserInfoActivity.this, "记得选择性别哦！", Toast.LENGTH_SHORT).show();

    }

    private void save() {
        HttpUtils httputil = new HttpUtils();
        RequestParams params = new RequestParams();

        final String nickname = edit_nickname.getText().toString();
        final String intro = edit_intro.getText().toString();
        params.addBodyParameter("uid", MyApp.users.getUid() + "");
        params.addBodyParameter("nickname", nickname);
        params.addBodyParameter("intro", intro);
        params.addBodyParameter("sex", sex);
        httputil.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "UserServlet?action=updateInfo", params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if ("1".equals(responseInfo.result)) {
                    Toast.makeText(UserInfoActivity.this, "信息修改成功", Toast.LENGTH_SHORT).show();
                    MyApp.users.setNickname(nickname);
                    MyApp.users.setSex(sex);
                    MyApp.users.setIntro(intro);
                } else {
                    Toast.makeText(UserInfoActivity.this, "信息修改失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(UserInfoActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class UpHead implements Runnable {

        @Override
        public void run() {
            // 获取一个HttpClient对象
            HttpClient client = new DefaultHttpClient();
            // 定义一个HttpPost对象
            HttpPost httpPost = new HttpPost(UrlConst.BASE_URL + "UserServlet?action=updateHead");
            try {
                // 定义一个 MultipartEntity 对象 用于封装传递对象
                //用户添加的动态
                MultipartEntity entity = new MultipartEntity();

                FileBody fileBody = new FileBody(file);

                Log.v("tp", "file: [ path ] " + file.getAbsolutePath());
                // 将图片添加到该对象中
                entity.addPart("file", fileBody);

                //用户ID  ->此处应该判断用户是否已经离线 或者未登录
                Users users = ((MyApp) getApplication()).users;
                if (users == null) {
                    //离线装载
                    Toast.makeText(UserInfoActivity.this, "离线", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
                } else {
                    int uid = users.getUid();
                    StringBody stringBody01 = new StringBody(uid + "",
                            Charset.forName("utf-8"));
                    entity.addPart("uid", stringBody01);
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