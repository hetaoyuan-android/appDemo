package com.example.test.flowlayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.io.File;

/**
 * Created by newcboy on 2018/3/9.
 */

public class ZoomImageView extends View {

    public static final int IMAGE_MAX_SIZE = 1000;//加载图片允许的最大size，单位kb
    private float minimal = 100.0f;

    private float screenW;//屏幕宽度
    private float screenH;//屏幕高度

    //单指按下的坐标
    private float mFirstX = 0.0f;
    private float mFirstY = 0.0f;

    //单指离开的坐标
    private float lastMoveX =-1f;
    private float lastMoveY =-1f;

    //两指的中点坐标
    private float centPointX;
    private float centPointY;

    //图片的绘制坐标
    private float translationX = 0.0f;
    private float translationY = 0.0f;

    //图片的原始宽高
    private float primaryW;
    private float primaryH;

    //图片当前宽高
    private float currentW;
    private float currentH;

    private float scale = 1.0f;
    private float maxScale, minScale;
    private Bitmap bitmap;
    private Matrix matrix;

    private int mLocker = 0;
    private float fingerDistance = 0.0f;

    private boolean isLoaded = false;
    private boolean isClickInImage = false;

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 从资源文件中读取图片
     * @param context
     * @param imageId
     */
    public void setResourceBitmap(Context context, int imageId){
        bitmap = BitmapFactory.decodeResource(context.getResources(), imageId);
        isLoaded = true;
        primaryW = bitmap.getWidth();
        primaryH = bitmap.getHeight();
        matrix = new Matrix();
    }

    public void setResourceBitmap(Context context, Bitmap bitmap){
        bitmap = bitmap;
        isLoaded = true;
        primaryW = bitmap.getWidth();
        primaryH = bitmap.getHeight();
        matrix = new Matrix();
    }

    /**
     * 根据路径添加图片
     * @param path
     * @param scale
     */
    public void setImagePathBitmap(String path, float scale){
        this.scale = scale;
        setImageBitmap(path);
    }

    private void setImageBitmap(String path){
        File file = new File(path);
        if (file.exists()){
            isLoaded = true;
            bitmap = ImageLoadUtils.getImageLoadBitmap(path, IMAGE_MAX_SIZE);
            primaryW = bitmap.getWidth();
            primaryH = bitmap.getHeight();
            matrix = new Matrix();
        }else {
            isLoaded = false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed){
            screenW = getWidth();
            screenH = getHeight();
            translationX = (screenW - bitmap.getWidth() * scale)/  2;
            translationY = (screenH - bitmap.getHeight() * scale) / 2;
            setMaxMinScale();
        }
    }

    /**
     *
     */
    private void setMaxMinScale(){
        float xScale, yScale;

        xScale = minimal / primaryW;
        yScale = minimal / primaryH;
        minScale = xScale > yScale ? xScale : yScale;

        xScale = primaryW / screenW;
        yScale = primaryH / screenH;
        if (xScale > 1 || yScale > 1 ) {
            if (xScale > yScale) {
                maxScale = 1/xScale;
            }else {
                maxScale = 1/yScale;
            }
        }else {
            if (xScale > yScale) {
                maxScale = 1/xScale;
            }else {
                maxScale = 1/yScale;
            }
        }
        if (isScaleError()){
            restoreAction();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isLoaded){
            return true;
        }
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                mFirstX = event.getX();
                mFirstY = event.getY();
                isClickInImage();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                fingerDistance = getFingerDistance(event);
                isClickInImage(event);
                break;
            case MotionEvent.ACTION_MOVE:
                float fingerNum = event.getPointerCount();
                if (fingerNum == 1 && mLocker == 0 && isClickInImage){
                    movingAction(event);
                }else if (fingerNum == 2 && isClickInImage){
                    zoomAction(event);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mLocker = 1;
                if (isScaleError()){
                    translationX = (event.getX(1) + event.getX(0)) / 2;
                    translationY = (event.getY(1) + event.getY(0)) / 2;
                }
                break;
            case MotionEvent.ACTION_UP:
                lastMoveX = -1;
                lastMoveY = -1;
                mLocker = 0;
                if (isScaleError()){
                    restoreAction();
                }
                break;
        }
        return true;
    }


