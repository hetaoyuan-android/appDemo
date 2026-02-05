package com.example.test

import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity

/**
 * 作者: yuanhetao
 * 时间: 2026/2/5
 * 描述:
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        // 1. 初始化 Toolbar
        val toolbar = findViewById<Toolbar>(R.id.common_toolbar)
        setSupportActionBar(toolbar)

        // 2. 设置标题（优先取子类定义的标题，否则取清单文件里的 label）
        val title = getPageTitle()
        if (title.isNotEmpty()) {
            supportActionBar?.title = title
        }

        // 3. 配置左上角返回按钮
        if (showBackIcon()) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
        }
        toolbar.navigationIcon?.let { icon ->
            androidx.core.graphics.drawable.DrawableCompat.setTint(icon, android.graphics.Color.WHITE)
        }

        // 4. 填充业务布局到内容容器
        val container = findViewById<FrameLayout>(R.id.container)
        layoutInflater.inflate(getLayoutId(), container)

        initView()
    }

    // --- 给子类提供的配置项 ---

    abstract fun getLayoutId(): Int
    abstract fun initView()

    /** 子类重写此方法返回具体标题，默认返回空字符串 */
    open fun getPageTitle(): String = ""

    /** 默认显示返回键，首页可重写返回 false */
    open fun showBackIcon(): Boolean = true

    // --- 统一逻辑处理 ---

    /** 响应左上角返回按钮点击 */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed() // 调用系统的返回逻辑（finish当前页面）
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}