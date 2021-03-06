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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.test.BuildConfig;
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
            Bitmap bitmap1 = ImageUtil.drawTextToRightBottom(ImageAddTextActivity.this, bitmap, "???????????????", 30, R.color.colorPrimary, 20, 30);
            ivPhoto.setImageBitmap(bitmap1);
        });
        photo.setOnClickListener(v -> {
            openGallery();
        });

    }

    // ?????????????????????requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;

    /**
     * ????????????????????????
     * ?????????????????????????????????
     */
    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            //????????????????????????
            openCamera();
        } else {
            //??????????????????????????????
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CAMERA_REQUEST_CODE);
        }
    }

    /**
     * ??????????????????????????????
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //???????????????????????????????????????
                openCamera();
            } else {
                //?????????????????????????????????
                Toast.makeText(this,"?????????????????????",Toast.LENGTH_LONG).show();
            }
        }
    }

    //???????????????????????????uri
    private Uri mCameraUri;

    // ????????????????????????????????????Android 10????????????????????????????????????
    private String mCameraImagePath;

    // ?????????Android 10????????????
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    /**
     * ??????????????????
     */
    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // ?????????????????????
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;

            if (isAndroidQ) {
                // ??????android 10
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
                        //??????Android 7.0?????????????????????FileProvider????????????content?????????Uri
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
     * ??????????????????uri,?????????????????????????????? Android 10????????????????????????
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // ???????????????SD???,????????????SD?????????,?????????SD????????????????????????
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * ???????????????????????????
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
                    // Android 10 ????????????uri??????
//                    ivPhoto.setImageURI(mCameraUri);
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(ImageAddTextActivity.this.getContentResolver(), mCameraUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // ????????????????????????
//                    ivPhoto.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                    bitmap = BitmapFactory.decodeFile(mCameraImagePath);
                }
                Bitmap bitmap1 = ImageUtil.drawTextToRightBottom(this,bitmap,"???????????????",30, Color.RED,20,30);
                ivPhoto.setImageBitmap(bitmap1);
            } else {
                Toast.makeText(this,"??????",Toast.LENGTH_LONG).show();
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

        //??????API29
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
        // ????????????crop=true?????????????????????Intent??????????????????VIEW?????????
        intent.putExtra("crop", "true");
        // aspectX aspectY ??????????????????
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY ?????????????????????
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);


        startActivityForResult(intent, PIC_OK);
    }



}