    /**
     * 移动操作
     * @param event
     */
    private void movingAction(MotionEvent event){
        float moveX = event.getX();
        float moveY = event.getY();
        if (lastMoveX == -1 || lastMoveY == -1) {
            lastMoveX = moveX;
            lastMoveY = moveY;
        }
        float moveDistanceX = moveX - lastMoveX;
        float moveDistanceY = moveY - lastMoveY;
        translationX = translationX + moveDistanceX;
        translationY = translationY + moveDistanceY;
        lastMoveX = moveX;
        lastMoveY = moveY;
        invalidate();
    }

    /**
     * 缩放操作
     * @param event
     */
    private void zoomAction(MotionEvent event){
        midPoint(event);
        float currentDistance = getFingerDistance(event);
        if (Math.abs(currentDistance - fingerDistance) > 1f) {
            float moveScale = currentDistance / fingerDistance;
            scale = scale * moveScale;
            translationX = translationX * moveScale + centPointX * (1-moveScale);
            translationY = translationY * moveScale + centPointY * (1-moveScale);
            fingerDistance = currentDistance;
            invalidate();
        }
    }

    /**
     * 图片恢复到指定大小
     */
    private void restoreAction(){
        if (scale < minScale){
            scale  = minScale;
        }else if (scale > maxScale){
            scale = maxScale;
        }
        translationX = translationX - bitmap.getWidth()*scale / 2;
        translationY = translationY - bitmap.getHeight()*scale / 2;
        invalidate();
    }


    /**
     * 判断手指是否点在图片内(单指)
     */
    private void isClickInImage(){
        if (translationX <= mFirstX && mFirstX <= (translationX + currentW)
                && translationY <= mFirstY && mFirstY <= (translationY + currentH)){
            isClickInImage = true;
        }else {
            isClickInImage = false;
        }
    }

    /**
     * 判断手指是否点在图片内(双指)
     * 只要有一只手指在图片内就为true
     * @param event
     */
    private void isClickInImage(MotionEvent event){
        if (translationX <= event.getX(0) && event.getX(0) <= (translationX + currentW)
                && translationY <= event.getY(0) && event.getY(0) <= (translationY + currentH)){
            isClickInImage = true;
        }else if (translationX <= event.getX(1) && event.getX(1) <= (translationX + currentW)
                && translationY <= event.getY(1) && event.getY(1) <= (translationY + currentH)){
            isClickInImage = true;
        }else {
            isClickInImage = false;
        }
    }


    /**
     * 获取两指间的距离
     * @param event
     * @return
     */
    private float getFingerDistance(MotionEvent event){
        float x = event.getX(1) - event.getX(0);
        float y = event.getY(1) - event.getY(0);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 判断图片大小是否符合要求
     * @return
     */
    private boolean isScaleError(){
        if (scale > maxScale
                || scale < minScale){
            return true;
        }
        return false;
    }


    /**
     * 获取两指间的中点坐标
     * @param event
     */
    private void midPoint(MotionEvent event){
        centPointX = (event.getX(1) + event.getX(0))/2;
        centPointY = (event.getY(1) + event.getY(0))/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isLoaded){
            imageZoomView(canvas);
        }
    }

    private void imageZoomView(Canvas canvas){
        currentW = primaryW * scale;
        currentH = primaryH * scale;
        matrix.reset();
        matrix.postScale(scale, scale);//x轴y轴缩放
        peripheryJudge();
        matrix.postTranslate(translationX, translationY);//中点坐标移动
        canvas.drawBitmap(bitmap, matrix, null);
    }

    /**
     * 图片边界检查
     * (只在屏幕内)
     */
    private void peripheryJudge(){
        if (translationX < 0){
            translationX = 0;
        }
        if (translationY < 0){
            translationY = 0;
        }
        if ((translationX + currentW) > screenW){
            translationX = screenW - currentW;
        }
        if ((translationY + currentH) > screenH){
            translationY = screenH - currentH;
        }
    }

}

