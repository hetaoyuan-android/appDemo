package com.example.test.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.MessageQueue;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("SimpleDateFormat")
public class CommonUtils {

    private static final String SIMCARD = "SIMCARD";
    private static final String ISDOUBLE = "ISDOUBLE";
    private static final String SIMCARD_1 = "SIMCARD_1";
    private static final String SIMCARD_2 = "SIMCARD_2";
    /**
     * 只能有一个登录用户，登录时覆盖
     */
    public static final String LOGIN_CONTACT_PS_KEY = "LOGIN_CONTACT";

    /**
     * 生成头像名称
     *
     * @param displayName
     * @return
     */
    public static CharSequence getUserImgName(String displayName) {
        if (!TextUtils.isEmpty(displayName) && "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".contains(displayName.subSequence(0, 1))) {
            if (displayName.length() >= 2) {
                return displayName.subSequence(0, 2);

            } else {
                return displayName.subSequence(0, 1);
            }

        } else if (!TextUtils.isEmpty(displayName)) {
            if (displayName.length() >= 2) {
                if ("、".equals(displayName.substring(displayName.length() - 1))) {
                    return displayName.subSequence(displayName.length() - 3,
                            displayName.length() - 1);
                } else {
                    return displayName.subSequence(displayName.length() - 2, displayName.length());
                }
            } else {
                return displayName.subSequence(displayName.length() - 1, displayName.length());
            }
        } else {
            return "用户";
        }
    }

    /**
     * 判断是否为IP地址
     *
     * @param addr
     * @return
     */
    public static boolean isIP(String addr) {
        if (addr.length() < 7 || addr.length() > 15) {
            return false;
        }
        /*
          判断IP格式和范围
         */
        Pattern pat = Pattern.compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");
//		"(25[0-4]|2[0-4]\\d|1\\d\\d|[1-9]\\d|[1-9])\\.((25[0-4]|2[0-4]\\d|1\\d\\d|[1-9]\\d|\\d)\\.){2}(25[0-4]|2[0-4]\\d|1\\d\\d|[1-9]\\d|[1-9])"
        Matcher mat = pat.matcher(addr);

        return mat.matches();
    }

    public static boolean isHostDomain(String address)
    {
        Pattern pat = Pattern.compile("^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$");
        Matcher mat = pat.matcher(address);
        return mat.matches();
    }

    /**
     * 根据文件大小和时间计算速度
     *
     * @param fileNewLen 文件新的长度
     * @param fileOldLen 文件旧的长度
     * @param newTime    文件达到新的长度时候的时间
     * @param oldTime    文件在旧的长度时候的时间
     * @return 传输速度，单位KB/s
     */
    public static Long countSpeek(long fileNewLen, long fileOldLen,
                                  long newTime, long oldTime) {
        if (newTime - oldTime == 0) {
            return 0L;
        } else {
            return ((fileNewLen - fileOldLen) / (newTime - oldTime));
        }
    }

    /**
     * 根据路径获取文件名
     *
     * @param path 路径
     * @return
     */
    public static String getFileName(String path) {
        int start = path.lastIndexOf("/");
        // 显示文件后缀，如果不显示后缀，需要考虑文件名没有后缀的情况
        // int end=path.lastIndexOf(".");
        int end = path.length();
        if (start != -1 && end != -1) {
            return path.substring(start + 1, end);
        } else {
            return null;
        }
    }

    /**
     * 根据文件夹路径，获取文件夹下所有文件并放入List中
     *
     * @param path
     * @return
     */
	/*public static List<FileMessage> find(String path) {
		ArrayList<FileMessage> list = new ArrayList<>();
		// 得到该路径文件夹下所有的文件
		CommonUtils.checkFileExists(path);
		File fileAll = new File(path);
		File[] files = fileAll.listFiles();
		// 将所有的文件存入ArrayList中
		if (files != null)
		{
			for (int i = 0; i < files.length; i++)
				if (files[i].isFile())    //无视子文件夹
				{
					FileMessage filemessage = new FileMessage();
					File file = files[i];
					filemessage.setFileName(file.getName());
					filemessage.setPath(file.getAbsolutePath());
					list.add(filemessage);
				}
		}else {
			list = null;
		}
		// 返回得到的文件列表
		return list;
	}*/

