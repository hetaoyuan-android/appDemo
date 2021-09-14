package com.example.test.textcolor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTest {

    private ExecutorService mThreadPool = Executors.newCachedThreadPool();
    private List<String> mList = new CopyOnWriteArrayList<>();
    private static final int mCount = 100;

    private volatile boolean hasSendMessage = false;


    /**
     * 执行多线程任务
     */
    public void startTask() {
        mList.clear();
        for (int i = 0; i < mCount; i++) {
            final String item = String.valueOf(i);
            mThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    doTask(item);
                }
            });
        }
    }

    /**
     * 子线程执行任务
     *
     * @param item
     */
    private void doTask(String item) {
        System.out.println("线程" + Thread.currentThread().getName() + "执行doTask");
        mList.add(item);
        // 以下方法拿到的size，并不是其他线程添加item之后的新size
        System.out.println("线程" + Thread.currentThread().getName() + " mList.size()=" + mList.size());
        if (mList.size() == mCount && !hasSendMessage /*添加了hasSendMessage的判断，保证 sendMessage 只执行一次*/) {
            hasSendMessage = true;
            sendMessage();
        }
    }


    /**
     * 期望该方法只执行一次
     */
    public void sendMessage() {
        System.out.println("线程" + Thread.currentThread().getName() + "执行sendMessage");
        mThreadPool.shutdown();
    }

    public static void main(String[] args) {
        ThreadPoolTest threadPoolTest = new ThreadPoolTest();
        threadPoolTest.startTask();
    }
}
