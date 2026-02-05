package com.example.test.contact

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.test.BaseActivity
import com.example.test.R
import com.github.promeg.pinyinhelper.Pinyin

/**
 * 通讯录主页面
 * 继承自 BaseActivity，自动获得蓝色 Toolbar、标题设置和返回键功能
 */
class ContactActivity : BaseActivity() {

    private val displayList = mutableListOf<IBaseItem>()
    private lateinit var adapter: CityAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var rv: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    // --- 1. 实现基类约定的配置 ---

    override fun getLayoutId(): Int = R.layout.activity_contact

    override fun getPageTitle(): String = "我的通讯录"

    // 注意：不再需要重写 onCreate，逻辑全部移至 initView
    override fun initView() {
        // 初始化视图控件
        setupRecyclerView()
        setupSwipeRefresh()
        setupIndexBar()

        // 初始加载数据
        loadData()
    }

    // --- 2. 业务初始化逻辑 ---

    private fun setupRecyclerView() {
        rv = findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager

        adapter = CityAdapter(displayList)
        rv.adapter = adapter

        // 性能优化
        rv.setHasFixedSize(true)
        rv.itemAnimator = null
    }

    private fun setupSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.swipeRefresh)
        swipeRefreshLayout.apply {
            // 使用你项目定义的品牌色
            setColorSchemeResources(R.color.blue_line_color_dark)
            // 调整进度条偏移量，避免被 Toolbar 遮挡
            setProgressViewOffset(true, 50, 150)
        }

        swipeRefreshLayout.setOnRefreshListener {
            loadData()
            // 模拟加载完成，实际开发中应在异步回调中关闭
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun setupIndexBar() {
        val indexBar = findViewById<QuickIndexBar>(R.id.indexBar)
        indexBar.onLetterChangeListener = { letter ->
            if (letter == "↑") {
                // 平滑滚动到顶部
                rv.smoothScrollToPosition(0)
            } else {
                // 寻找对应字母 Header 的位置
                val pos = displayList.indexOfFirst {
                    it is LetterHeader && it.letter == letter
                }
                if (pos != -1) {
                    // 精准置顶定位
                    layoutManager.scrollToPositionWithOffset(pos, 0)
                }
            }
        }
    }

    // --- 3. 数据处理逻辑 ---

    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        val rawData = getContactData()

        // A-Z 排序逻辑
        val sorted = rawData.map { name ->
            var py = Pinyin.toPinyin(name, "").uppercase()

            // 特殊地名/多音字修正
            if (name.startsWith("重庆")) py = "CHONGQING"
            if (name.startsWith("厦门")) py = "XIAMEN"

            val firstChar = py[0]
            val tag = if (firstChar in 'A'..'Z') firstChar.toString() else "#"
            CityItem(name, py, tag)
        }.sortedWith(compareBy(
            { it.tag != "#" }, // '#' 排在最前
            { it.tag },        // A-Z 排序
            { it.pinyin }      // 同组内按拼音排序
        ))

        // 扁平化：构造带 Header 的列表
        displayList.clear()
        var lastTag = ""
        sorted.forEach { item ->
            if (item.tag != lastTag) {
                lastTag = item.tag
                displayList.add(LetterHeader(lastTag))
            }
            displayList.add(item)
        }

        adapter.notifyDataSetChanged()
    }

    private fun getContactData(): List<String> {
        return listOf(
            "阿坝", "北京", "包头", "成都", "重庆", "大连", "佛山", "广州",
            "杭州", "济an", "昆明", "兰州", "南京", "平顶山", "青岛", "上海",
            "深圳", "天津", "武汉", "西安", "厦门", "郑州", "123服务台", "@未知用户"
        )
    }
}