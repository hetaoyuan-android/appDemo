package com.example.test.bigimage
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davemorrissey.labs.subscaleview.ImageSource

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.example.test.R

class LargeImageActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_large_image)
        findViewById<SubsamplingScaleImageView>(R.id.imageView).setImage(ImageSource.resource(R.mipmap.card))
    }


}