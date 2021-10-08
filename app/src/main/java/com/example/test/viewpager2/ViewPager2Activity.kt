package com.example.test.viewpager2

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.test.R
import java.util.ArrayList

class ViewPager2Activity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpager2)
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager2)
        val myAdapter = MyAdapter()
        var data: ArrayList<Int> = ArrayList()
        data.add(0)
        data.add(1)
        data.add(2)
        data.add(3)
        myAdapter.setList(data)
        viewPager2.adapter = myAdapter

        //禁止滑动
        //viewPager2.isUserInputEnabled = false
        //设置间距
//        viewPager2.setPageTransformer(MarginPageTransformer(resources.getDimension(R.dimen.dp_10).toInt()))
        //缩放效果
//        val compositePageTransformer = CompositePageTransformer()
//        compositePageTransformer.addTransformer(ScaleInTransformer())
//        compositePageTransformer.addTransformer(MarginPageTransformer(resources.getDimension(R.dimen.dp_10).toInt()))
//        viewPager2.setPageTransformer(compositePageTransformer)

        //ViewPager2的一屏多页效果
        viewPager2.apply {
            offscreenPageLimit=1
            val recyclerView= getChildAt(0) as RecyclerView
            recyclerView.apply {
                val padding = resources.getDimensionPixelOffset(R.dimen.dp_10) +
                        resources.getDimensionPixelOffset(R.dimen.dp_10)

                setPadding(padding, 0, padding, 0)
                clipToPadding = false
            }
        }
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(ScaleInTransformer())
        compositePageTransformer.addTransformer(MarginPageTransformer(resources.getDimension(R.dimen.dp_10).toInt()))
        viewPager2.setPageTransformer(compositePageTransformer)


        viewPager2.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Toast.makeText(this@ViewPager2Activity, "page selected $position", Toast.LENGTH_SHORT).show()
            }
        })
    }
}