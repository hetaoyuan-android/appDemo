package com.example.test.photopicker.selector;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.photopicker.selector.touchgallery.GalleryWidget.FilePagerAdapter;
import com.example.test.photopicker.selector.touchgallery.GalleryWidget.GalleryViewPager;

public class GalleryFileActivity extends Activity {
	private GalleryViewPager gvp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gallery_activity);
		initView();
	}

	private void initView() {
		Intent intent = getIntent();
		ArrayList<String> paths = intent.getStringArrayListExtra("imagePathes");
		if (paths==null) {
			Toast.makeText(this, "没有获取到数据", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		gvp = (GalleryViewPager) findViewById(R.id.viewer);
		gvp.setAdapter(new FilePagerAdapter(GalleryFileActivity.this, paths));
	}
	
}
