package com.example.test.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.test.BuildConfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * 文件工具类
 *
 * @author CCL
 */

public class FileUtils {


    /**
     * 声明各种类型文件的dataType
     **/
    private static final String DATA_TYPE_ALL = "*/*";//未指定明确的文件类型，不能使用精确类型的工具打开，需要用户选择
    private static final String DATA_TYPE_APK = "application/vnd.android.package-archive";
    private static final String DATA_TYPE_VIDEO = "video/*";
    private static final String DATA_TYPE_AUDIO = "audio/*";
    private static final String DATA_TYPE_HTML = "text/html";
    private static final String DATA_TYPE_IMAGE = "image/*";
    private static final String DATA_TYPE_PPT = "application/vnd.ms-powerpoint";
    private static final String DATA_TYPE_EXCEL = "application/vnd.ms-excel";
    private static final String DATA_TYPE_WORD = "application/msword";
    private static final String DATA_TYPE_CHM = "application/x-chm";
    private static final String DATA_TYPE_TXT = "text/plain";
    private static final String DATA_TYPE_PDF = "application/pdf";


    private static char separatorChar = System.getProperty("file.separator", "/").charAt(0);
    private static String separator = String.valueOf(separatorChar);
    public static final String[][] MIME_MapTable = {
            // {后缀名， MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"}, {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"},
            {".sh", "text/plain"}, {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"}, {".txt", "text/plain"},
            {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"}, {"", "*/*"}};

    /**
     * 打开文件
     */
    public static void openFile(File file, Context context) {
        String type = getMIMEType(file);
        Intent intent = new Intent();
//		if (type.equals("application/pdf")) {
//			Uri uri = Uri.parse(file.getAbsolutePath());
//			intent.setClass(context, MuPDFActivity.class);
//			intent.setAction(Intent.ACTION_VIEW);
//			intent.setData(uri);
//		} else {
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			// 设置intent的Action属性
//			intent.setAction(Intent.ACTION_VIEW);
//			// 获取文件file的MIME类型
//			// 设置intent的data和Type属性。
//			intent.setDataAndType(Uri.fromFile(file), type);
//		}
        /**
         * 获取手机厂商
         */
        String leixing = Build.MANUFACTURER;
        /**
         * 修改者：史丰
         *
         * 进入PDF阅读器选择界面
         * */
        if (type.equals("application/pdf")) {
//			if (leixing.equals("HTC")||leixing.equals("samsung")) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            // 获取文件file的MIME类型
            // 设置intent的data和Type属性。
            //修复Android7.0异常
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setDataAndType(FileProvider.getUriForFile(context,
                        BuildConfig.APPLICATION_ID + ".provider",
                        file), type);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                intent.setDataAndType(Uri.fromFile(file), type);
            }
//			} else {
//				Uri uri = Uri.parse(file.getAbsolutePath());
//				intent.setClass(context, MuPDFActivity.class);
//				intent.setAction(Intent.ACTION_VIEW);
//				intent.setData(uri);
//			}
        }
        // 跳转
        context.startActivity(intent);
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     */
    private static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length());
        //modify by zxw 20191119
        /*if (end == "") {
            return type;
		}*/
        if (TextUtils.isEmpty(end)) {
            return type;
        }
        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equalsIgnoreCase(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    public final static Map<String, Integer> FILE_TYPE_MAP = new HashMap<>();
    public final static int TYPE_PIC = 1;
    public final static int TYPE_TXT = 2;
    public final static int TYPE_DOC = 3;
    public final static int TYPE_XLS = 4;
    public final static int TYPE_PDF = 5;
    public final static int TYPE_ZIP = 6;
    public final static int TYPE_AUDIO = 7;
    public final static int TYPE_UNKNOW = 8;

    static {
        FILE_TYPE_MAP.put("bmp", TYPE_PIC);
        FILE_TYPE_MAP.put("jpg", TYPE_PIC);
        FILE_TYPE_MAP.put("jpeg", TYPE_PIC);
        FILE_TYPE_MAP.put("png", TYPE_PIC);
        FILE_TYPE_MAP.put("tiff", TYPE_PIC);
        FILE_TYPE_MAP.put("gif", TYPE_PIC);
        FILE_TYPE_MAP.put("pcx", TYPE_PIC);
        FILE_TYPE_MAP.put("tga", TYPE_PIC);
        FILE_TYPE_MAP.put("exif", TYPE_PIC);
        FILE_TYPE_MAP.put("fpx", TYPE_PIC);
        FILE_TYPE_MAP.put("svg", TYPE_PIC);
        FILE_TYPE_MAP.put("psd", TYPE_PIC);
        FILE_TYPE_MAP.put("cdr", TYPE_PIC);
        FILE_TYPE_MAP.put("pcd", TYPE_PIC);
        FILE_TYPE_MAP.put("dxf", TYPE_PIC);
        FILE_TYPE_MAP.put("ufo", TYPE_PIC);
        FILE_TYPE_MAP.put("eps", TYPE_PIC);
        FILE_TYPE_MAP.put("ai", TYPE_PIC);
        FILE_TYPE_MAP.put("raw", TYPE_PIC);
        FILE_TYPE_MAP.put("wmf", TYPE_PIC);

        FILE_TYPE_MAP.put("txt", TYPE_TXT);
        FILE_TYPE_MAP.put("doc", TYPE_DOC);
        FILE_TYPE_MAP.put("docx", TYPE_DOC);
        FILE_TYPE_MAP.put("xls", TYPE_XLS);
        FILE_TYPE_MAP.put("htm", TYPE_UNKNOW);
        FILE_TYPE_MAP.put("html", TYPE_UNKNOW);
        FILE_TYPE_MAP.put("jsp", TYPE_TXT);
        FILE_TYPE_MAP.put("rtf", TYPE_TXT);
        FILE_TYPE_MAP.put("wpd", TYPE_UNKNOW);
        FILE_TYPE_MAP.put("pdf", TYPE_PDF);
        FILE_TYPE_MAP.put("ppt", TYPE_PDF);
        FILE_TYPE_MAP.put("zip", TYPE_ZIP);
        FILE_TYPE_MAP.put("z", TYPE_ZIP);
        FILE_TYPE_MAP.put("rar", TYPE_ZIP);

        FILE_TYPE_MAP.put("mp4", TYPE_AUDIO);
        FILE_TYPE_MAP.put("avi", TYPE_AUDIO);
        FILE_TYPE_MAP.put("mov", TYPE_AUDIO);
        FILE_TYPE_MAP.put("wmv", TYPE_AUDIO);
        FILE_TYPE_MAP.put("asf", TYPE_AUDIO);
        FILE_TYPE_MAP.put("navi", TYPE_AUDIO);
        FILE_TYPE_MAP.put("3gp", TYPE_AUDIO);
        FILE_TYPE_MAP.put("mkv", TYPE_AUDIO);
        FILE_TYPE_MAP.put("f4v", TYPE_AUDIO);
        FILE_TYPE_MAP.put("rmvb", TYPE_AUDIO);
        FILE_TYPE_MAP.put("webm", TYPE_AUDIO);

        FILE_TYPE_MAP.put("mp3", TYPE_AUDIO);
        FILE_TYPE_MAP.put("wma", TYPE_AUDIO);
        FILE_TYPE_MAP.put("wav", TYPE_AUDIO);
        FILE_TYPE_MAP.put("mod", TYPE_AUDIO);
        FILE_TYPE_MAP.put("ra", TYPE_AUDIO);
        FILE_TYPE_MAP.put("cd", TYPE_AUDIO);
        FILE_TYPE_MAP.put("md", TYPE_AUDIO);
        FILE_TYPE_MAP.put("asf", TYPE_AUDIO);
        FILE_TYPE_MAP.put("aac", TYPE_AUDIO);
        FILE_TYPE_MAP.put("vqf", TYPE_AUDIO);
        FILE_TYPE_MAP.put("ape", TYPE_AUDIO);
        FILE_TYPE_MAP.put("mid", TYPE_AUDIO);
        FILE_TYPE_MAP.put("ogg", TYPE_AUDIO);
        FILE_TYPE_MAP.put("m4a", TYPE_AUDIO);
        FILE_TYPE_MAP.put("vqf", TYPE_AUDIO);
    }

    public static Integer getType(String url) {
        String fileType = url.substring(url.lastIndexOf(".") + 1, url.length());
        Integer integer = FILE_TYPE_MAP.get(fileType.toLowerCase());
        if (integer == null) {
            return TYPE_UNKNOW;
        }
        return integer;
    }


    /**
     * 格式化文件大小
     */
    public static String FormatFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#0.00");
        String s = "";
        if (fileSize < 1024) {
            s = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            s = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            s = df.format((double) fileSize / 1048576) + "M";
        } else {
            s = df.format((double) fileSize / 1073741824) + "G";
        }
        return s;
    }

    public static void delFolder(String folderPath) {
        File file = new File(folderPath);
        if (!file.exists()) {
            return;
        }
        delAllFile(folderPath); // 删除完里面所有内容
        file.delete(); // 删除空文件夹
    }

    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        //modify by zxw null pointer 20151119
        if (tempList != null && tempList.length > 0) {
            for (int i = 0; i < tempList.length; i++) {
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList[i]);
                } else {
                    temp = new File(path + File.separator + tempList[i]);
                }
                if (temp.isFile()) {
                    temp.delete();
                }
                if (temp.isDirectory()) {
                    // delAllFile(path + File.separator + tempList[i]);//
                    // 先删除文件夹里面的文件
                    delFolder(path + File.separator + tempList[i]);// 再删除空文件夹
                }
            }
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移动文件到指定目录
     *
     * @param oldPath
     * @param newPath
     */
    public static void moveFile(String oldPath, String newPath) {
        copyFile(oldPath, newPath);
        File oldFile = new File(oldPath);
        if (oldFile.exists()) {
            oldFile.delete();
        }
    }

    /**
     * 将图片保存到本地
     *
     * @param context
     * @param imageView
     */
    public static void SaveImageToSysAlbum(Context context, ImageView imageView) {

        BitmapDrawable bmpDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = null;
        if (bmpDrawable != null) {
            bitmap = bmpDrawable.getBitmap();
        }
        if (bitmap == null) {
            return;
        }
        MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "HaiGouShareCode", "");
        //如果是4.4及以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String fileName = UUID.randomUUID().toString();
            File mPhotoFile = new File(fileName);
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(mPhotoFile);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);

        } else {
            context.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://"
                            + Environment.getExternalStorageDirectory())));
        }
        Toast.makeText(context, "已保存到相册", Toast.LENGTH_SHORT).show();

    }


    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    public static String getFileName(String pathName) {
        int separatorIndex = pathName.lastIndexOf(separator);
        return (separatorIndex < 0) ? pathName : pathName.substring(separatorIndex + 1, pathName.length());
    }


    /**
     * 打开文件
     *
     * @param filePath 文件的全路径，包括到文件名
     */
    public static void openFile(Context mContext, String filePath,String downLoadUrl) {
        File file = new File(filePath);

        if (!file.exists()) {
            //如果文件不存在
            Toast.makeText(mContext, "打开失败，原因：文件已经被移动或者删除", Toast.LENGTH_SHORT).show();
            return;
        }
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase(Locale.getDefault());
        /* 依扩展名的类型决定MimeType */


        Intent intent = null;
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            intent = generateVideoAudioIntent(mContext, filePath, DATA_TYPE_AUDIO);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            intent = generateCommonIntent(mContext, filePath, DATA_TYPE_IMAGE);
        } else if (end.equals("apk")) {
            intent = generateCommonIntent(mContext, filePath, DATA_TYPE_APK);
        } else if (end.equals("html") || end.equals("htm")) {
            intent = generateHtmlFileIntent(filePath);
        } else if (end.equals("ppt")) {
            intent = generateCommonIntent(mContext, filePath, DATA_TYPE_PPT);
        } else if (end.equals("xls")) {
            intent = generateCommonIntent(mContext, filePath, DATA_TYPE_EXCEL);
        } else if (end.equals("doc") || end.equals("docx")) {
            intent = generateCommonIntent(mContext, filePath, DATA_TYPE_WORD);
        } else if (end.equals("pdf")) {
            intent = generateCommonIntent(mContext, filePath, DATA_TYPE_PDF);
        } else if (end.equals("chm")) {
            intent = generateCommonIntent(mContext, filePath, DATA_TYPE_CHM);
        } else if (end.equals("txt")) {
            intent = generateCommonIntent(mContext, filePath, DATA_TYPE_TXT);
        } else if (end.equals("swf")) {
            return;
        } else if (end.equals("3gp") || end.equals("mp4")){
            intent = generateVideoAudioIntent(mContext, filePath, DATA_TYPE_VIDEO);
        } else {
            intent = generateCommonIntent(mContext, filePath, DATA_TYPE_ALL);
        }
        mContext.startActivity(intent);


