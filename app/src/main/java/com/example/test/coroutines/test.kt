package com.example.test.coroutines

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun main() {

    GlobalScope.launch(context = Dispatchers.IO) {
        delay(1000)
        Log.e("test","launch")
    }

}