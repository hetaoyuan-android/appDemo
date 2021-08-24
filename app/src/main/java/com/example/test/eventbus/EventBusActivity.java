package com.example.test.eventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.test.R;

import org.greenrobot.eventbus.EventBus;

public class EventBusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus);
        EventBus.getDefault().post(EventCenter.call(EventCenter.PUBLISH_PUNCH_SUCCESS, "12"));
        // 粘性事件
        EventBus.getDefault().postSticky(EventCenter.call(EventCenter.PUBLISH_PUNCH_SUCCESS, "2320"));
    }
}
