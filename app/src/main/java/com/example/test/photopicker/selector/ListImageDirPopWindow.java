package com.example.test.photopicker.selector;

import java.util.List;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.test.R;

public class ListImageDirPopWindow extends PopupWindow {
	private int mHeight;
	private int mWidth;
	private View mConvertView;
	private ListView mListView;
	private List<FolderBean> mFolderBeans;
	private Context mContext;
	private OnDirSelectedListener mOnDirSelectedListener;
	public interface OnDirSelectedListener {
		void onSelected(FolderBean folderBean);
	}
	
	public void setOnDirSelectedListener(OnDirSelectedListener listener){
		this.mOnDirSelectedListener = listener;
	}

	public ListImageDirPopWindow(Context context, List<FolderBean> folderBeans) {
		this.mFolderBeans = folderBeans;
		this.mContext = context;
		calculWidthAndHeight(context);

		setFocusable(true);
		setTouchable(true);
		setOutsideTouchable(true);
		setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					dismiss();
					return true;
				}
				return false;
			}
		});
		setBackgroundDrawable(mContext.getWallpaper());
		initView();
		initEvent();

	}

	private void initEvent() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(mOnDirSelectedListener!=null){
					FolderBean folderBean = (FolderBean) mListView.getAdapter().getItem(position);
					mOnDirSelectedListener.onSelected(folderBean);
				}
				dismiss();
			}

		});
	}

	private void initView() {
		mConvertView = View.inflate(mContext, R.layout.pop_main, null);
		mListView = (ListView) mConvertView.findViewById(R.id.lv);
		mListView.setAdapter(new ImageDirAdapter(mContext, mFolderBeans));
		setContentView(mConvertView);
	}

	/**
	 * 计算popup的宽和高
	 * 
	 * @param context
	 */
	private void calculWidthAndHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mWidth = outMetrics.widthPixels;
		mHeight = (int) (outMetrics.heightPixels * 0.7);
		setWidth(mWidth);
		setHeight(mHeight);
	}

	public int getmHeight() {
		return mHeight;
	}

	public void setmHeight(int mHeight) {
		this.mHeight = mHeight;
	}

	public int getmWidth() {
		return mWidth;
	}

	public void setmWidth(int mWidth) {
		this.mWidth = mWidth;
	}

	public ListView getmListView() {
		return mListView;
	}

	public void setmListView(ListView mListView) {
		this.mListView = mListView;
	}

	public List<FolderBean> getmFolderBeans() {
		return mFolderBeans;
	}

	public void setmFolderBeans(List<FolderBean> mFolderBeans) {
		this.mFolderBeans = mFolderBeans;
	}

}
