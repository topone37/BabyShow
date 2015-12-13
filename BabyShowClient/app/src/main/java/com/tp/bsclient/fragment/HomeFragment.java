package com.tp.bsclient.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tp.bsclient.R;
import com.tp.bsclient.activity.AdvertiseActivity;
import com.tp.bsclient.activity.AlbumDetailActivity;
import com.tp.bsclient.adapter.AlbumAdapter;
import com.tp.bsclient.adapter.ImgAdapter;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.po.Advertise;
import com.tp.bsclient.util.UrlConst;
import com.tp.bsclient.view.MyGallery;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/6.
 */
public class HomeFragment extends Fragment implements OnItemClickListener, GridView.OnItemLongClickListener, View.OnClickListener {
    int x;// 屏幕的宽
    int y;// 屏幕的高
    private GridView gv_album;//相册GridView
    private Dialog dialog;
    private JSONArray album_array; //相册数据
    //首页的轮播组件
    private MyGallery gallery = null;
    private ArrayList<Advertise> imgList;
    private ArrayList<ImageView> portImg; //点点
    private int preSelImgIndex = 0;
    private LinearLayout ll_focus_indicator_container = null;
    private EditText edit_aname; //新建相册的名字
    private LayoutInflater inflater;
    private int position = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        x = dm.widthPixels;// 屏幕的宽
        y = dm.heightPixels;// 屏幕的高
        this.inflater = inflater;
        initView(view);
        //初始化数海报据
        initPostData();
        //初始化相册数据
        initAlbum();
        return view;
    }

    private void initView(View v) {

        this.gv_album = (GridView) v.findViewById(R.id.gv_album);
        this.gv_album.setOnItemClickListener(this);
        this.gv_album.setOnItemLongClickListener(this);
        this.gallery = (MyGallery) v.findViewById(R.id.gallery); //轮播图
        this.ll_focus_indicator_container = (LinearLayout) v.findViewById(R.id.ll_focus_indicator_container);//对应下面的小数点
    }

    private void initPostData() {
        /**
         *  此处应该联网 获取 数据 初始化轮播图
         */
        imgList = new ArrayList<Advertise>();

        Advertise advertimes = new Advertise();
        advertimes.setId(1 + "");
        advertimes.setUrl("http://pic05.babytreeimg.com/foto3/thumbs/2015/0703/18/0/1df1bace60365d0f4e611645_mb.png");
        advertimes.setContentUrl("http://www.babytree.com/merchant/group/topic.php?id=42575407&pg=1");
        imgList.add(advertimes);

        advertimes = new Advertise();
        advertimes.setId(2 + "");
        advertimes.setUrl("http://pic21.nipic.com/20120601/4935363_083344321392_2.jpg");
        advertimes.setContentUrl("http://www.babytree.com/merchant/group/topic.php?id=42575407&pg=2");
        imgList.add(advertimes);

        advertimes = new Advertise();
        advertimes.setId(3 + "");
        advertimes.setUrl("http://pic7.nipic.com/20100601/4505234_090758007556_2.jpg");
        advertimes.setContentUrl("http://bbs.xiaoyeren.com/thread-214511-1-1.html");
        imgList.add(advertimes);


        ImgAdapter adapter = new ImgAdapter(getActivity(), imgList);
        gallery.setAdapter(adapter);
        gallery.setFocusable(true);
        InitFocusIndicatorContainer();

        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int selIndex, long arg3) {
                if (imgList.size() != 0) {
                    selIndex = selIndex % imgList.size();
                    // 修改上一次选中项的背景
                    portImg.get(preSelImgIndex).setImageResource(R.drawable.shape_bar_normal);
                    // 修改当前选中项的背景
                    portImg.get(selIndex).setImageResource(R.drawable.shape_bar_sel);
                    preSelImgIndex = selIndex;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        gallery.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position", position + "");
                Intent intent = new Intent();
                intent.putExtra("_url", imgList.get(position % imgList.size()).getContentUrl());
                intent.setClass(getActivity(), AdvertiseActivity.class);
                startActivity(intent);
            }

        });


    }


    private void initAlbum() {
        if (MyApp.users == null) {
            Toast.makeText(getActivity(), "你已经处于离线状态,请重新登录!", Toast.LENGTH_SHORT).show();
            return;
        }
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "AlbumServlet?action=getAll&uid=" + MyApp.users.getUid(), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                if (!"".equals(result)) {
                    //相册信息
                    try {

                        //准备数据
                        album_array = new JSONArray(result);
                        //装载数据到适配器上
                        AlbumAdapter adapter = new AlbumAdapter(getActivity(), album_array);
                        //设置相册适配器
                        gv_album.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(getActivity(), "网络异常,相册数据获取失败!" + s, Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void InitFocusIndicatorContainer() {
        //点点
        portImg = new ArrayList<ImageView>();
        for (int i = 0; i < imgList.size(); i++) {
            ImageView localImageView = new ImageView(getActivity());
            localImageView.setId(i);
            ImageView.ScaleType localScaleType = ImageView.ScaleType.FIT_XY;
            localImageView.setScaleType(localScaleType);
            LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(
                    32, 5);
            localImageView.setLayoutParams(localLayoutParams);
            localImageView.setPadding(0, 0, 10, 0);
            if (i == 0) {
                localImageView.setImageResource(R.drawable.shape_bar_sel);
            } else {
                localImageView.setImageResource(R.drawable.shape_bar_normal);
            }
            portImg.add(localImageView);
            this.ll_focus_indicator_container.addView(localImageView);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = ((TextView) view.findViewById(R.id.tv_aid)).getText().toString();
        int aid = Integer.valueOf(str);
        if (aid == 0) {
            View mView = inflater.inflate(R.layout.dialog_newalbum, null);
            Button btn_cancel = (Button) mView.findViewById(R.id.btn_cancel_dialog_new);
            Button btn_confirm = (Button) mView.findViewById(R.id.btn_confirm_dialog_new);
            edit_aname = (EditText) mView.findViewById(R.id.edit_pname);
            btn_confirm.setOnClickListener(this);
            btn_cancel.setOnClickListener(this);
            dialog = new Dialog(getActivity(), R.style.dialog_exit);
            dialog.getWindow().setLayout((int) (x / 2.2), (int) (y / 1.8));
            dialog.setContentView(mView);
            dialog.show();
        } else {
            //跳转 到 对应的Activity界面
            startActivity(new Intent(getActivity(), AlbumDetailActivity.class).putExtra("aid", aid));
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            return false;
        } else {
            this.position = position;
            //删除和重命名相册
            //根据位置 拿到 相册 ID
            View mView = inflater.inflate(R.layout.dialog_re_del, null);
            Button btn_rename = (Button) mView.findViewById(R.id.btn_rename);
            Button btn_del = (Button) mView.findViewById(R.id.btn_del);
            Button btn_cancel = (Button) mView.findViewById(R.id.btn_error);
            btn_rename.setOnClickListener(this);
            btn_del.setOnClickListener(this);
            btn_cancel.setOnClickListener(this);
            dialog = new Dialog(getActivity(), R.style.dialog_exit);
            dialog.getWindow().setLayout((int) (x / 2.2), (int) (y / 1.8));
            dialog.setContentView(mView);
            dialog.show();

        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel_dialog_new:
                dialog.dismiss();
                break;
            case R.id.btn_confirm_dialog_new:
                //创建相册
                creatAlbum();
                break;
            case R.id.btn_rename:
                dialog.dismiss();
                //弹出重命名对话框
                View mView = inflater.inflate(R.layout.dialog_newalbum, null);
                ((TextView) mView.findViewById(R.id.tv_title)).setText("请输入相册新名字！");
                Button btn_confirm = (Button) mView.findViewById(R.id.btn_confirm_dialog_new);
                Button btn_cancel = (Button) mView.findViewById(R.id.btn_cancel_dialog_new);
                final EditText edit_name = (EditText) mView.findViewById(R.id.edit_pname);
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edit_name != null && !edit_name.getText().toString().trim().equals("")) {
                            rename(edit_name.getText().toString());
                        } else {
                            Toast.makeText(getActivity(), "请输入新名称!", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }

                });
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog = new Dialog(getActivity(), R.style.dialog_exit);
                dialog.getWindow().setLayout((int) (x / 2.2), (int) (y / 1.8));
                dialog.setContentView(mView);
                dialog.show();
                break;
            case R.id.btn_del:
                dialog.dismiss();
                //弹出删除对话框
                View view = inflater.inflate(R.layout.dialog_exit, null);
                ((TextView) view.findViewById(R.id.tv_title)).setText("删除相册");
                ((TextView) view.findViewById(R.id.tv_message)).setText("你确定删除这个相册吗？");
                Button btn_ok = (Button) view.findViewById(R.id.btn_confirm_dialog);
                Button btn_no = (Button) view.findViewById(R.id.btn_cancel_dialog);
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delAlbum();
                        dialog.dismiss();
                    }

                });
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog = new Dialog(getActivity(), R.style.dialog_exit);
                dialog.getWindow().setLayout((int) (x / 2.2), (int) (y / 1.8));
                dialog.setContentView(view);
                dialog.show();
                break;
            case R.id.btn_error:
                //去掉
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

    private void creatAlbum() {
        if (edit_aname.getText() != null && !"".equals(edit_aname.getText().toString())) {
            //请求服务器新建相册
            if (MyApp.users == null) {
                Toast.makeText(getActivity(), "你已经处于离线状态,请重新登录!", Toast.LENGTH_SHORT).show();
                return;
            }
            String aname = edit_aname.getText().toString();
            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("aname", aname);
            params.addBodyParameter("uid", MyApp.users.getUid() + "");
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "AlbumServlet?action=add", params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    String rs = responseInfo.result;
                    if ("1".equals(rs)) {
                        //新建相册成功
                        //刷新相册信息
                        initAlbum();
                    } else {
                        Toast.makeText(getActivity(), "相册添加失败!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
            dialog.dismiss();
        } else {
            Toast.makeText(getActivity(), "请输入相册名称", Toast.LENGTH_SHORT).show();
        }
    }

    private void rename(final String s) {
        try {
            int aid = album_array.getJSONObject(position).optInt("aid");
            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("aid", aid + "");
            params.addBodyParameter("aname", s);
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "AlbumServlet?action=rename", params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if ("1".equals(responseInfo.result)) {
                        initAlbum();
                        Toast.makeText(getActivity(), "重命名成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "重命名失败！", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "JSONException", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void delAlbum() {
        try {
            int aid = album_array.getJSONObject(position).optInt("aid");
            HttpUtils httpUtils = new HttpUtils();
            RequestParams params = new RequestParams();
            params.addBodyParameter("aid", aid + "");
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlConst.BASE_URL + "AlbumServlet?action=del", params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    if ("1".equals(responseInfo.result)) {
                        initAlbum();
                        Toast.makeText(getActivity(), "删除成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "JSONException", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}