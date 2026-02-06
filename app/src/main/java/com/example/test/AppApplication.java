package com.example.test;


import android.app.Application;
import android.os.Environment;

import com.example.test.skin.SkinManager;
import com.example.test.tabscroll.db.SQLHelper;
import com.example.test.utils.UnCeHandler;

import java.io.File;

public class AppApplication extends Application {
	private static AppApplication mAppApplication;
	private SQLHelper sqlHelper;
	public static final String PATH_APP = "studywithme/";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mAppApplication = this;
		//设置该CrashHandler为程序的默认处理器
		UnCeHandler catchExcep = new UnCeHandler(this);
		Thread.setDefaultUncaughtExceptionHandler(catchExcep);
		//皮肤切换初始化
		SkinManager.getInstance().init(this);
	}

	/** 获取Application */
	public static AppApplication getApp() {
		return mAppApplication;
	}

	/** 获取数据库Helper */
	public SQLHelper getSQLHelper() {
		if (sqlHelper == null)
			sqlHelper = new SQLHelper(mAppApplication);
		return sqlHelper;
	}

	/** 摧毁应用进程时候调用 */
	public void onTerminate() {
		if (sqlHelper != null)
			sqlHelper.close();
		super.onTerminate();
	}

	public void clearAppCache() {
	}

	public File getWorkspace() {

		File file = Environment.getExternalStorageDirectory();
		File workspace = new File(file, PATH_APP);
		if (!workspace.exists()) {
			workspace.mkdirs();
		}
		return workspace;
	}
}
