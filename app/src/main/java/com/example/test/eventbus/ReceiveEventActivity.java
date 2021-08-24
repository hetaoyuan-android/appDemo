package com.example.test.eventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.test.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ReceiveEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_event);
        EventBus.getDefault().register(this);


    }

    /**
     *
     * @Subscribe(threadMode = ThreadMode.MAIN)
     *     ThreadMode.MAIN：表示这个方法在主线程中执行
     *     ThreadMode.BACKGROUND：表示该方法在后台执行，不能并发处理
     *     ThreadMode.ASYNC：也表示在后台执行，可以异步并发处理
     *     ThreadMode.POSTING：表示该方法和消息发送方在同一个线程中执行
     *     @Subscribe(threadMode = ThreadMode.MAIN, priority = 100)
     *      //终止事件往下传递，事件的优先级类似广播的优先级，优先级越高优先获得消息
     *      EventBus.getDefault().cancelEventDelivery(event);
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventBus(EventCenter event) {
        switch (event.getEventType()) {
            //发布打卡成功
            case EventCenter.PUBLISH_PUNCH_SUCCESS:
                String bundleId = event.getBundleId();
                Log.e("activity", "onEventBus: " + bundleId);
                break;

        }
    }



    //和之前的方法一样,只是多了一个 sticky = true 的属性.
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(EventCenter event) {
        switch (event.getEventType()) {
            //发布打卡成功
            case EventCenter.PUBLISH_PUNCH_SUCCESS:
                String bundleId = event.getBundleId();
                Log.e("activity", "onEventBus: " + bundleId);
                break;

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //移除所有粘性事件
        EventBus.getDefault().removeAllStickyEvents();
        //移除指定类型的粘性事件
        //EventBus.getDefault().removeStickyEvent(new StickyEvent("我是粘性事件"));
    }
}
