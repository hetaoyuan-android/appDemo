package com.example.test.annotation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.test.R;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AnnotationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
        InjectUtil.inject(this);
    }

    @OnClick(myValue = R.id.one_bt)
    public void onClick(View view) {
        Toast.makeText(AnnotationActivity.this, "点击", Toast.LENGTH_SHORT).show();
    }

    @OnLongClick(myValue = R.id.two_bt)
    public boolean onLongClick(View view) {
        Toast.makeText(AnnotationActivity.this, "长按", Toast.LENGTH_SHORT).show();
        return true;
    }


    public static void injectOnClick(final Activity activity) {
        Class<? extends Activity> cls = activity.getClass();

        Method[] declaredMethods = cls.getDeclaredMethods();

        if (declaredMethods == null && declaredMethods.length < 0) {
            return;

        }

        for (final Method declaredMethod : declaredMethods) {
            if (declaredMethod.isAnnotationPresent(OnClick.class)) {
                OnClick onClick = declaredMethod.getAnnotation(OnClick.class);

                try {
                    int[] ids = onClick.myValue();

                    Object proxy = Proxy.newProxyInstance(View.OnClickListener.class.getClassLoader(), new Class[]{View.OnClickListener.class},

                            new InvocationHandler() {
                                @Override

                                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                    return declaredMethod.invoke(activity, args);

                                }

                            });

                    for (int id : ids) {
                        View view = activity.findViewById(id);

                        Method setOnClickListener = view.getClass().getMethod("setOnClickListener", View.OnClickListener.class);

                        setOnClickListener.invoke(view, proxy);

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
