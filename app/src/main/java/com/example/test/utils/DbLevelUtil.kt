package com.example.test.utils

import com.example.test.R


object DbLevelUtil {
    fun getDbLevelDrawable(level: Int): Int {
        return when (level) {
            0 -> R.drawable.recorder_start_0
            1 -> R.drawable.recorder_start_1
            2 -> R.drawable.recorder_start_2
            3 -> R.drawable.recorder_start_3
            4 -> R.drawable.recorder_start_4
            5 -> R.drawable.recorder_start_5
            6 -> R.drawable.recorder_start_6
            7, 8 -> R.drawable.recorder_start_7
            else -> R.drawable.recorder_start_7
        }
    }
}