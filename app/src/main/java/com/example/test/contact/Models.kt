package com.example.test.contact

/**
 * 作者: yuanhetao
 * 时间: 2026/2/4
 * 描述:
 */
// 混合列表的基类接口
// 混合列表的基类接口
interface IBaseItem {
    val itemType: Int
}

// 字母块模型 (例如 "A", "B")
data class LetterHeader(val letter: String) : IBaseItem {
    override val itemType = 0
}

// 城市/联系人模型：增加 tag 字段
data class CityItem(
    val name: String,   // 显示的名称，如 "北京"
    val pinyin: String, // 全拼，用于组内排序，如 "BEIJING"
    val tag: String     // 索引标签，用于分类和右侧定位，如 "B"
) : IBaseItem {
    override val itemType = 1
}