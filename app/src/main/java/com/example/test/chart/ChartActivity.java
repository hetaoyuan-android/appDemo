package com.example.test.chart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.test.R;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {


    PieChart pieChart;
    ArcView arcView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
//        int[] color = {getApplication().getResources().getColor(R.color.colorAccent), getApplication().getResources().getColor(R.color.colorPrimaryDark), 0xFFE32636, 0xFF800000};
//        pieChart = findViewById(R.id.pie_chat);
//        List<PieChartBean> list = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            PieChartBean bean = new PieChartBean();
//            bean.setColor(color[i]);
//            bean.setVaule(i + 1);
//            list.add(bean);
//        }
//        pieChart.setData(list);
//        pieChart.startAnimation(3000);
        arcView =findViewById(R.id.arc);
        testArcView();

    }

    private void testArcView() {


        List<Times> times = new ArrayList<>();
//        for (int i = 4; i > 0; i--) {
//            Times t = new Times();
//            t.hour = i;
//            t.text = "Number"+i;
//            times.add(t);
//        }
        times.add(new Times(8,"PC"));
        times.add(new Times(15,"App"));
        times.add(new Times(15,"H5"));
        times.add(new Times(10,"web"));

        ArcView.ArcViewAdapter myAdapter = arcView.new ArcViewAdapter<Times>(){
            @Override
            public double getValue(Times times) {
                return times.hour;
            }

            @Override
            public String getText(Times times) {
                return times.text;
            }
        };//设置adapter
        myAdapter.setData(times);//设置数据集
        arcView.setMaxNum(5);//设置可以显示的最大数值 该数值之后的会合并为其他
        //  arcView.setRadius(150);//设置圆盘半径
    }

    class Times {
        double hour;
        String text;

        public Times(double hour, String text) {
            this.hour = hour;
            this.text = text;
        }

        public double getHour() {
            return hour;
        }

        public void setHour(double hour) {
            this.hour = hour;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
