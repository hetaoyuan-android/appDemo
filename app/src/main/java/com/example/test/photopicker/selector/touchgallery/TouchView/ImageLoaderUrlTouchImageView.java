package com.example.test.photopicker.selector.touchgallery.TouchView;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.test.R;
import com.example.test.photopicker.selector.ImageLoaderManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
public class ImageLoaderUrlTouchImageView extends RelativeLayout {
    protected ProgressBar mProgressBar;
    protected TouchImageView mImageView;

    protected Context mContext;
	private ImageLoader mImageLoader;

    public ImageLoaderUrlTouchImageView(Context ctx)
    {
        super(ctx);
        mContext = ctx;
        init();

    }
    public ImageLoaderUrlTouchImageView(Context ctx, AttributeSet attrs)
    {
        super(ctx, attrs);
        mContext = ctx;
        init();
    }
    public TouchImageView getImageView() { return mImageView; }

    protected void init() {
        mImageView = new TouchImageView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mImageView.setLayoutParams(params);
        this.addView(mImageView);
        mProgressBar = new ProgressBar(mContext);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(30, 0, 30, 0);
        mProgressBar.setLayoutParams(params);
        this.addView(mProgressBar);
        mImageLoader = ImageLoaderManager.getImageLoader(mContext);
    }

    public void setUrl(String imageUrl)
    {
    	 mImageLoader.displayImage("file://"+imageUrl, mImageView,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				mImageView.setImageResource(R.drawable.pic_default_pic);
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				mProgressBar.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
			}
		});
    }
    
    public void setScaleType(ScaleType scaleType) {
        mImageView.setScaleType(scaleType);
    }
    
}
