package com.example.test.photopicker.selector;

import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


/**
 * 获取ImageLoader
 *
 */
public class ImageLoaderManager {

	private static ImageLoader imageLoader;

	public static ImageLoader getImageLoader(Context context) {
		if (imageLoader == null) {
			init(context);
		}
		if (!imageLoader.isInited()) {
			init(context);
		}
		return imageLoader;
	}

	private static synchronized void init(Context context) {
		if (imageLoader != null && imageLoader.isInited()) {
			return;
		}
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context);
		builder.tasksProcessingOrder(QueueProcessingType.LIFO);
		builder.memoryCache(new LruMemoryCache((int)(Runtime.getRuntime().maxMemory()/4)));
		builder.memoryCacheSize((int)(Runtime.getRuntime().maxMemory()/4));
		builder.threadPriority(Thread.MAX_PRIORITY-2);
		builder.threadPoolSize(10);
		DisplayImageOptions.Builder dispB = new DisplayImageOptions.Builder();
		dispB.cacheInMemory(true);
		dispB.cacheOnDisk(false);
		builder.defaultDisplayImageOptions(dispB.build());
//		builder.writeDebugLogs();
		ImageLoaderConfiguration build = builder.build();
		imageLoader= ImageLoader.getInstance();
		imageLoader.init(build);
	}

}
