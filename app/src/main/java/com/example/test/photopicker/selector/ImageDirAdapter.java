package com.example.test.photopicker.selector;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;

public class ImageDirAdapter extends BaseAdapter {
	private Context mContext;
	private List<FolderBean> mBeans;
	private LayoutInflater mInflater;
//	private ImageLoader mImageLoader;
	private YangeImageLoader yangeImageLoader = null;//YangeImageLoader.getInstance();
	public ImageDirAdapter(Context context,List<FolderBean> beans){
		this.mContext = context;
		this.mBeans = beans;
		this.mInflater = LayoutInflater.from(mContext);
//		mImageLoader = ImageLoaderManager.getImageLoader(mContext);
		yangeImageLoader = YangeImageLoader.getInstance();
	}
	
	

	@Override
	public int getCount() {
		return mBeans.size();
	}

	@Override
	public FolderBean getItem(int position) {
		return mBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView==null) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.iv_first = (ImageView) convertView.findViewById(R.id.iv_first);
			holder.tv_dir = (TextView) convertView.findViewById(R.id.tv_dir);
			holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		FolderBean folderBean = getItem(position);
		holder.tv_count.setText(folderBean.getCount()+"张");
		holder.tv_dir.setText(folderBean.getDir());
//		mImageLoader.displayImage("file://"+folderBean.getFirstImgPath(), holder.iv_first);
		yangeImageLoader.loadImage(folderBean.getFirstImgPath(), holder.iv_first);
		
		return convertView;
	}
	static class ViewHolder{
		ImageView  iv_first;
		TextView tv_dir;
		TextView tv_count;
	}

}
