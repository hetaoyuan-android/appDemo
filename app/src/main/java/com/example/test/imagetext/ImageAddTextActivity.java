package com.example.test.imagetext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageAddTextActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 1000;
    private static final int PIC_ALBUM = 1005;
    private static final int PIC_OK = 1006;

    private ImageView ivPhoto;
    private Button button,button1, photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_add_text);
        ivPhoto = findViewById(R.id.imageView);
        button = findViewById(R.id.button);
        button1 = findViewById(R.id.button1);
        photo = findViewById(R.id.photo);
        button.setOnClickListener(v -> checkPermissionAndCamera());
        button1.setOnClickListener(v -> {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b);
            Bitmap bitmap1 = ImageUtil.drawTextToRightBottom(ImageAddTextActivity.this, bitmap, "拖鞋王子猪", 30, R.color.colorPrimary, 20, 30);
            ivPhoto.setImageBitmap(bitmap1);
        });
        photo.setOnClickListener(v -> {
            openGallery();
        });

    }

    // 申请相机权限的requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;

    /**
     * 检查权限并拍照。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            //有调起相机拍照。
            openCamera();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CAMERA_REQUEST_CODE);
        }
    }

    /**
     * 处理权限申请的回调。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，有调起相机拍照。
                openCamera();
            } else {
                //拒绝权限，弹出提示框。
                Toast.makeText(this,"拍照权限被拒绝",Toast.LENGTH_LONG).show();
            }
        }
    }

    //用于保存拍照图片的uri
    private Uri mCameraUri;

    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;

    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    /**
     * 调起相机拍照
     */
    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;

            if (isAndroidQ) {
                // 适配android 10
                photoUri = createImageUri();
            } else {
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
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
        }
    }

    /**
     * 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建保存图片的文件
     */
    private File createImageFile() throws IOException {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
                if (isAndroidQ) {
                    // Android 10 使用图片uri加载
//                    ivPhoto.setImageURI(mCameraUri);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(ImageAddTextActivity.this.getContentResolver(), mCameraUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // 使用图片路径加载
//                    ivPhoto.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                    bitmap = BitmapFactory.decodeFile(mCameraImagePath);
                }
                Bitmap bitmap1 = ImageUtil.drawTextToRightBottom(this,bitmap,"拖鞋王子猪",30, Color.RED,20,30);
                ivPhoto.setImageBitmap(bitmap1);
            } else {
                Toast.makeText(this,"取消",Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == PIC_ALBUM) {
            if (data != null) {
                startPhotoZoom(data.getData());
            }
        }

        if (requestCode == PIC_OK) {
            if (data != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setPicToView(data);
                    }
                }, 500);
            }
        }
    }


    protected void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PIC_ALBUM);
    }

    private void setPicToView(Intent picData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(mPhotoUri));
                String name = mPhotoUri.toString();
                if (!TextUtils.isEmpty(name) && name.contains("/")) {
                    String[] split = name.split("/");
                    if (split.length > 0) {
                        name = split[split.length - 1];
                    }
                }
                mPhotoUri.toString();
//                File file = saveBitmapFile(photo, name);
//                viewModel.uploadResource(file);
                ivPhoto.setImageBitmap(photo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Bundle extras = picData.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                Drawable drawable = new BitmapDrawable(getResources(), photo);
                if (null != photo) {
//                    saveBitmapFile(photo);
//                    binding.userInfoHeadIv.setImageDrawable(drawable);
//                    viewModel.uploadResource(file);
                    ivPhoto.setImageBitmap(photo);
                }
            }
        }

    }

    private Uri mPhotoUri;
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");

        //适配API29
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File CropPhoto = FileUtils.uri2File(uri, this);
            mPhotoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", CropPhoto);
            intent.setDataAndType(mPhotoUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        } else {
            intent.setDataAndType(uri, "image/*");
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);


        startActivityForResult(intent, PIC_OK);
    }



}