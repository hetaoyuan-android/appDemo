package com.example.test.annotation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.test.R;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AnnotationActivity extends AppCompatActivity {

    /**
     * activity
     *      @ViewInject(R.id.tv_content)
     *     private TextView tv_content;
     *     //在onCreate()中setContentView()方法之后调用
     *     ViewUtils.inject(this);
     *
     * fragment
     *      @ViewInject(R.id.tv_content)
     *     private TextView tv_content;
     *     @Override
     *     public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
     *         View view = inflater.inflate(R.layout.fgt_main, null);
     *         ViewUtils.inject(this, view);
     *         return view;
     *     }
     *
     *
     *  在ViewHolder中使用：
     *          @Override
     *         public View getView(int position, View convertView, ViewGroup parent) {
     *             ViewHolder holder;
     *             if (convertView == null) {
     *                 holder = new ViewHolder();
     *                 convertView = LayoutInflater.from(MainFgt.this.getContext()).inflate(R.layout.list_item_content, null);
     *                 ViewUtils.inject(holder, convertView);
     *                 convertView.setTag(holder);
     *             } else
     *                 holder = (ViewHolder) convertView.getTag();
     *             holder.tv_list_content.setText(position + "测试文字");
     *             return convertView;
     *         }
     *
     *         private class ViewHolder {
     *             @ViewInject(R.id.tv_list_content)
     *             private TextView tv_list_content;
     *        }
     */

    @LcsBindView(R.id.three_bt)
    private Button btnFindViewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotation);
        InjectUtil.inject(this);
        InjectUtil.bindViews(this);
        ViewUtils.inject(this);
        btnFindViewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AnnotationActivity.this, "findViewById", Toast.LENGTH_SHORT).show();
            }
        });
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