//        if (end.equals("3gp") || end.equals("mp4")) {
//
//
////            Intent intent1 = new Intent(mContext, DiscoveryVideoPlayerActivity.class);
////            intent1.putExtra(DiscoveryVideoPlayerActivity.URL_KEY,downLoadUrl);
////            mContext.startActivity(intent1);
//            intent = generateVideoAudioIntent(mContext, filePath, DATA_TYPE_VIDEO);
//            mContext.startActivity(intent);
//        }
    }

    /**
     * 产生打开视频或音频的Intent
     *
     * @param filePath 文件路径
     * @param dataType 文件类型
     * @return
     */
    private static Intent generateVideoAudioIntent(Context mContext, String filePath, String dataType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        File file = new File(filePath);
        intent.setDataAndType(getUri(mContext, intent, file), dataType);
        return intent;
    }

    /**
     * 产生除了视频、音频、网页文件外，打开其他类型文件的Intent
     *
     * @param filePath 文件路径
     * @param dataType 文件类型
     * @return
     */
    private static Intent generateCommonIntent(Context mContext, String filePath, String dataType) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(filePath);
        Uri uri = getUri(mContext, intent, file);
        intent.setDataAndType(uri, dataType);
        return intent;
    }

    /**
     * 产生打开网页文件的Intent
     *
     * @param filePath 文件路径
     * @return
     */
    private static Intent generateHtmlFileIntent(String filePath) {
        Uri uri = Uri.parse(filePath)
                .buildUpon()
                .encodedAuthority("com.android.htmlfileprovider")
                .scheme("content")
                .encodedPath(filePath)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, DATA_TYPE_HTML);
        return intent;
    }

    /**
     * 获取对应文件的Uri
     *
     * @param intent 相应的Intent
     * @param file   文件对象
     * @return
     */
    private static Uri getUri(Context mContext, Intent intent, File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //判断版本是否在7.0以上
            uri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    //文件写入
    public static void writeFileData(String filePath, String fileName, String content){
        FileOutputStream fos = null;
        OutputStreamWriter osw;
        BufferedWriter bw = null;
        try {
            File file = new File(filePath, fileName);
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);
            bw.write(content);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.flush();
                bw.close();
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    //读取方法
    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static File uri2File(Uri uri, Context context) {
        File file = null;
        if (uri == null) {
            return null;
        }
        if (uri.getScheme().equals(ContentResolver.SCHEME_FILE)) {
            file = new File(uri.getPath());
        } else if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver contentResolver = context.getContentResolver();
            String displayName = UUID.randomUUID() + "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri));
            try {
                InputStream is = contentResolver.openInputStream(uri);
                File cache = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath(), displayName);
                FileOutputStream fos = new FileOutputStream(cache);
                FileUtils.copy(is, fos);
                file = cache;
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) {
        try {
            int byteread;
            byte[] buffer = new byte[1444];
            while ((byteread = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, byteread);
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}