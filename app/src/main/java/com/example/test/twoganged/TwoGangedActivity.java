package com.example.test.twoganged;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.test.R;
import com.example.test.twoganged.view.ExpandableGridViewActivity;
import com.example.test.twoganged.view.ExpandableListViewActivity;
import com.example.test.twoganged.view.ListListActivity;
import com.example.test.twoganged.view.ScrollGridActivity;

public class TwoGangedActivity extends Activity {

	private Button listlistview, listgridview, expandableListView,
			expandableGridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two_genged);
		listlistview = (Button) findViewById(R.id.listlist);
		listgridview = (Button) findViewById(R.id.listgrid);
		expandableListView = (Button) findViewById(R.id.expandableListView);
		expandableGridView = (Button) findViewById(R.id.expandableGridView);

		listlistview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(TwoGangedActivity.this, ListListActivity.class);
				startActivity(intent);
			}
		});

		listgridview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(TwoGangedActivity.this, ScrollGridActivity.class);
				startActivity(intent);
			}
		});

		expandableListView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(TwoGangedActivity.this,
						ExpandableListViewActivity.class);
				startActivity(intent);
			}
		});

		expandableGridView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(TwoGangedActivity.this,
						ExpandableGridViewActivity.class);
				startActivity(intent);
			}
		});
	}

}
