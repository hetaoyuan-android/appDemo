package com.example.test.flowlayout;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;

import java.io.File;
import java.io.FileInputStream;

/**
 * 图片加载工具类
 *
 * Created by newcboy on 2018/1/25.
 */

public class ImageLoadUtils {

    /**
     * 原图加载，根据传入的指定图片大小。
     * @param imagePath
     * @param maxSize
     * @return
     */
    public static Bitmap getImageLoadBitmap(String imagePath, int maxSize){
        int fileSize = 1;
        Bitmap bitmap = null;
        int simpleSize = 1;
        File file = new File(imagePath);
        if (file.exists()) {
            Uri imageUri = Uri.parse(imagePath);
            try {
                fileSize = (int) (getFileSize(file) / 1024);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Options options = new Options();
            if (fileSize > maxSize){
                for (simpleSize = 2; fileSize>= maxSize; simpleSize++){
                    fileSize = fileSize / simpleSize;
                }
            }
            options.inSampleSize = simpleSize;
            bitmap = BitmapFactory.decodeFile(imageUri.getPath(), options);
        }
        return bitmap;
    }


    /**
     * 获取指定文件的大小
     * @param file
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception{
        if(file == null) {
            return 0;
        }
        long size = 0;
        if(file.exists()) {
            FileInputStream mInputStream = new FileInputStream(file);
            size = mInputStream.available();
        }
        return size;
    }


    /**
     * 获取手机运行内存
     * @param context
     * @return
     */
    public static long getTotalMemorySize(Context context){
        long size = 0;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();//outInfo对象里面包含了内存相关的信息
        activityManager.getMemoryInfo(outInfo);//把内存相关的信息传递到outInfo里面C++思想
        //size = outInfo.totalMem;  //总内存
        size = outInfo.availMem;    //剩余内存
        return (size/1024/1024);
    }

}
