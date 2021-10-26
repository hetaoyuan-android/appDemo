package com.example.test.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import com.example.test.AppApplication;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cunguoyao on 2017/7/27.
 */

public class UnCeHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    public static final String TAG = "CatchExcep";
    AppApplication application;

    public UnCeHandler(AppApplication application){
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.application = application;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        procesError(ex);
        if(!handleException(ex) && mDefaultHandler != null){
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }else{
//            try{
//                application.logout(false, true);
//                Thread.sleep(100);
//            }catch (InterruptedException e){
//                LogUtils.e(TAG + "error : ", e);
//            }
            /*Intent intent = new Intent(application.getApplicationContext(), SplashActivity.class);
            PendingIntent restartIntent = PendingIntent.getActivity(
                    application.getApplicationContext(), 0, intent,
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            //退出程序
            AlarmManager mgr = (AlarmManager)application.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 400,
                    restartIntent); // 半秒钟后重启应用*/
//            ActivityStack.getInstance().clear();
//            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(AppApplication.getApp(), "很抱歉,程序出现异常,即将退出.",
                        Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        return true;
    }

    private void procesError(Throwable ex) {
        final File file = saveReport(ex);
//        new Thread(new Runnable(){
//            @Override
//            public void run() {
//                uploadReport(file);
//            }
//        }).start();
    }

    public static String FormatStackTrace(Throwable throwable) {
        if(throwable==null) return "";
        String rtn = throwable.getStackTrace().toString();
        try {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            throwable.printStackTrace(printWriter);
            printWriter.flush();
            writer.flush();
            rtn = writer.toString();
            printWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
        }
        return rtn;
    }

    /*
        生成错误日志
     */
    private File saveReport(Throwable ex) {
        FileWriter writer = null;
        PrintWriter printWriter = null;



        try{
            ClipboardManager clipboardManager = (ClipboardManager) AppApplication.getApp().getSystemService(Context.CLIPBOARD_SERVICE);
            //创建ClipData对象
            ClipData clipData = ClipData.newPlainText("ex", FormatStackTrace(ex));
//            CountData.errorMsgCount( FormatStackTrace(ex));
            //添加ClipData对象到剪切板中
            clipboardManager.setPrimaryClip(clipData);

        }catch (Exception excep)
        {
            excep.printStackTrace();
        }
        try {


            File workspace = AppApplication.getApp().getWorkspace();
            File file = new File(workspace, "an_app_log_" + System.currentTimeMillis() + ".log");
            writer = new FileWriter(file);
            printWriter = new PrintWriter(writer);
            writer.append("========Build==========\n");
            writer.append(String.format("BOARD\t%s\n", Build.BOARD));
            writer.append(String.format("BOOTLOADER\t%s\n", Build.BOOTLOADER));
            writer.append(String.format("BRAND\t%s\n", Build.BRAND));
            writer.append(String.format("CPU_ABI\t%s\n", Build.CPU_ABI));
            writer.append(String.format("CPU_ABI2\t%s\n", Build.CPU_ABI2));
            writer.append(String.format("DEVICE\t%s\n", Build.DEVICE));
            writer.append(String.format("DISPLAY\t%s\n", Build.DISPLAY));
            writer.append(String.format("FINGERPRINT\t%s\n", Build.FINGERPRINT));
            writer.append(String.format("HARDWARE\t%s\n", Build.HARDWARE));
            writer.append(String.format("HOST\t%s\n", Build.HOST));
            writer.append(String.format("ID\t%s\n", Build.ID));
            writer.append(String.format("MANUFACTURER\t%s\n", Build.MANUFACTURER));
            writer.append(String.format("MODEL\t%s\n", Build.MODEL));
            writer.append(String.format("SERIAL\t%s\n", Build.SERIAL));
            writer.append(String.format("PRODUCT\t%s\n", Build.PRODUCT));

            writer.append("========APP==========\n");

//            PackageInfo packageInfo = Utils.getPackageInfoByName(application.getApplication(), "com.linkage.mobile72.gsnewest");
//            int versionCode = packageInfo.versionCode;
//            String versionName = packageInfo.versionName;
            writer.append(String.format("versionCode\t%s\n", 1));
            writer.append(String.format("versionName\t%s\n", 2));

            writer.append("========Exception==========\n");
            ex.printStackTrace(printWriter);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    public static String getStackTraceInfo(Exception e) {

        StringWriter sw = null;
        PrintWriter pw = null;

        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);//将出错的栈信息输出到printWriter中
            pw.flush();
            sw.flush();

            return sw.toString();
        } catch (Exception ex) {

            return "发生错误";
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }

    }

    /*
       上传错误日志
     */
    private void uploadReport(File report) {
        Map<String, File> params = new HashMap<>();
        params.put("fileupload", report);
        try {
            upLoadFilePost("http://115.29.165.46:8817/gateway/api/k12/crashLogUploadFile", params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*OutputStream os = null;
        FileInputStream fis = null;
        try {
            URL url = new URL("http://115.29.165.46:8817/gateway/login/crashLogUploadFile");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            os = urlConnection.getOutputStream();
            fis = new FileInputStream(report);
            byte[] buf = new byte[1024 * 8];
            int len = 0;
            while ((len = fis.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            int responseCode = urlConnection.getResponseCode();
            Log.e("uploadReport", "" + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(os);
            close(fis);
        }*/
        /*List<ParamItem> params = new ArrayList<ParamItem>();
        params.add(new ParamItem("commandtype", "crashLogUploadFile", ParamItem.TYPE_TEXT));
        params.add(new ParamItem("fileupload", report, ParamItem.TYPE_FILE));
        WDJsonObjectMultipartRequest mRequest = new WDJsonObjectMultipartRequest(
                "http://115.29.165.46:8817/gateway/login/crashLogUploadFile", Request.Method.POST, params,
                true, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("--response--", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                Log.e("--response-err-", arg0.getMessage());
            }
        });
        TApplication.getInstance().addToRequestQueue(mRequest, TAG);*/
    }

    /**
     * 文件上传
     * @param actionUrl:上传接口地址
     * @param files:需上传的文件集合
     * @return
     * @throws IOException
     */
    public static String upLoadFilePost(String actionUrl, Map<String, File> files) throws IOException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 1000);
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false);
        conn.setRequestMethod("POST"); // Post方式
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                + ";boundary=" + BOUNDARY);

        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        // 发送文件数据
        if (files != null)
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                        + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());
                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }

                is.close();
                outStream.write(LINEND.getBytes());
            }

        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();

        // 得到响应码
        int res = conn.getResponseCode();
        if (res == 200) {
            InputStream in = conn.getInputStream();
            InputStreamReader isReader = new InputStreamReader(in);
            BufferedReader bufReader = new BufferedReader(isReader);
            String line = "";
            String data = "";
            while ((line = bufReader.readLine()) != null) {
                data += line;
            }
            outStream.close();
            conn.disconnect();
            return data;
        }
        outStream.close();
        conn.disconnect();
        return "";
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

