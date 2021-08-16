package com.example.test.circle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.test.R;

public class CircleActivity extends AppCompatActivity {
    private int mTotalProgress = 90;
    private int mCurrentProgress = 0;
    //进度条
    private AnnulusView mTasksView;

    private CircleProgress circleProgress;
    private CircleProgressOne circleProgressOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);

        mTasksView = (AnnulusView) findViewById(R.id.tasks_view);
//        mTasksView.setProgress(1,0);


//        new Thread(new ProgressRunable()).start();



        circleProgress = findViewById(R.id.circleProgress);
        circleProgress.setOnCircleProgressListener(new CircleProgress.OnCircleProgressListener() {
            @Override
            public void onProgressChange(int progress) {
                circleProgress.setCircleText("签到");
            }
        });

        circleProgressOne = findViewById(R.id.circleProgressOne);
        circleProgressOne.setOnCircleProgressListener(new CircleProgressOne.OnCircleProgressListener() {
            @Override
            public void onProgressChange(int progress) {
                circleProgressOne.setCircleText(progress/10 + "s");
            }
        });



    }

    public void start(View view) {
        circleProgress.startAnimProgress(90);
        circleProgressOne.startAnimProgress(30 * 10, 30 * 1000);

//        mTasksView.startAnimProgress(90);
    }
//
//        public void progress(View view) {
//            circleProgress.setProgress(50);
//        }

    class ProgressRunable implements Runnable {
        @Override
        public void run() {
            while (mCurrentProgress < mTotalProgress) {
                mCurrentProgress += 1;
                mTasksView.setProgress(mCurrentProgress, 90);
                try {
                    Thread.sleep(90);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
