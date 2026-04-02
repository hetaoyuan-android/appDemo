package com.example.test.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;


import com.example.test.AppApplication;

import java.io.File;

/**
 * 常量
 * @author LWH
 *
 */
public class Constans {
	/**
	 * 	SD卡路径
	 */
	private static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

	/**
	 * 	图片路径
	 */
	public static final String PHOTO_FOLDER = SD_PATH + "/image";

	/**
	 * 	视频路径
	 */
	public static final String VIDEO_FOLDER = SD_PATH + "/video";

	/**
	 * 	视频回传路径
	 */
	public static final String VIDEO_UPLOAD_FOLDER = SD_PATH + "/videoUpload";

	/**
	 *  录音路径
	 */
	public static final String AUDIO_FOLDER = SD_PATH + "/audio";

	/**
	 *  文件
	 */
	public static final String FILE_FOLDER = SD_PATH + "/file";
	
	/**
	 * 多媒体
	 */
	public static final String MULTIMEDIA_FOLDER = SD_PATH + "/multimedia";

	/**
	 * 头像
	 */
	public static final String HEADPICTURE_FOLDER = SD_PATH + "/headpicture";
	
	/**
	 * 牛信版本
	 */
	public static final String NUCHAT_VERSION_FILE = SD_PATH + "/ccmVersion";
	

	/**
	 *  视频回传日志路径
	 */
	static final String UPVIDEOLOD_FOLDER = SD_PATH + "/ccmvideo/MIPCNetLib.log";
	
//	/**
//	 * 牛信头像裁剪相册
//	 */ 没用到
//	public static final String NU_PORTRAIT_CROP = SD_PATH + "/nuCropPortrait";
	
	/**
	 * 牛信头像缓存文件夹,下载头像实际缓存路径
	 */
	public static final String NU_PORTRAINT_CACHE = SD_PATH + "/cacheheadpicture/";

	/**
	 * 直播连麦音频缓冲文件夹
	 */
	public static String getLiveAudioFolder(Context context) {
		return context.getCacheDir() + "/LiveAudioMessage/";
	}

	/**
	 *  百度地图的路径
	 */
	private static final String BAIDU_MAP_FOLDER = SD_PATH + "/BaiduMapSDKNew/";

	/**
	 *Andtroid 10 的地图路径
	 */
	private static final String BAIDU_MAP_VERSION_Q_FOLDER = SD_PATH + "/Android/data/" + AppApplication.getApp().getPackageName() + "/files/BaiduMapSDKNew/";

	public static final String BAIDU_NAVIGATION_FOLDER_NAME = "BNSDK";

	public static final String APP_FOLDER_NAME = "Nuchat";

	public static String getBaiduMapFolder()
	{
		if(Build.VERSION.SDK_INT >= 29)
		{
			return BAIDU_MAP_VERSION_Q_FOLDER;
		}else
		{
			return BAIDU_MAP_FOLDER;
		}
	}

	public static String getSdcardDir(Context context) {
		if (Build.VERSION.SDK_INT >= 29) {
			// 如果外部储存可用 ,获得外部存储路径
			File file = context.getExternalFilesDir(null);
			if (file != null && file.exists()) {
				return file.getPath();
			} else {
				return context.getFilesDir().getPath();
			}
		} else {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
	}
}
