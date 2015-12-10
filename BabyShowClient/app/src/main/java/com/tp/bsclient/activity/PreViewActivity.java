package com.tp.bsclient.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.tp.bsclient.R;
import com.tp.bsclient.util.UrlConst;
import com.tp.bsclient.view.ZoomImageView;

/**
 * 大图预览 功能描述：一般我们浏览一个应用，别人发布的状态或新闻都会有很多配图， 我们点击图片时可以浏览大图，
 * 这张大图一般可以放大，放大到超过屏幕后
 * 可以移动 需要从activity的Intent传参数过来 传入参数：url 大图下载地址 smallPath 缩略图存在本地的地址
 *
 * @author Administrator
 */
public class PreViewActivity extends Activity {
    private ZoomImageView zoomView;
    private GestureDetector gestureDetector;
    private String pname; //图片的名字
    private ImageLoader imageLoader;
    private DisplayImageOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        imageLoader = ImageLoader.getInstance();
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.shape_trans_style) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.shape_trans_style) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .build(); // 构建完成
        zoomView = (ZoomImageView) findViewById(R.id.zoom_view);
        /* 通过Intent获取图片的下载地址 */
        pname = getIntent().getStringExtra("pname");
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int widthPixels = metrics.widthPixels;
        final int heightPixels = metrics.heightPixels;

        if (!"".equals(pname)) {
            ImageSize imageSize = new ImageSize(widthPixels, heightPixels);
            Bitmap bitmap = imageLoader.loadImageSync(UrlConst.PHOTO_URL + pname, imageSize, options);
//            imageLoader.displayImage(UrlConst.PHOTO_URL + pname, zoomView, options); //将图片加载
            zoomView.setImageBitmap(zoomBitmap(bitmap, widthPixels, heightPixels));
        } else {
            zoomView.setImageBitmap(zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img01), widthPixels, heightPixels));
        }
//        zoomView.setImageBitmap(zoomBitmap(
//                BitmapFactory.decodeResource(getResources(), R.drawable.img01), widthPixels,
//                heightPixels));
        gestureDetector = new GestureDetector(this,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2,
                                           float velocityX, float velocityY) {
                        float x = e2.getX() - e1.getX();
                        if (x > 0) {
                            prePicture();
                        } else if (x < 0) {

                            nextPicture();
                        }
                        return true;
                    }
                });
    }

    protected void nextPicture() {
        Toast.makeText(PreViewActivity.this, "下一个", Toast.LENGTH_SHORT).show();

    }

    protected void prePicture() {
        Toast.makeText(PreViewActivity.this, "上一个", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recycle();
    }

    public void recycle() {
        if (zoomView != null && zoomView.getDrawable() != null) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) zoomView
                    .getDrawable();
            if (bitmapDrawable != null && bitmapDrawable.getBitmap() != null
                    && !bitmapDrawable.getBitmap().isRecycled())

            {
                bitmapDrawable.getBitmap().recycle();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        if (bitmap == null)
            return bitmap;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        if (scaleWidth < scaleHeight) {
            matrix.postScale(scaleWidth, scaleWidth);
        } else {
            matrix.postScale(scaleHeight, scaleHeight);
        }
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }
}
