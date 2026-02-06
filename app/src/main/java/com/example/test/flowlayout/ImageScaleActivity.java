package com.example.test.flowlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.test.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ImageScaleActivity extends AppCompatActivity {

    private  ZoomImageView zoomImageView;
    private String imagePath = "https://doge.zzzmh.cn/wallpaper/origin/b0fced9bf8864e88bb35b437b72f0c14.jpg";
    private Bitmap bitmap;
    private Bitmap myBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        zoomImageView = findViewById(R.id.zoom_image_view);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final Bitmap bitmap = getBitmap(imagePath);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (null != bitmap) {
//                            zoomImageView.setResourceBitmap(FlowActivity.this, bitmap);
//                        }
//                        Log.e("----", "99999");
//                    }
//                });
//            }
//        }).start();


        //setImagePathBitmap()是从系统中读取图片加载的方法，setResourceBitmap()是从资源文件中读取图片的方法。
//        zoomImageView.setImagePathBitmap(imagePath, 1.0f);
        zoomImageView.setResourceBitmap(ImageScaleActivity.this, R.mipmap.b);


    }



    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
    }
}
