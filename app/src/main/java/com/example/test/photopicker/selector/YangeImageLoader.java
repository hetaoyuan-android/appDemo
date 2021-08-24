package com.example.test.photopicker.selector;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * 获取YangeImageLoader对象的唯一方式 getInstance
 * 
 *
 */
public class YangeImageLoader {
	private static YangeImageLoader mImageLoader;

	private YangeImageLoader(int threadCount, TaskType type) {
		init(threadCount, type);
	}

	/**
	 * 初始化
	 * 
	 * @param threadCount
	 * @param type
	 */
	private void init(int threadCount, TaskType type) {
		initPoolThread();
		initLruCache();
		// 初始化线程池
		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mTaskQueue = new LinkedList<Runnable>();
		mType = type;
		mSemaphorePoolTread = new Semaphore(threadCount);
	}

	/**
	 * 初始化LruCache
	 */
	private void initLruCache() {
		// 最大内存缓存
		int maxCacheMemory = (int) (Runtime.getRuntime().maxMemory() /4);
		mLruCache = new LruCache<String, Bitmap>(maxCacheMemory) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	/*
	 * 初始化后台轮询线程，负责接收其他线程发送过来的任务
	 */
	private void initPoolThread() {
		// 后台轮询线程
		mPoolThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				mPoolThreadHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case NEW_TASK:
							Runnable task = getTask();
							if (task!=null) {
								mThreadPool.execute(task);
							}
							break;

						default:
							break;
						}
					}

				};
				Looper.loop();
			}
		});
		mPoolThread.start();
		//发出信号
		mSemaphore4PoolThreadHandler.release();
	}

	/**
	 * 获取任务队列中的任务
	 * 
	 * @return
	 */
	private Runnable getTask() {
		if (mTaskQueue.isEmpty()) {
			return null;
		}
		switch (mType) {
		case FIFO:
			return mTaskQueue.removeFirst();
		case LIFO:
			return mTaskQueue.removeLast();
		default:
			break;
		}
		return null;
	}

	/**
	 * 图片的内存缓存
	 */
	private LruCache<String, Bitmap> mLruCache;
	/**
	 * 执行任务的线程池
	 */
	private ExecutorService mThreadPool;
	/**
	 * 线程池默认线程个数
	 */
	private static final int DEFAULT_THREAD_COUNT = 15;
	/**
	 * 新任务消息通知
	 */
	private static final int NEW_TASK = 0x01;
	/**
	 * 任务队列的调度方式 默认FIFO
	 */
	private TaskType mType = TaskType.FIFO;
	/**
	 * 任务队列
	 */
	private LinkedList<Runnable> mTaskQueue;
	/**
	 * 轮询线程，不断的从任务队列中取任务
	 */
	private Thread mPoolThread;
	private Handler mPoolThreadHandler;
	/**
	 * 初始化0个信号量,用于同步mPoolThreadHandler的初始化
	 */
	private Semaphore mSemaphore4PoolThreadHandler = new Semaphore(0);
	private Semaphore mSemaphorePoolTread ;
	/**
	 * 用于图片显示
	 */
	private Handler mUIHandler;

	/**
	 * 获取YangeImageLoader对象的唯一方式
	 * 
	 * @return
	 */
	public static YangeImageLoader getInstance() {
		if (mImageLoader == null) {
			synchronized (YangeImageLoader.class) {
				if (mImageLoader == null) {
					mImageLoader = new YangeImageLoader(DEFAULT_THREAD_COUNT, TaskType.FIFO);
				}
			}
		}
		return mImageLoader;
	}

	public enum TaskType {
		FIFO, LIFO;
	}

	/**
	 * 
	 * @param path
	 * @param iv
	 */
	public void loadImage(final String path, final ImageView iv) {
		iv.setTag(path);
		if (mUIHandler == null) {
			mUIHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// 为iv设置图片
					ImageHolder holder = (ImageHolder) msg.obj;
					if (holder.imageView.getTag().toString().equals(holder.path)) {
						holder.imageView.setImageBitmap(holder.bitmap);
					}
				}
			};
		}
		Bitmap bm = getBitmapFromLruCache(path);
		if (bm != null) {
			refreshBitmap(path, iv, bm);
		} else {
			addTask(new Runnable() {

				@Override
				public void run() {
					// 获取图片的大小
					ImageViewSize size = getImageSize(iv);
					//压缩图片
					Bitmap bm = decodeSampledBitmapFromPath(path, size);
					//把图片放到缓存
					addBitmap2LruCache(path,bm);
					refreshBitmap(path, iv, bm);
					mSemaphorePoolTread.release();
				}
				
			});
		}
	}
	/**
	 * 更新图片
	 * @param path
	 * @param iv
	 * @param bm
	 */
	private void refreshBitmap(final String path, final ImageView iv, Bitmap bm) {
		Message msg = mUIHandler.obtainMessage();
		ImageHolder imageHolder = new ImageHolder();
		imageHolder.bitmap = bm;
		imageHolder.imageView = iv;
		imageHolder.path = path;
		msg.obj = imageHolder;
		mUIHandler.sendMessage(msg);
	}
	/**
	 * 
	 * @param path
	 * @param bm
	 */
	protected void addBitmap2LruCache(String path, Bitmap bm) {
		if (path!=null) {
			if (bm!=null) {
				mLruCache.put(path, bm);
			}
		}
	}

	/**
	 * 根据图片的大小压缩图片
	 * 
	 * @param path
	 * @param size
	 * @return
	 */
	protected Bitmap decodeSampledBitmapFromPath(String path, ImageViewSize size) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int heightSample = options.outHeight / size.height;
		int widthSample = options.outWidth / size.width;
		options.inSampleSize = heightSample > widthSample ? heightSample : widthSample;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	/**
	 * 获取ImageView控件的宽和高
	 * 
	 * @param iv
	 * @return
	 */
	protected ImageViewSize getImageSize(ImageView imageView) {
		ImageViewSize imageViewSize = new ImageViewSize();
		LayoutParams lp = imageView.getLayoutParams();
		int width = imageView.getWidth();
		if (width<=0) {
			width = lp.width;
		}
		if (width<=0) {
			width = getImageViewFiledValue(imageView, "mMaxWidth");
		}
		if (width<=0) {
			DisplayMetrics metrics = imageView.getContext().getResources().getDisplayMetrics();
			width = metrics.widthPixels;
		}
		int height = imageView.getHeight();
		if (height<=0) {
			height = lp.height;
		}
		if (height<=0) {
			height = getImageViewFiledValue(imageView, "mMaxHeight");
		}
		if (height<=0) {
			DisplayMetrics metrics = imageView.getContext().getResources().getDisplayMetrics();
			height = metrics.heightPixels;
		}
		imageViewSize.height = height;
		imageViewSize.width = width;
		return imageViewSize;
	}
	/**
	 * 通过反射获取单位
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public int getImageViewFiledValue(Object obj,String fieldName){
		int value = 0;
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = field.getInt(obj);
			if (fieldValue<=0&&fieldValue<=Integer.MAX_VALUE) {
				value = fieldValue;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return value;
	}

	/**
	 * ImageView的宽和高
	 * 
	 */
	private class ImageViewSize {
		int height;
		int width;
	}

	/**
	 * 将加载图片任务添加到任务队列中
	 * 
	 * @param runnable
	 */
	private void addTask(Runnable runnable) {
		mTaskQueue.add(runnable);
		try {
			if (mPoolThreadHandler==null) {
				mSemaphore4PoolThreadHandler.acquire();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mPoolThreadHandler.sendEmptyMessage(NEW_TASK);
	}

	/**
	 * 从LruCache中获取图片
	 * 
	 * @param path
	 * @return
	 */
	private Bitmap getBitmapFromLruCache(String path) {
		return mLruCache.get(path);
	}

	private class ImageHolder {
		Bitmap bitmap;
		ImageView imageView;
		String path;
	}

}
