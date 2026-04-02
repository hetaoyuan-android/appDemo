/**
 * GroupControl
 * DrawableHelper.java
 * 2017-9-14
 * Copyright (c) Genew Technologies 2010-2016. All rights reserved.
 * 
 */
package com.example.test.widget;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.EmptySignature;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.util.Util;
import com.example.test.AppApplication;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * TODO Add class comment here<p/>
 * @version 1.0.0
 * @since 1.0.0
 * @author liangjiahao
 * @history<br/>
 * ver    date       author desc
 * 1.0.0  2017-9-14  liangjiahao    created<br/>
 * <p/> 
 */
public class DrawableHelper
{


	public static final int HEAD_PICTURE_RADIUS = 5;

	/**
     * 安全的创建bitmap;如果新建 Bitmap 时产生了 OOM，可以主动进行一次 GC - System.gc()，然后再次尝试创建
     * 来源 https://github.com/QMUI/QMUI_Android
     * @param width
     * @param height
     * @param config
     * @param retryCount
     * @return
     */
    public static Bitmap createBitmapSafely(int width, int height, Bitmap.Config config, int retryCount) {
        try {
            return Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (retryCount > 0) {
                System.gc();
                return createBitmapSafely(width, height, config, retryCount - 1);
            }
            return null;
        }
    }
    
    /**
     * 由一个drawable生成bitmap
     * 来源 https://github.com/QMUI/QMUI_Android
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable)
    {
        if (drawable == null)
		{
			return null;
		} else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();

        if (!(intrinsicWidth > 0 && intrinsicHeight > 0))
		{
			return null;
		}

        try {
            Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
    
    /**
     * 创建一张指定大小的纯色图片，支持圆角
     * 来源 https://github.com/QMUI/QMUI_Android
     * @param resources    Resources对象，用于创建BitmapDrawable
     * @param width        图片的宽度
     * @param height       图片的高度
     * @param cornerRadius 图片的圆角，不需要则传0
     * @param filledColor  图片的填充色
     * @return 指定大小的纯色图片
     */
    public static BitmapDrawable createDrawableWithSize(Resources resources, int width, int height, int cornerRadius, int filledColor) 
    {
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

		if (cornerRadius > 0) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(filledColor);
            canvas.drawRoundRect(new RectF(0, 0, width, height), cornerRadius, cornerRadius, paint);
        } else {
            canvas.drawColor(filledColor);
        }
        return new BitmapDrawable(resources, output);
    }

    public static Bitmap decodeFile2Bitmap(File file)
    {
    	BitmapFactory.Options bfOptions=new BitmapFactory.Options();   
    	bfOptions.inDither=false;   
    	bfOptions.inPurgeable=true;   
    	bfOptions.inTempStorage=new byte[12 * 1024];    
    	bfOptions.inJustDecodeBounds = true;   
    	if (!file.exists())
    	{
    		return null;
    	}
    	FileInputStream fs=null;   
    	Bitmap bmp = null;   
    	try {   
    	    fs = new FileInputStream(file);   
    	    if(fs != null)   
    	        bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);   
    	     } catch (Exception e) {   
    	    e.printStackTrace();   
    	}  
    	return bmp;
    }

	public static void showImgWithGlideNoCache(ImageView img, String path,int defaultResource)
	{

		Glide.with(AppApplication.getApp())
				.asBitmap()
				.load("file://" + path)
				.placeholder(defaultResource)
				.skipMemoryCache(true)
				.diskCacheStrategy(DiskCacheStrategy.NONE)
				.into(img);
	}

}
