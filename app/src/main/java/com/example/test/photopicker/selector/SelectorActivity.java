package com.example.test.photopicker.selector;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;

import com.example.test.R;

public class SelectorActivity extends Activity {
	/**
	 * 扫描结束
	 */
	protected static final int SCAN_OVER = 0x1110;
	private GridView mGridView;
	private TextView tv_dir;
	private TextView tv_preview;
	private List<FolderBean> mFolderBeans = new ArrayList<FolderBean>();
	private ProgressDialog progressDialog;
	private int mMaxCount;
	private File mCurrentFile;
	private List<String> mImages = new ArrayList<String>();
	private Set<String> mSelectedImags = new HashSet<String>();
	private int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;
	/**
	 * 获取自定义的ImageLoader
	 */
	private YangeImageLoader yangeImageLoader = YangeImageLoader.getInstance();
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCAN_OVER:
				if (mCurrentFile != null) {
					tv_dir.setText(mCurrentFile.getName());
					tv_preview.setText(mMaxCount + "张");
					mImages = Arrays.asList(mCurrentFile.list(getFilenameFilter()));
					mGridView.setAdapter(new ImageAdapter(SelectorActivity.this, mImages, mCurrentFile.getAbsolutePath()));
				} else {
					Toast.makeText(SelectorActivity.this, "未扫描到任何图片！", Toast.LENGTH_LONG).show();
				}
				break;

			default:
				break;
			}
		};

	};
	private Button btn_send;
	private ListImageDirPopWindow mPopWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_selector);
		initView();
		initData();
		initPopupWindow();
	}

	private void initData() {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "当前存储卡不可用", Toast.LENGTH_SHORT).show();
			return;
		}
		progressDialog = ProgressDialog.show(this, "正在扫描图片", "请稍等片刻");
		new Thread() {
			@Override
			public void run() {
				Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver cr = SelectorActivity.this.getContentResolver();
				Cursor cursor = cr.query(imageUri, null, MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?", new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);
				Set<String> dirPaths = new HashSet<String>();
				while (cursor.moveToNext()) {
					String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					File parentFile = new File(path).getParentFile();
					if (parentFile == null) {
						continue;
					}
					String dirPath = parentFile.getAbsolutePath();
					if (dirPaths.contains(dirPath)) {
						continue;
					} else {
						dirPaths.add(dirPath);
						FolderBean folderBean = new FolderBean();
						folderBean.setDir(dirPath);
						folderBean.setFirstImgPath(path);
						if (parentFile.list() == null) {
							continue;
						}
						int picCount = parentFile.list(getFilenameFilter()).length;
						folderBean.setCount(picCount);
						if (picCount > mMaxCount) {
							mMaxCount = picCount;
							mCurrentFile = parentFile;
						}
						mFolderBeans.add(folderBean);

					}
				}
				mHandler.sendEmptyMessage(SCAN_OVER);
				cursor.close();
				progressDialog.dismiss();

			};
		}.start();

	}

	private void initPopupWindow() {
		mPopWindow = new ListImageDirPopWindow(SelectorActivity.this, mFolderBeans);
		mPopWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						lightOn();
					}
				}, 400);
			}

		});
		mPopWindow.setOnDirSelectedListener(new ListImageDirPopWindow.OnDirSelectedListener() {

			@Override
			public void onSelected(FolderBean foler) {
				tv_dir.setText(foler.getName());
				tv_preview.setText(foler.getCount() + "张");
				mCurrentFile = new File(foler.getDir());
				mImages = Arrays.asList(mCurrentFile.list(getFilenameFilter()));
				mGridView.setAdapter(new ImageAdapter(SelectorActivity.this, mImages, mCurrentFile.getAbsolutePath()));
			}
		});
	}

	private void initView() {
		mGridView = (GridView) findViewById(R.id.gv);
		mGridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mScrollState = scrollState;
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});
		tv_dir = (TextView) findViewById(R.id.tv_dir);
		tv_preview = (TextView) findViewById(R.id.tv_priview);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SelectorActivity.this, GalleryFileActivity.class);
				if (mSelectedImags == null || mSelectedImags.size() < 1) {
					Toast.makeText(SelectorActivity.this, "您还未选择图片", Toast.LENGTH_LONG).show();
					return;
				}
				intent.putStringArrayListExtra("imagePathes", new ArrayList<String>(mSelectedImags));
				startActivity(intent);
			}
		});
		final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
		rl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopWindow.setAnimationStyle(R.style.dir_popupwindow);
				mPopWindow.showAsDropDown(rl, 0, 0);
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						lightOff();
					}
				}, 400);

			}
		});
	}

	/**
	 * 关灯
	 */
	protected void lightOff() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.2f;
		getWindow().setAttributes(lp);
	}

	/**
	 * 开灯
	 */
	private void lightOn() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 1.0f;
		getWindow().setAttributes(lp);
	}

	/**
	 * 获取文件过滤器
	 * 
	 * @return
	 */
	private FilenameFilter getFilenameFilter() {
		return new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				if (filename == null) {
					return false;
				}
				if (filename.toLowerCase(Locale.CHINA).endsWith(".jpeg") || filename.toLowerCase(Locale.CHINA).endsWith(".jpg") || filename.toLowerCase(Locale.CHINA).endsWith(".png")) {
					return true;
				}
				return false;
			}
		};
	}

	private class ImageAdapter extends BaseAdapter {
		private Context mContext;
		private List<String> mDatas;
		private String mDirPath;
		private LayoutInflater mInflater;

		// private ImageLoader mImageLoader;
		public ImageAdapter(Context context, List<String> datas, String dirPath) {
			this.mContext = context;
			this.mDatas = datas;
			this.mDirPath = dirPath;
			mInflater = LayoutInflater.from(mContext);
			mDirPath = dirPath;
			// mImageLoader = ImageLoaderManager.getImageLoader(mContext);
		}

		@Override
		public int getCount() {
			return mDatas.size();
		}

		@Override
		public String getItem(int position) {
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.grid_item, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView.findViewById(R.id.iv_item);
				holder.imageCheck = (ImageView) convertView.findViewById(R.id.ib_check);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.imageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mSelectedImags.contains(new File(mDirPath, getItem(position)).getAbsolutePath())) {
						mSelectedImags.remove(new File(mDirPath, getItem(position)).getAbsolutePath());
						holder.imageCheck.setImageResource(R.drawable.common_radio_off);
						holder.imageView.setColorFilter(null);
					} else {
						mSelectedImags.add(new File(mDirPath, getItem(position)).getAbsolutePath());
						holder.imageCheck.setImageResource(R.drawable.common_radio_on);
						holder.imageView.setColorFilter(Color.parseColor("#77000000"));
					}
					btn_send.setText("查看(" + mSelectedImags.size() + ")");
				}
			});
			if (mSelectedImags.contains(new File(mDirPath, getItem(position)).getAbsolutePath())) {
				holder.imageCheck.setImageResource(R.drawable.common_radio_on);
				holder.imageView.setColorFilter(Color.parseColor("#77000000"));
			} else {
				holder.imageView.setColorFilter(null);
				holder.imageCheck.setImageResource(R.drawable.common_radio_off);
			}

			// mImageLoader.displayImage("file://"+new
			// File(mDirPath,getItem(position)).getAbsolutePath(),
			// holder.imageView);
			yangeImageLoader.loadImage(new File(mDirPath, getItem(position)).getAbsolutePath(), holder.imageView);
			return convertView;
		}

		private class ViewHolder {
			ImageView imageView;
			ImageView imageCheck;
		}

	}

}
