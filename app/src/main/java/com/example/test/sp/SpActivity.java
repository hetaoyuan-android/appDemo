package com.example.test.sp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.example.test.R;

public class SpActivity extends AppCompatActivity {

    private Button button,read;
    private TextView text;
    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp);

        button = findViewById(R.id.submit);
        read = findViewById(R.id.read);
        text = findViewById(R.id.text);
        mWebView = findViewById(R.id.web_view);
        WebSettings setting = mWebView.getSettings();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8") ;
        mWebView.setBackgroundColor(0); // 设置背景色
//        mWebView.getBackground().setAlpha(2); // 设置填充透明度 范围：0-255
        mWebView.loadDataWithBaseURL(null, "加载中。。", "text/html", "utf-8",null);
        mWebView.loadUrl("https://www.baidu.com");
        mWebView.setVisibility(View.VISIBLE); // 加载完之后进行设置显示，以免加载时初始化效果不好看

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefsUtils.get(SpActivity.this, "abc").putString("text","yuanhetao");
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textStr = SharedPrefsUtils.get(SpActivity.this).getString("text", "11");
                text.setText(textStr);
            }
        });
    }
}
