package com.example.test.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.util.Log;
import android.view.View;


/**
 * 注解工具类
 * Annotation Utils
 */
public class ViewUtils {


    private static final String TAG = "RRL";


    /**
     * find view in activity
     *
     * @param object
     */
    public static void inject(Object object) {
        long time = System.currentTimeMillis();
        reflectFindView(object, null);
        injectMethod(object, null);
        Log.e(TAG, ViewUtils.class.getSimpleName() + " inject use time:" + (System.currentTimeMillis() - time) + "ms");
    }


    /**
     * find view in fragment or ViewHolder
     *
     * @param object Fragment or ViewHolder
     * @param view   there is findViewById method view
     */
    public static void inject(Object object, View view) {
        long time = System.currentTimeMillis();
        reflectFindView(object, view);
        injectMethod(object, view);
        Log.e(TAG, ViewUtils.class.getSimpleName() + " inject use time:" + (System.currentTimeMillis() - time) + "ms");
    }


    /**
     * 反射找到View Id
     * Find view id by reflect
     *
     * @param object Activity Fragment or ViewHolder
     * @param view   there is findViewById method view
     */
    private static void reflectFindView(Object object, View view) {
        //Access to all of the fields
        Class<?> fieldClass = object.getClass();
        Field[] fields = fieldClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            //if every field have you defined annotation class(ViewInject.class) and get view id.
            Field field = fields[i];
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {//if you defined annotation class's values is not null
                reflectFindView(object, view, field, viewInject.value());
            }
        }
    }


    /**
     * 反射找控件
     * find view by reflect class
     *
     * @param field annotation field
     * @param view  findViewById class
     * @param id    view's Id
     */
    private static void reflectFindView(Object object, View view, Field field, int id) {
        if (id == -1) {
            return;
        }
        Class<?> fieldCls = object.getClass();
        Class<?> findViewClass = (view == null ? object : view).getClass();
        try {
            // 获取类中的findViewById方法，参数为int
            Method method = (findViewClass == null ? fieldCls : findViewClass).getMethod("findViewById", int.class);
            // 执行该方法，返回一个Object类型的View实例
            Object resView = method.invoke(view == null ? object : view, id);
            field.setAccessible(true);
            // 把字段的值设置为该View的实例
            field.set(object, resView);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * Annotation method
     *
     * @param object annotation class with declared methods
     * @param view   annotation view in Fragment or ViewHolder
     */
    private static void injectMethod(Object object, View view) {
        long time = System.currentTimeMillis();
        reflectMethod(object, view);
        Log.e(TAG, ViewUtils.class.getSimpleName() + " inject use time:" + (System.currentTimeMillis() - time) + "ms");
    }


    /**
     * reflect method
     *
     * @param object annotation class with declared methods
     * @param view   annotation view in Fragment or ViewHolder
     */
    private static void reflectMethod(Object object, View view) {
        Class<?> objectClass = object.getClass();
        Method[] objectMethods = objectClass.getDeclaredMethods();
        for (int i = 0; i < objectMethods.length; i++) {
            Method method = objectMethods[i];
            method.setAccessible(true);//if method is private
            //get annotation method
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                int[] values = onClick.myValue();
                for (int index = 0; index < values.length; index++) {
                    int id = values[index];
                    reflectMethod(object, view, id,index, method);
                }
            }
        }
    }


    /**
     * reflect method
     *
     * @param object annotation class with declared methods
     * @param view   annotation view in Fragment or ViewHolder
     * @param id     annotation view id
     * @param method annotation method
     */
    private static void reflectMethod(final Object object, View view, int id,int index, final Method method) {
        Class<?> objectClass = object.getClass();
        try {
            Method findViewMethod = (view == null ? objectClass : view.getClass()).getMethod("findViewById", int.class);
            final View resView = (View) findViewMethod.invoke(object, id);
            if (resView == null) {
                Log.e(TAG, "@OnClick annotation value view id (index = "+index+") isn't find any view in " + object.getClass().getSimpleName());
                return;
            }
            resView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        method.invoke(object, resView);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
