package com.example.test.imagetext;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class H5ImageActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 2000;
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000013;

    private WebView mWebView;
    private Uri mCameraUri;
    private String mCameraImagePath;
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebView = new WebView(this);
        setContentView(mWebView);
        initWebView();
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        // 允许混合内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());

        // 注入 JS Bridge，暴露给 H5 调用
        mWebView.addJavascriptInterface(new AndroidBridge(), "Android");

        // 加载 assets 中的 H5 页面
        mWebView.loadUrl("file:///android_asset/h5_watermark_camera.html");
    }

    /**
     * JS Bridge：供 H5 页面调用的原生方法
     */
    private class AndroidBridge {

        @JavascriptInterface
        public void capturePhoto() {
            // JS 调用来自 WebView 子线程，切回主线程
            runOnUiThread(() -> checkPermissionAndCamera());
        }
    }

    /**
     * 检查相机权限
     */
    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                callJsError("相机权限被拒绝");
            }
        }
    }

    /**
     * 调起原生相机
     */
    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;

            if (isAndroidQ) {
                photoUri = createImageUri();
            } else {
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    callJsError("创建图片文件失败");
                    return;
                }

                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        photoUri = FileProvider.getUriForFile(this,
                                getPackageName() + ".fileprovider", photoFile);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
            }

            mCameraUri = photoUri;
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
            }
        } else {
            callJsError("未找到相机应用");
        }
    }

    // ===================== Android 版本适配 =====================

    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    private File createImageFile() throws IOException {
        String imageName = "h5_" + new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date()) + ".jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }

    // ===================== 相机回调 + 水印处理 =====================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != CAMERA_REQUEST_CODE) return;

        if (resultCode == RESULT_OK) {
            processPhotoAndAddWatermark();
        } else {
            callJsError("拍照已取消");
        }
    }

    /**
     * 加载照片 → 加水印 → 转 base64 → 回调 H5
     */
    private void processPhotoAndAddWatermark() {
        try {
            // 1. 加载照片 Bitmap
            Bitmap bitmap;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2; // 压缩采样，避免 OOM
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            if (isAndroidQ) {
                bitmap = MediaStore.Images.Media.getBitmap(
                        getContentResolver(), mCameraUri);
            } else {
                bitmap = BitmapFactory.decodeFile(mCameraImagePath, options);
            }

            if (bitmap == null) {
                callJsError("无法读取照片");
                return;
            }

            // 2. 加水印到右下角
            Bitmap watermarkedBitmap = ImageUtil.drawTextToRightBottom(
                    this, bitmap, getCurrentDateTime(), 30, Color.RED, 20, 30);

            // 释放原图内存
            if (watermarkedBitmap != bitmap) {
                bitmap.recycle();
            }

            // 3. 转 base64
            String base64 = bitmapToBase64(watermarkedBitmap);
            watermarkedBitmap.recycle();

            // 4. 回调 H5 页面展示
            callJs("window.onPhotoCaptured('" + base64 + "')");

        } catch (Exception e) {
            e.printStackTrace();
            callJsError("处理照片失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前日期时间字符串，格式：2026/6/8 15:41
     */
    private String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy/M/d HH:mm", Locale.getDefault()).format(new Date());
    }

    /**
     * Bitmap 转 base64 字符串（JPEG 压缩）
     */
    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    // ===================== JS 回调工具方法 =====================

    private void callJs(final String jsCode) {
        runOnUiThread(() -> mWebView.evaluateJavascript(jsCode, null));
    }

    private void callJsError(final String errorMsg) {
        runOnUiThread(() -> {
            String escaped = errorMsg.replace("'", "\\'").replace("\n", " ");
            mWebView.evaluateJavascript("window.onPhotoError('" + escaped + "')", null);
            Toast.makeText(H5ImageActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
