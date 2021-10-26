package com.example.test.imagetext;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;


import com.google.android.exoplayer2.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 类名称: MoblieUtils
 * 类描述: 工具类，主要针对于手机SD卡的操作
 * 创建人: 焦哲刚
 * 创建时间: 2016/7/18 17:23
 * 修改人： 无
 * 修改时间：无
 * 修改备注: 无
 */
public class MoblieUtils {
	/**
	 * 函数名称: sdk()
	 * 函数描述: 返回当前的sdk
	 * 创建人: 焦哲刚
	 * 创建时间: 2016/7/18 17:01
	 * 修改人：无
	 * 修改时间：无
	 * 修改备注: 无
	 *
	 */
	public static String sdk() {
		return Build.VERSION.SDK;
	}
	/**
	 * 函数名称: isSdPresent()
	 * 函数描述: 正在操作sd卡
	 * 创建人: 焦哲刚
	 * 创建时间: 2016/7/18 17:00
	 * 修改人：无
	 * 修改时间：无
	 * 修改备注: 无
	 *
	 */
	public static boolean isSdPresent()
	{
		if (Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * 函数名称: getSdPath()
	 * 函数描述: 获取sd卡的路径
	 * 创建人: 焦哲刚
	 * 创建时间: 2016/7/18 16:59
	 * 修改人：无
	 * 修改时间：无
	 * 修改备注: 无
	 *
	 */
	public static String getSdPath()
	{
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 函数名称: getIMEI()
	 * 函数描述: 获取IMEI
	 * 创建人: 王绪
	 * 创建时间: 2016/7/26 16:59
	 * 修改人：无
	 * 修改时间：2019/7/19 取消权限检查，因为在调用在方法前已进行了权限申请.调用此方法前先申请权限。
	 * 修改备注: 无
	 *
	 */
	@SuppressLint("MissingPermission")
	public static String getIMEI(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Activity.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	@SuppressLint("MissingPermission")
	public static String getAndroidID(Context context) {
		String androidID = Settings.System.getString(
				context.getContentResolver(), Settings.Secure.ANDROID_ID);
		return androidID;
	}

	/**
	 * 函数名称: dp2px()
	 * 函数描述: 根据手机的分辨率dp 转成px(像素)
	 * 创建人: 王绪
	 * 创建时间: 2016/7/26 16:59
	 * 修改人：无
	 * 修改时间：无
	 * 修改备注: 无
	 *
	 */
	public static int dp2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 函数名称: px2dp()
	 * 函数描述: 根据手机的分辨率px(像素) 转成dp
	 * 创建人: 王绪
	 * 创建时间: 2016/7/26 16:59
	 * 修改人：无
	 * 修改时间：无
	 * 修改备注: 无
	 *
	 */
	public static int px2dp(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 函数名称: getWindowWidth()
	 * 函数描述: 获取屏幕宽度
	 * 创建人: 王绪
	 * 创建时间: 2016/7/26 16:59
	 * 修改人：无
	 * 修改时间：无
	 * 修改备注: 无
	 *
	 */
	public static int getWindowWidth(Activity activity) {
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.widthPixels;
	}
	/**
	 * 函数名称: getWindowHeight
	 * 函数描述: 获取屏幕高度
	 * 创建人: 魏然
	 * 创建时间: 2016/8/15 0015 15:50
	 * 修改人：无
	 * 修改时间：无
	 * 修改备注: 无
	 *
	 */
	public static int getWindowHeight(Activity activity) {
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.heightPixels;
	}

	public static int getScreenWidth(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	public static int getScreenHeight(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	/**
	  * 函数名称: isXiaomi
	  * 函数描述: 判断手机是否为小米
	  * 创建人: 魏然
	  * 创建时间: 2018/2/5 0005 16:54
	  * 修改人：无
	  * 修改时间：无
	  * 修改备注: 无
	  *
	  */
	public static boolean isXiaomi(){
		String manufacturer = Build.MANUFACTURER;
		if ("Xiaomi".equals(manufacturer)||"xiaomi".equals(manufacturer)){
			return  true;
		}
		return false;
	}

	/**
	 *  流量 情况下 获取手机IP地址
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return "";
	}

	/**
	 * wifi 情况下获取手机IP地址
	 */
	public static String getLocalMacAddress(Context context) {
		String LocalMacAddress = context.getSharedPreferences("config", Context.MODE_PRIVATE).getString("LocalMacAddress", "");
		if(TextUtils.isEmpty(LocalMacAddress)) {
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			LocalMacAddress = info.getMacAddress();
			context.getSharedPreferences("config", Context.MODE_PRIVATE).edit().putString("LocalMacAddress", LocalMacAddress).apply();
		}
		return LocalMacAddress;
	}

	public static void bgAlpha(Activity context, float f) {
		context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		WindowManager.LayoutParams layoutParams = context.getWindow().getAttributes();
		layoutParams.alpha = f;
		context.getWindow().setAttributes(layoutParams);
	}
}
