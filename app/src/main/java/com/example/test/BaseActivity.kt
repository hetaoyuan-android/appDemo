package com.example.test

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.LayoutInflaterCompat
import com.example.test.skin.SkinFactory
import com.example.test.skin.SkinManager
import com.example.test.skin.SkipUpdateEvent
import com.example.test.skin.ThemeConstant
import org.greenrobot.eventbus.EventBus

/**
 * 作者: yuanhetao
 * 时间: 2026/2/5
 * 描述:
 */
abstract class BaseActivity : AppCompatActivity(), SkinManager.SkinUpdateListener  {

    private var mSkinFactory: SkinFactory? = null
    var TAG: String = javaClass.getSimpleName()
    override fun onCreate(savedInstanceState: Bundle?) {
        mSkinFactory = SkinFactory(this)
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), mSkinFactory!!)
        SkinManager.getInstance().addSkinUpdateListener(TAG, this)
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

    override fun onSkinUpdate() {
        if (mSkinFactory != null) {
            mSkinFactory!!.apply()
            changeStatusBar()
            EventBus.getDefault().post(SkipUpdateEvent())
        }
    }

    fun changeStatusBar() {
        when (SkinManager.getInstance().mSkinSuffix) {
            ThemeConstant.DARK -> {
                setSystemBarFontColor(false)
            }

            ThemeConstant.GREEN -> {
                setSystemBarFontColor(false)
            }

            else -> {
                setSystemBarFontColor(true)
            }
        }
    }

    fun setSystemBarFontColor(dark: Boolean) {
        if (dark) {
            getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        } else {
            getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        }
        getWindow().setStatusBarColor(getColor(R.color.transparent))
    }

    /**
     * 初始化 ToolBar 和状态栏占位符
     *
     * @param placeholderId          状态栏占位View的资源ID（可以为0表示不需要）
     */
    protected fun initIdToolBar(placeholderId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = this.getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.setStatusBarColor(Color.TRANSPARENT)
            window.getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }

        // 获取状态栏高度
        val statusBarHeight: Int = getStatusBarHeight(this)
        // 设置状态栏占位View高度
        if (placeholderId != 0) {
            val statusBarPlaceholder: View? = findViewById(placeholderId)
            if (statusBarPlaceholder != null) {
                val params = statusBarPlaceholder.getLayoutParams() as LinearLayout.LayoutParams
                params.height = statusBarHeight
                statusBarPlaceholder.setLayoutParams(params)
            }
        }
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId =
            context.getResources().getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId)
        } else {
            // 如果没有找到资源，尝试使用默认值（如 24dp）
            result = dip2px(context, 24f)
            Log.w("StatusBarUtils", "无法获取系统状态栏高度，默认使用 24dp (" + result + "px)")
        }
        return result
    }



    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.getResources().getDisplayMetrics().density
        return (dipValue * scale + 0.5f).toInt()
    }

}