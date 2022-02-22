package com.example.test.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CoroutineActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)
    }



    fun testCoroutineLaunch() {
        val scope = CoroutineScope(Job())
        val job = scope.launch {
            println("running launch")
        }
        job.cancel()
    }
}