    /**
     * 把byte转换为mb
     *
     * @param b
     * @return
     */
    public static float b2M(long b) {
        return b / 1024 / 1024;
    }

    /**
     * 转换B为KB、MB或GB
     *
     * @param size
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String formatSize(long size) {
        String result;
        if (size < 1024) {
            result = Long.toString(size) + "B";
        } else if (size < 1048576) {
            result = String.format("%.2fKB", (double) size / 1024);
        } else if (size < 1073741824) {
            result = String.format("%.2fMB", (double) size / 1048576);
        } else {
            result = String.format("%.2fGB", (double) size / 1073741824);
        }
        return result;
    }


    /**
     * 时间格式化函数：格式为：yyyy-MM-dd hh:ss
     *
     * @param time : System.currentTimeMillis()
     * @return
     */
    public static String formatDate(long time) {
        Date nowTime = new Date(time);
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd hh:ss");
        return sdFormatter.format(nowTime);
    }

    /**
     * 删除文件夹下的所有内容,包括本文件夹
     *
     * @param path 删除文件路径
     */
    public static void delFileByPath(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File subFile : files) {
                    delFileByPath(subFile.getAbsolutePath());
                }
                file.delete();
            } else {
                file.delete();
            }
        }
    }

    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            return file.delete();
        } else return false;
    }

    /**
     * 取个人信息
     */
	/*public static void getZYAppContact(Context context,String contactId)
	{
		SharedPreferences preferences = context.getSharedPreferences("addressData", context.MODE_PRIVATE);
		if (preferences != null) {	
			String zyAppContact = preferences.getString("ZYAPPCONTACT:"+contactId, "");
			if (zyAppContact.length() > 0) {
				LogUtil.d("hh", "zyAppContact加载");
				byte[] buffer = Base64.decode(zyAppContact, Base64.DEFAULT);
				ByteArrayInputStream bis = new ByteArrayInputStream(buffer);
				try {
					ObjectInputStream ois = new ObjectInputStream(bis);
					ZYApplication.contactInfo = (ContactInfo) ois.readObject();
					LogUtil.d("hh", "取出ZYApplication.contactInfo");
					ois.close();
					bis.close();
				} catch (StreamCorruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}else{
				LogUtil.d("hh", "zyAppContact为空");
			}
		} 
	}*/

    /**
     * 变化时保存配置
     * @param context
     */
	/*public static void saveApplicationInfo(Context context)
	{
		saveZYAppContact(context);
		saveVideoInfo(context);
	}*/

    /**
     * @param context
     */
	/*private static void saveVideoInfo(Context context)
	{
		SharedPreferences preferences = context.getSharedPreferences("addressData", context.MODE_PRIVATE);
		if (preferences != null) 
		{
			Editor editor = preferences.edit();
			editor.putString("videoIp", ZYApplication.videoIp);
			editor.putInt("videoPort", ZYApplication.videoPort);
			editor.commit();
			GLogger.debug("saveVideoInfo,videoIp:{},  videoPort:{}" , ZYApplication.videoIp ,ZYApplication.videoPort);
		}else
		{
			GLogger.error("saveVideoInfo fial, preferences尚未初始化");
		}
	}
	
	private static void readVideoInfo(Context context) 
	{
		SharedPreferences preferences = context.getSharedPreferences("addressData", context.MODE_PRIVATE);
		if (preferences != null) 
		{
			ZYApplication.videoIp = preferences.getString("videoIp", null);
			ZYApplication.videoPort = preferences.getInt("videoPort", 0);
		}else{
			GLogger.error("readVideoInfo fial, preferences尚未初始化");
			}
	}*/



    public static void startNuPhoneService(Context context, String packageName, String ip1,String port1, String account1, String password1,String ip2,String port2, String account2, String password2) {
        Intent serviceIntent = new Intent();
        serviceIntent.setClassName(packageName, "com.genew.gphone.service.AutoRegisterService");
        serviceIntent.putExtra("ip1", ip1);
        serviceIntent.putExtra("port1", port1);
        serviceIntent.putExtra("account1", account1);
        serviceIntent.putExtra("password1", password1);
        serviceIntent.putExtra("ip2", ip2);
        serviceIntent.putExtra("port2", port2);
        serviceIntent.putExtra("account2", account2);
        serviceIntent.putExtra("password2", password2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent);
        } else {
            context.startService(serviceIntent);
        }
    }




    /**
     *
     */
    /*public static void saveContactInfo(String tag,String result){
		// 登录成功
		
		Log.d(tag, "http decrypt response:+——+" + result);

		JsonParser jsonParser = new JsonParser();
		JsonObject json = (JsonObject) jsonParser.parse(result);
		// 保存登录后的个人用户信息
		if (null != json.get("data")) {
			JsonObject data = (JsonObject) json.get("data")
					.getAsJsonObject();
			JsonObject video = data.get("videoServerInfo").getAsJsonObject();
			ZYApplication.videoIp = video.get("ip").getAsString();
			ZYApplication.videoPort = video.get("port").getAsInt();
			if (null != data.get("userInfo") && !"".equals(data.get("userInfo"))) {
				JsonObject userInfo = (JsonObject) data.get(
						"userInfo").getAsJsonObject();
				ZYApplication.contactInfo.setId(userInfo.get("id")
						.getAsString());
				
				ZYApplication.contactInfo.setName(userInfo.get("name")
						.getAsString());
								
				ZYApplication.contactInfo.setDisplayName(userInfo.get(
						"displayName").getAsString());
				
				JsonArray phoneNumbers = (JsonArray) userInfo
						.get("phoneNumbers").getAsJsonArray();

				// 配置linphone
				String ip = SettingManager.getInstance().getValue(SettingKey.SERVER_IP);
				String linphone = SettingManager.getInstance().getKeyValue("linphoneName:" + ZYApplication.contactInfo.getId() + ip);
				String linphonePassword = SettingManager.getInstance().getKeyValue("linphonePassword:" + ZYApplication.contactInfo.getId() + ip);
				String linphoneDomain = SettingManager.getInstance().getKeyValue("linphoneDomain:" + ZYApplication.contactInfo.getId() + ip);
				SettingManager.getInstance().setValue(SettingKey.CALL_SIP_DOMAIN, linphoneDomain);
				SettingManager.getInstance().setValue(SettingKey.CALL_SIP_USER_NAME, linphone);
				SettingManager.getInstance().setValue(SettingKey.CALL_SIP_PASSWORD, linphonePassword);

				List<PhoneNumberInfo> list = new ArrayList<PhoneNumberInfo>();
				for (int i = 0; i < phoneNumbers.size(); i++) {
					JsonObject phone = (JsonObject) phoneNumbers
							.get(i).getAsJsonObject();

					String id = (String) phone.get("id")
							.getAsString();
					Log.d("mac", "登录用户的ID------------->>>："
							+ id);
					String number = (String) phone
							.get("number").getAsString();
					//存储FTP_PHONE
					SettingManager.getInstance().setValue(SettingKey.FTP_PHONE, number);
					
					String type = (String) phone.get("type")
							.getAsString();

					PhoneNumberInfo phoneNumberInfo = new PhoneNumberInfo();
					phoneNumberInfo.setId(id);
					phoneNumberInfo.setNumber(number);
					phoneNumberInfo.setType(type);
					list.add(phoneNumberInfo);
				}
				ZYApplication.contactInfo.setPhoneNumbers(list);
				String extension = userInfo.get("extension").getAsString();
				ZYApplication.contactInfo.setExtension(extension);
				String memo = null;
				try{
					JsonParser parser = new JsonParser();
					JsonObject jsonObj = parser.parse(extension).getAsJsonObject();
					if(jsonObj.has("Memo"))
					{
						memo = jsonObj.get("Memo").getAsString();
					}
					ZYApplication.contactInfo.setMemo(memo);
				}catch(Exception ex)
				{
				}
				
				saveApplicationInfo(ZYApplication.getCurrentContext());
			}
		}
	}*/
    
    /*public static void initApplicationInfo(Context context, String loginUserId)
    {
    	getZYAppContact(context,loginUserId);
    	readVideoInfo(context);
    	// 初始化联系人
		AppContactManager.getInstance().setContext(
				context.getApplicationContext(), ZYApplication.contactInfo.id);
		// 初始化群组
		AppGroupManager.getInstance().setContext(
				context.getApplicationContext(), ZYApplication.contactInfo.id);
		// 初始化组织架构
		AppOrganizationManager.getInstance().setContext(
				context.getApplicationContext(), ZYApplication.contactInfo.id);
		// 数据库设置id
		//FIXME 初始化数据比较乱
		MessageManager.getInstance().setUid(ZYApplication.contactInfo.id);
		MediaUploadManage.getInstance().setUid(ZYApplication.contactInfo.id);

    	// 配置linphone
		String ip = SettingManager.getInstance().getValue(SettingKey.SERVER_IP);
		String linphone = SettingManager.getInstance().getKeyValue("linphoneName:" + ZYApplication.contactInfo.getId() + ip);
		String linphonePassword = SettingManager.getInstance().getKeyValue("linphonePassword:" + ZYApplication.contactInfo.getId() + ip);
		String linphoneDomain = SettingManager.getInstance().getKeyValue("linphoneDomain:" + ZYApplication.contactInfo.getId() + ip);
		SettingManager.getInstance().setValue(SettingKey.CALL_SIP_DOMAIN, linphoneDomain);
		SettingManager.getInstance().setValue(SettingKey.CALL_SIP_USER_NAME, linphone);
		SettingManager.getInstance().setValue(SettingKey.CALL_SIP_PASSWORD, linphonePassword);
    }*/
    
    /*public static String getIntercomGroupIdKey()
    {
    	
    	try{
    		return SettingKey.INTERCOM_GROUP_ID.toString() + ZYApplication.contactInfo.id + SettingManager.getInstance().getValue(SettingKey.SERVER_IP);	
    	}catch(Exception ex){
    		return null;
    	}
    }
    
    public static String getIntercomGroupNameKey()
    {
    	try{
    		return SettingKey.INTERCOM_GROUP_NAME.toString() + ZYApplication.contactInfo.id + SettingManager.getInstance().getValue(SettingKey.SERVER_IP);
    	}catch(Exception ex)
    	{
    		return null;
    	}
    }*/
    public static void checkFileExists(String path) {
        File localDir = new File(path);
        if (!localDir.exists()) {
            localDir.mkdirs();
        }
    }

    public static void checkFileByPathPattern(String path) {
        if (matchPathPattern(path, Constans.AUDIO_FOLDER)) {
            checkFileExists(Constans.AUDIO_FOLDER);
        } else if (matchPathPattern(path, Constans.FILE_FOLDER)) {
            checkFileExists(Constans.FILE_FOLDER);
        } else if (matchPathPattern(path, Constans.PHOTO_FOLDER)) {
            checkFileExists(Constans.PHOTO_FOLDER);
        } else if (matchPathPattern(path, Constans.UPVIDEOLOD_FOLDER)) {
            checkFileExists(Constans.UPVIDEOLOD_FOLDER);
        } else if (matchPathPattern(path, Constans.VIDEO_FOLDER)) {
            checkFileExists(Constans.VIDEO_FOLDER);
        }
    }


