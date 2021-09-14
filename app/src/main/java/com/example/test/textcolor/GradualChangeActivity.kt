package com.example.test.textcolor

import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.example.test.R
import com.example.test.databinding.GradualChangeDataBinDing
import com.example.test.textcolor.fragment.HomeFragment
import com.example.test.textcolor.fragment.MyFragment
import com.example.test.textcolor.fragment.SettingFragment
import com.example.test.textcolor.fragment.TestFragment

class GradualChangeActivity : AppCompatActivity() {
    lateinit var db : GradualChangeDataBinDing

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        db  = DataBindingUtil.setContentView(this, R.layout.activity_gradual_change)
        title = "渐变文字Demo"
        initKotlin()
    }

    private fun initKotlin() {

        db.left.setOnClickListener {
            db.changeTextView.setSlidingPosition(GradualChangeTextView.GRADUAL_CHANGE_LEFT)
            startAnimator(db)
        }

        db.right.setOnClickListener {
            db.changeTextView.setSlidingPosition(GradualChangeTextView.GRADUAL_CHANGE_RIGHT)
            startAnimator(db)
        }

        val list = ArrayList<View>();
        repeat(10) {
            val img = ImageView(this)
            img.setBackgroundColor(Color.parseColor("#aabbcc"))
            list.add(img)
        }

        initViewPager(db)

    }

    private fun initViewPager(db: GradualChangeDataBinDing) {
        val textList = listOf(db.text1, db.text2, db.text3,db.text4)
        val list = listOf(HomeFragment(), MyFragment(), TestFragment(), SettingFragment())
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, list)
        db.viewPager.adapter = viewPagerAdapter
        db.viewPager.currentItem = 1
        textList[db.viewPager.currentItem].percent = 1f
        db.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
               if (positionOffset > 0) {
                   val left = textList[position]
                   val right = textList[position + 1]
                   left.setSlidingPosition(GradualChangeTextView.GRADUAL_CHANGE_RIGHT)
                   right.setSlidingPosition(GradualChangeTextView.GRADUAL_CHANGE_LEFT)
                   left.percent = 1 - positionOffset
                   right.percent = positionOffset
               }
            }

            override fun onPageSelected(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onPageScrollStateChanged(state: Int) {
                //当 ViewPage结束的时候,重新设置一下状态 [不设置的话会有'残影']
                textList.forEach {
                    if (it.tag == textList[db.viewPager.currentItem].tag) {
                        it.percent = 1f
                    } else {
                        it.percent = 0f
                    }
                }
            }

        })

        //点击事件处理
        repeat(db.linear.childCount) {
            val view = db.linear.getChildAt(it)
            view.tag = it
            view.setOnClickListener {
                db.viewPager.currentItem = view.tag as Int
            }
        }
    }


    private fun startAnimator(db: GradualChangeDataBinDing, time: Long = 3000) {
        ObjectAnimator.ofFloat(db.changeTextView, "percent", 0f, 1f).apply {
            duration = time
            start()
        }
    }
}