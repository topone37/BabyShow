package com.tp.bsclient.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.tp.bsclient.R;
import com.tp.bsclient.application.MyApp;
import com.tp.bsclient.util.UrlConst;
import com.tp.bsclient.view.ZoomImageView;

/**
 * 大图预览 功能描述：一般我们浏览一个应用，别人发布的状态或新闻都会有很多配图， 我们点击图片时可以浏览大图，
 * 这张大图一般可以放大，放大到超过屏幕后
 * 可以移动 需要从activity的Intent传参数过来 传入参数：url 大图下载地址 smallPath 缩略图存在本地的地址
 *
 * @author Administrator
 */
public class PreViewActivity extends Activity implements ZoomImageView.IChangePicListener {
    int widthPixels;
    int heightPixels;
    private ZoomImageView zoomView;
    private String[] pname; //图片的名字
    private int currItem;
    private ImageSize imageSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        zoomView = (ZoomImageView) findViewById(R.id.zoom_view);
        zoomView.setonChangePicListenetr(this);
        /* 通过Intent获取图片的下载地址 */
        pname = getIntent().getStringArrayExtra("pname");
        currItem = getIntent().getIntExtra("curr", 1);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        imageSize = new ImageSize(widthPixels, heightPixels);
        widthPixels = metrics.widthPixels;
        heightPixels = metrics.heightPixels;
        initPic();

    }

    private void initPic() {
        if (!pname[currItem].equals("")) {
            Bitmap bitmap = MyApp.imageLoader.loadImageSync(UrlConst.PHOTO_URL + pname[currItem], imageSize, MyApp.options);
            zoomView.setImageBitmap(zoomBitmap(bitmap, widthPixels, heightPixels));
        } else {
            zoomView.setImageBitmap(zoomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.img01), widthPixels, heightPixels));
        }
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

    @Override
    public void prePic() {
        if (currItem > 0) {
            currItem--;
            initPic();
        }
    }

    @Override
    public void nextPic() {
        if (currItem < pname.length - 1) {
            currItem++;
            initPic();
        }
    }
}
