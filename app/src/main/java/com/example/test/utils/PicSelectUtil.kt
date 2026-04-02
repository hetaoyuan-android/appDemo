package com.example.test.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 * @author yanteng
 * @date 2026-03-19 15:10
 * @description 选择相册、选择拍照。选完后直接裁剪，裁剪完回调图片地址
 */
object PicSelectUtil {

    private var callbackFilePath : ((path : String) -> Unit)? = null

    private var path: String = ""
    private var imageUri: Uri? = null
    private var uriTempFile: Uri? = null

    //临时文件，用于储存裁剪后的图片，使用完即删除掉
    private lateinit var cropPath: String
    private val TAG = "PicSelectUtil"

    fun setCallbackFilePath(callbackPath : (path : String) -> Unit){
        this.callbackFilePath = callbackPath
    }

    fun gotoPhoto(activity: Activity) {
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(intent, PictureEditType.RES_USE_ALBUM)
    }


    fun gotoCamera(activity: Activity) {
        PermissionsUtil.requestCameraPermission(object : PermissionListener {
            override fun permissionGranted(permission: Array<String>) {
                try {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    
                    // 使用应用私有存储目录
                    val cameraFile = createFile(activity, "camera")
                    path = cameraFile.path
                    
                    // 使用FileProvider生成安全的URI
                    imageUri = FileProvider.getUriForFile(
                        activity,
                        "${activity.packageName}.fileprovider",
                        cameraFile
                    )
                    
                    intent.putExtra(
                        MediaStore.EXTRA_OUTPUT,
                        imageUri
                    )
                    
                    // 添加必要的权限
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    
                    activity.startActivityForResult(intent, PictureEditType.RES_USE_CAMERA)
                } catch (exception: Exception) {
                    Log.e(TAG, "CAPTURE exception", exception)
                }
            }

            override fun permissionDenied(permission: Array<String>) {
            }
        })
    }

    @Throws(IOException::class)
    private fun createFile(context: Context, folderName: String): File {
        // 使用应用的私有存储目录
        val appDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            ?: context.filesDir
        
        val cropDir = File(appDir, folderName)
        if (!cropDir.exists()) {
            cropDir.mkdirs()
        }
        
        val file = File(cropDir, "${System.currentTimeMillis()}.jpg")
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }


    /**
     * 拍照和相册返回的图片直接去裁剪
     */
    fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult: requestCode = $requestCode, resultCode = $resultCode")
        
        //拍照
        if (requestCode == PictureEditType.RES_USE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "onActivityResult: Camera success, imageUri = $imageUri")
                if (imageUri != null) {
                    cropPhoto(activity, imageUri)
                } else {
                    Log.e(TAG, "onActivityResult: Camera success but imageUri is null")
                }
            } else {
                Log.d(TAG, "onActivityResult: Camera cancelled")
            }
        } else if (requestCode == PictureEditType.RES_USE_ALBUM) {                //相册选择图片
            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data
                Log.d(TAG, "onActivityResult: Album success, uri = $uri")
                if (uri != null) {
                    imageUri = uri
                    cropPhoto(activity, imageUri)
                } else {
                    Log.e(TAG, "onActivityResult: Album success but uri is null")
                }
            } else {
                Log.d(TAG, "onActivityResult: Album cancelled")
            }
        } else if (requestCode == PictureEditType.RES_USE_CROP) {                //剪裁图片
            if (resultCode == Activity.RESULT_OK && uriTempFile != null) {
                try {
                    // 不需要解码整个图片，直接使用文件路径
                    val filePath = uriTempFile!!.path
                    if (filePath != null) {
                        Log.d(TAG, "onActivityResult: Crop success, filePath = $filePath")
                        callbackFilePath?.invoke(filePath)
                    } else {
                        Log.e(TAG, "onActivityResult: Crop success but filePath is null")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "onActivityResult: Exception while handling crop result", e)
                }
            } else {
                Log.d(TAG, "onActivityResult: Crop cancelled or uriTempFile is null")
            }
        }
    }


    private fun cropPhoto(activity: Activity, uri: Uri?) {
        if (uri == null) {
            Log.e(TAG, "cropPhoto: uri is null")
            return
        }
        
        try {
            Log.d(TAG, "cropPhoto: uri = $uri")
            
            // 先将源图片复制到应用私有目录，避免权限问题
            val sourceFile = copyUriToAppDir(activity, uri)
            val sourceUri = FileProvider.getUriForFile(
                activity,
                "${activity.packageName}.fileprovider",
                sourceFile
            )
            
            Log.d(TAG, "cropPhoto: sourceFile = ${sourceFile.absolutePath}")
            Log.d(TAG, "cropPhoto: sourceUri = $sourceUri")
            
            val intent = Intent("com.android.camera.action.CROP")
            intent.setDataAndType(sourceUri, "image/*")
            
            // 添加必要的权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            
            intent.putExtra("crop", "true")
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputX", 720)
            intent.putExtra("outputY", 720)
            // 设置了true的话直接返回bitmap，可能会很占内存
            intent.putExtra("return-data", false)

            val cropFile = createFile(activity, "crop")
            
            // 使用FileProvider生成安全的URI
            uriTempFile = FileProvider.getUriForFile(
                activity,
                "${activity.packageName}.fileprovider",
                cropFile
            )
            
            Log.d(TAG, "cropPhoto: cropFile = ${cropFile.absolutePath}")
            Log.d(TAG, "cropPhoto: uriTempFile = $uriTempFile")
            
            // 为裁剪应用授权访问所有相关URI
            activity.grantUriPermission(
                "com.android.camera",
                sourceUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            
            activity.grantUriPermission(
                "com.android.camera",
                uriTempFile,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTempFile)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
            
            activity.startActivityForResult(intent, PictureEditType.RES_USE_CROP)
        } catch (e: Exception) {
            Log.e(TAG, "cropPhoto: Exception during crop process", e)
        }
    }
    
    /**
     * 将Uri指向的图片复制到应用私有目录
     */
    private fun copyUriToAppDir(context: Context, srcUri: Uri): File {
        Log.d(TAG, "copyUriToAppDir: srcUri = $srcUri")
        val outFile = createFile(context, "crop_src")
        Log.d(TAG, "copyUriToAppDir: outFile = ${outFile.absolutePath}")

        var inputStream: java.io.InputStream? = null
        var outputStream: java.io.OutputStream? = null

        try {
            inputStream = context.contentResolver.openInputStream(srcUri)
            if (inputStream != null) {
                outputStream = FileOutputStream(outFile)
                
                // 使用缓冲流提高性能并减少内存占用
                val buffer = ByteArray(8192)
                var bytesRead: Int
                var totalBytes = 0L
                
                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                    totalBytes += bytesRead
                }
                
                Log.d(TAG, "copyUriToAppDir: totalBytes = $totalBytes")
            } else {
                val errorMsg = "Failed to open input stream for Uri: $srcUri"
                Log.e(TAG, errorMsg)
                throw IOException(errorMsg)
            }
        } catch (e: Exception) {
            Log.e(TAG, "copyUriToAppDir: Exception while copying file", e)
            // 清理临时文件
            if (outFile.exists()) {
                outFile.delete()
            }
            throw IOException("Failed to copy file from Uri: $srcUri", e)
        } finally {
            // 确保流被关闭
            try {
                outputStream?.close()
            } catch (e: IOException) {
                Log.e(TAG, "copyUriToAppDir: Error closing output stream", e)
            }
            try {
                inputStream?.close()
            } catch (e: IOException) {
                Log.e(TAG, "copyUriToAppDir: Error closing input stream", e)
            }
        }
        
        return outFile
    }
}