///// <summary>
//    /// 电话的状态
//    /// </summary>
//    [DataContract]
//    public enum PhoneState
//    {
//        /// <summary>
//        /// 未预约状态
//        /// </summary>
//        [EnumMember]
//        NotBook = 0x00,
//            /// <summary>
//            /// 未知状态，如：未配置，状态未知等
//            /// </summary>
//            [EnumMember]
//        Unknown = 0x01,
//            /// <summary>
//            /// 故障状态
//            /// </summary>
//            [EnumMember]
//        Fault = 0x02,
//            /// <summary>
//            /// 禁止状态，如：欠费、停机、阻塞等
//            /// </summary>
//            [EnumMember]
//        Forbidden = 0x03,
//            /// <summary>
//            /// 空闲状态
//            /// </summary>
//            [EnumMember]
//        IDLE = 0x04,
//            /// <summary>
//            /// 呼入振铃
//            /// </summary>
//            [EnumMember]
//        CallIn = 0x05,
//            /// <summary>
//            /// 呼出听铃
//            /// </summary>
//            [EnumMember]
//        CallOut = 0x06,
//            /// <summary>
//            /// 通话
//            /// </summary>
//            [EnumMember]
//        InCall = 0x07,
//            /// <summary>
//            /// 呼出测试
//            /// </summary>
//            [EnumMember]
//        CallOutAttempt = 0x08,
//            /// <summary>
//            /// 呼入测试
//            /// </summary>
//            [EnumMember]
//        CallInAttempt = 0x09,
//            /// <summary>
//            /// 正在释放
//            /// </summary>
//            [EnumMember]
//        Releasing = 0x0A,
//            /// <summary>
//            /// 漫游
//            /// </summary>
//            [EnumMember]
//        Vagil = 0x0B,
//            /// <summary>
//            /// 未授权
//            /// </summary>
//            [EnumMember]
//        NoLicense = 0x0C,
//            /// <summary>
//            /// 连续调度态
//            /// </summary>
//            [EnumMember]
//        Schedule,
//            /// <summary>
//            /// 无效状态
//            /// </summary>
//            [EnumMember]
//        Butt
//    }
    public static boolean isValidState(String state) {
        switch (state) {
            case "IDLE":
            case "CallIn":
            case "CallOut":
            case "InCall":
            case "Schedule":
            case "CallOutAttempt":
            case "CallInAttempt":
            case "Releasing":
                return true;
            default:
                return false;
        }
    }


    private static boolean matchPathPattern(String path, String pattern) {
        return pattern.contains(path);
    }

    /**
     * 获取时间
     *
     * @return
     */
    public static String getCurrentDateString() {
        String result = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss",
                Locale.getDefault());
        Date nowDate = new Date();
        result = sdf.format(nowDate);
        return result;
    }

    /**
     * 判断是否为手机号码
     *
     * @param phoneNum
     * @return
     */
    public static boolean isCellPhoneNumber(String phoneNum) {
        String regex = "(\\+\\d+)?1[34578]\\d{9}$";
        return Pattern.matches(regex, phoneNum);
    }

    /**
     * @return 0 成功，-1 失败
     */
	/*public static int replyCallInSMS(Context context,String phoneNum,String displayName)
	{
		if(!isCellPhoneNumber(phoneNum))
		{
			return -1;
		}else{
			ChatItemInfo item = new ChatItemInfo();
			item.mId = System.currentTimeMillis() + "";
			item.cName = displayName;
			item.cType = "0";
			String phone = phoneNum;
			item.cId = phone.substring(0, phone.length() - 1);
			item.mStyle = "1";
			item.content = "您好！我现在不方便接听你的电话，稍后再联系您！";
			item.mType = "2";
			item.time = System.currentTimeMillis() + ""; // 时间
			item.state = "0";
			item.serial = item.time;
//			MessageManager.getInstance().addChatItem(item);
			// 调用系统发送短信功能
			SmsManager smsManager = SmsManager.getDefault();
			List<String> divideContents = smsManager.divideMessage(item.content);
			for (String text : divideContents)
			{
				Intent itSend = new Intent("SENT_SMS_ACTION");
				itSend.putExtra("name", item.cName);
				itSend.putExtra("mId", item.mId);
				PendingIntent sendPI = PendingIntent.getBroadcast(
						context, 0, itSend,
						PendingIntent.FLAG_ONE_SHOT);
				smsManager.sendTextMessage(phoneNum, null, text, sendPI,
						null);
			}
			return 0;
		}
		
	}*/
	
	
	/*public static void logout(String reason)
	{
//		ServiceUtils.closeAlarmService();//停止重连
		SettingManager.getInstance().setValue(SettingKey.FTP_PWD, "");
		//停止重连
//		NuNetManager.stopAllRequest();
//		NuNetManager.instance().disconnectWebSocket();
		AppGroupManager.getInstance().setVersionId(null);
		AppContactManager.getInstance().setVersionId(null);
		UiUtils.toastInCenter(reason);
    	for (Activity activity : ZYApplication.activities) 
    	{
			activity.finish();
		}
    	Intent intent = new Intent(ZYApplication.getContext(), LoginActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP  | Intent.FLAG_ACTIVITY_NEW_TASK);
    	ZYApplication.getContext().startActivity(intent);
	}*/
    public static boolean checkNetWorkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取NetworkInfo对象
        NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo net : networkInfo) {
            if (net.isConnected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测某个应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 启动应用
     * @param context 上下文
     * @param packageName 应用包名
     */
    public static void startApp(Context context, String packageName) {
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            context.startActivity(launchIntent);
        }
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @param packageName
     * @return 应用版本号 ,未安装时返回null
     */
    public static String getPackageVersion(Context context, String packageName) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs.append("0").append(stmp);
            else {
                hs = hs.append(stmp);
            }
            hs.append(":");
        }
        return hs.substring(0, hs.length() - 1);
    }



    public static void initIsDoubleTelephone(Context context) {
        boolean isDouble = true;
        Method method = null;
        Object result_0 = null;
        Object result_1 = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            //只要在反射getSimStateGemini 这个函数时报了错就是单卡手机（这是我自己的经验，不一定全正确）
            method = TelephonyManager.class.getMethod("getSimStateGemini", new Class[]{int.class});
            //获取SIM卡1
            result_0 = method.invoke(tm, new Object[]{new Integer(0)});
            //获取SIM卡1
            result_1 = method.invoke(tm, new Object[]{new Integer(1)});
        } catch (SecurityException e) {
            isDouble = false;
            e.printStackTrace();
            //System.out.println("1_ISSINGLETELEPHONE:"+e.toString());
        } catch (NoSuchMethodException e) {
            isDouble = false;
            e.printStackTrace();
            //System.out.println("2_ISSINGLETELEPHONE:"+e.toString());
        } catch (IllegalArgumentException e) {
            isDouble = false;
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            isDouble = false;
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            isDouble = false;
            e.printStackTrace();
        } catch (Exception e) {
            isDouble = false;
            e.printStackTrace();
            //System.out.println("3_ISSINGLETELEPHONE:"+e.toString());
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        if (isDouble) {
            //保存为双卡手机
            editor.putBoolean(ISDOUBLE, true);
            //保存双卡是否可用
            //如下判断哪个卡可用.双卡都可以用
            if (result_0.toString().equals("5") && result_1.toString().equals("5")) {
                if (!sp.getString(SIMCARD, "2").equals("0") && !sp.getString(SIMCARD, "2").equals("1")) {
                    editor.putString(SIMCARD, "0");
                }
                editor.putBoolean(SIMCARD_1, true);
                editor.putBoolean(SIMCARD_2, true);
            } else if (!result_0.toString().equals("5") && result_1.toString().equals("5")) {//卡二可用
                if (!sp.getString(SIMCARD, "2").equals("0") && !sp.getString(SIMCARD, "2").equals("1")) {
                    editor.putString(SIMCARD, "1");
                }
                editor.putBoolean(SIMCARD_1, false);
                editor.putBoolean(SIMCARD_2, true);
            } else if (result_0.toString().equals("5") && !result_1.toString().equals("5")) {//卡一可用
                if (!sp.getString(SIMCARD, "2").equals("0") && !sp.getString(SIMCARD, "2").equals("1")) {
                    editor.putString(SIMCARD, "0");
                }
                editor.putBoolean(SIMCARD_1, true);
                editor.putBoolean(SIMCARD_2, false);
            } else {//两个卡都不可用(飞行模式会出现这种种情况)
                editor.putBoolean(SIMCARD_1, false);
                editor.putBoolean(SIMCARD_2, false);
            }
        } else {
            //保存为单卡手机
            editor.putString(SIMCARD, "0");
            editor.putBoolean(ISDOUBLE, false);
        }
        editor.commit();
    }

    /**
     * 判断是否为亿联T58
     *
     * @return
     */
    public static boolean isT58() {
        if ("rockchip".equals(Build.BRAND) && "T58".endsWith(Build.DEVICE)) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为只能视频话机
     * TODO 后续适配更多机型
     *
     * @return
     */
    public static boolean isSmartVideoPhone() {
        return isT58();
    }

    /**
     * 获取本地IPv4地址
     * FIXME 后续加入复杂的网络判断，如vpn等
     *
     * @return
     */
    public static String getLocalHostIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
                        .hasMoreElements(); ) {
                    InetAddress inetAddress = ipAddr.nextElement();
                    //包含ipv6 则去掉!inetAddress.isLinkLocalAddress()
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }








    /**
     * 获取当前版本号
     *
     * @return
     */
    public static String getCurrentVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "未知版本";
    }

    public static String getCurrentVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return ""+info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "00";
    }

    /**
     * 配置 RecyclerView
     *
     * @param recyclerView
     * @param layoutManager
     */
    public static void configRecycleView(final RecyclerView recyclerView
            , RecyclerView.LayoutManager layoutManager, RecyclerView.ItemDecoration itemDecoration) {
        recyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        if (itemDecoration != null) {
            recyclerView.addItemDecoration(itemDecoration);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public static boolean isInteger(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }



    /**
     * 判断Activity是否Destroy
     *
     * @param mActivity
     * @return
     */
    public static boolean isDestroy(Activity mActivity) {
        if (mActivity == null || mActivity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && mActivity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }

    public static Bitmap getViewBitmap(View view) {
        view.setDrawingCacheEnabled(true);

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0,
                view.getMeasuredWidth(),
                view.getMeasuredHeight());

        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        return bitmap;
    }


//    private static boolean needInvokedHandlerMessageLeak = true;

    /**
     * 接收 handlerThread 的 looper
     */
    public static void flushStackLocalLeaks(Looper looper) {
        Handler handler = new Handler(looper);
        handler.post(() -> {
            //当队列闲置的时候，就给它发送空的 message，以确保不会发生 message 内存泄漏
            Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                @Override
                public boolean queueIdle() {
                    handler.sendMessageDelayed(handler.obtainMessage(), 1000);
//                    return true;
                    return false;
                }
            });
        });
    }

    /**
     * 通过 Thread.getAllStackTraces().keys 方法获取所有的线程。
     * 迭代遍历，判断线程是否为 HandlerThread，如果是，则调用上面的 flushStackLocalLeaks 方法。
     * 采用这种方案要注意的点是，要注意调用时机，确保调用的时候所有的 HandlerThread 都已经启动了，不然会有遗漏的情况。
     */
    public static void handleMessageLeak() {
//        if (needInvokedHandlerMessageLeak) {
            for (Thread thread : Thread.getAllStackTraces().keySet()) {
                if (thread instanceof HandlerThread && thread.isAlive()) {
                    // 添加 IdleHandler
                    flushStackLocalLeaks(((HandlerThread) thread).getLooper());
                }
            }
//        }
//        needInvokedHandlerMessageLeak = false;
    }

    /**
     * 生成长度len的随机串
     * @param len
     * @return
     */
    public static String getRandom(int len)
    {
        int rs = (int) ((Math.random() * 9 + 1) * Math.pow(10, len - 1));
        return String.valueOf(rs);
    }

    public static int sunday(String haystack, String needle) {
        int hayLen = haystack.length();
        int nLen = needle.length();

        int i = 0;//haystack串的游标索引
        int j = 0;// needle串的游标索引

        // haystack剩余字符少于needle串时跳过比较
        while (i <= hayLen - nLen) {
            // 将needle串与haystack串中参与比较的子串进行逐个字符比对
            while (j < nLen && haystack.charAt(i + j) == needle.charAt(j)) {
                j++;
            }

            // 如果j等于needle串的长度说明此时匹配成功，可以直接返回此时主串的游标索引
            if (j == nLen) {
                return i;
            }

            // 不匹配时计算需要跳过的字符数，移动主串游标i
            if (i < hayLen - nLen) {
                // 对照字符在needle串存在，则需要跳过的字符数为从对照字符在needle串中最后出现的位置起剩余的字符个数
                // 不存在则跳过的字符数为needle串长度+1，也就是代码nLen-(-1)的情况
                i += (nLen - lastIndex(needle, haystack.charAt(i + nLen)));
            } else {
                return -1;
            }
            // 每次比较之后将needle游标置为0
            j=0;
        }

        return -1;
    }

    private static int lastIndex(String str, char ch) {
        // 从后往前检索
        for (int j = str.length() - 1; j >= 0; j--) {
            if (str.charAt(j) == ch) {
                return j;
            }
        }
        return -1;
    }

    public static void getRight(String pat, int[] right) {
        for (int i = 0; i < 256; i++){
            right[i] = -1;
        }
        for (int i = 0; i < pat.length(); i++) {
            right[pat.charAt(i)] = i;
        }
    }

    public static int boyerMooreSearch(String txt, String pat, int[] right)
    {
        int M = txt.length();
        int N = pat.length();
        int skip;
        for (int i = 0; i <= M - N; i += skip) {
            skip = 0;
            for (int j = N - 1; j >= 0; j--) {
                if (pat.charAt(j) != txt.charAt(i + j)) {
                    skip = j - right[txt.charAt(i + j)];
                    if (skip < 1){
                        skip = 1;
                    }
                    break;
                }
            }
            if (skip == 0)
                return i;
        }
        return -1;
    }

    public static int kMPSearch(String txt, String pat, int[] next) {
        int M = txt.length();
        int N = pat.length();
        int i = 0;
        int j = 0;
        while (i < M && j < N) {
            if (j == -1 || txt.charAt(i) == pat.charAt(j)) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }
        if (j == N)
            return i - j;
        else
            return -1;
    }

    public static void getNext(String pat, int[] next) {
        int N = pat.length();
        next[0] = -1;
        int k = -1;
        int j = 0;
        while (j < N - 1) {
            if (k == -1 || pat.charAt(j) == pat.charAt(k)) {
                ++k;
                ++j;
                next[j] = k;
            } else
                k = next[k];
        }
    }

    private static final int MIN_DELAY_TIME= 1500; // 两次点击间隔不能少于1500ms

    private static long lastClickTime;

    public static boolean isFastClick() {

        boolean flag = true;

        long currentClickTime = System.currentTimeMillis();

        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {

            flag = false;

        }

        lastClickTime = currentClickTime;

        return flag;

    }
}
