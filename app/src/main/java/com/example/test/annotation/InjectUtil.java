package com.example.test.annotation;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectUtil {

    private static final String TAG = InjectUtil.class.getSimpleName();

    public static void inject(Activity activity) {
        if (activity == null) {
            return;
        }
        //得到类对象
        Class<? extends Activity> clz = activity.getClass();
        //获取类对象中的所有方法
        Method[] methods = clz.getDeclaredMethods();
        //判断是否存在成员方法
        if (methods == null) {
            return;
        }
        //遍历方法
        for (Method m : methods) {
            //获取方法上的所有注解
            Annotation[] annotations = m.getAnnotations();
            //判断是否存在注解
            if (annotations == null) {
                continue;
            }
            //遍历所有注解
            for (Annotation a : annotations) {
                //得到注解类型对象
                Class<? extends Annotation> annotationType = a.annotationType();
                if (annotationType.isAnnotationPresent(EventType.class)) {
                    //得到具体注解对象
                    EventType eventType = annotationType.getAnnotation(EventType.class);
                    //取值
                    Class listenerType = eventType.listenerType();
                    Log.d(TAG, "inject: " + listenerType);
                    String listenerSetter = eventType.listenerSetter();
                    try {
                        //得到标记有OnClick 和 OnLongClick 注解的方法
                        Method valueMethod = annotationType.getDeclaredMethod("myValue");
                        //得到所有需要点击控件的id 也就是注解value
                        int[] ids = (int[]) valueMethod.invoke(a);
                        //设置权限
                        m.setAccessible(true);
                        //InvocationHandler接口是proxy代理实例的调用处理程序实现的一个接口，
                        // 每一个proxy代理实例都有一个关联的调用处理程序；在代理实例调用方法时，方法调用被编码分派到调用处理程序的invoke方法
                        ListenerInvocationHandler invocationHandler = new ListenerInvocationHandler(m, activity);
                        //创建代理对象 最终会回调我们定义注解的方法
                        Object proxyInstance = Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{listenerType}, invocationHandler);
                        for (int id : ids) {
                            View view = activity.findViewById(id);
                            Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                            setter.invoke(view, proxyInstance);
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class ListenerInvocationHandler<T> implements InvocationHandler {
        private Method method;
        private T target;

        public ListenerInvocationHandler(Method method, T target) {
            this.target = target;
            this.method = method;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return this.method.invoke(target, args);
        }
    }

    /**
     * findViewById
     */
    public static void bindViews(Activity activity) {
        Class<? extends Activity> activityClass = activity.getClass(); //获取activity的class
        Field[] fields = activityClass.getDeclaredFields();  ////获取activity的字段
        //遍历所有的字段
        for (Field field : fields) {
            //获取该字段的注解
            LcsBindView lcsBindView = field.getAnnotation(LcsBindView.class);
            //!=null 说明该字段有注解并且是指定的注解
            if (lcsBindView != null) {
                int viewId = lcsBindView.value();
                try {
                    //获取到activity中findViewById的方法
                    Method findViewByIdMethod = activityClass.getMethod("findViewById", int.class);
                    try {
                        //执行findViewById方法
                        Object resView = findViewByIdMethod.invoke(activity, viewId);
                        //允许通过反射访问私有变量
                        field.setAccessible(true);
                        //把字段的值设置该view的实例
                        field.set(activity, resView);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}