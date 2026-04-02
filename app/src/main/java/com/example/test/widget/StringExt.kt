package com.genew.mpublic.helper

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.example.test.AppApplication


/**
 * @author yanteng
 * @date 2026-03-31 15:30
 * @description
 */

fun String.copyToClipboard() {
    try {
        val clipboardManager = AppApplication.getApp().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Label", this) // "Label"是剪贴数据的标签，可自定义
        clipboardManager.setPrimaryClip(clipData)
//        ToastUtils.showShort(Utils.getApp().getString(R.string.copy_to_clipboard))
    }catch (e : Exception) {

    }

}
