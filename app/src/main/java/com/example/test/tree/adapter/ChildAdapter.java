package com.example.test.tree.adapter;

import java.util.ArrayList;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.tree.entity.ChildEntity;

/**
 * 子类分组的适配器
 */
public class ChildAdapter extends BaseExpandableListAdapter {

	private Context mContext;// 上下文

	private ArrayList<ChildEntity> mChilds;// 数据源

	public ChildAdapter(Context context, ArrayList<ChildEntity> childs) {
		this.mContext = context;
		this.mChilds = childs;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mChilds.get(groupPosition).getChildNames() != null ? mChilds
				.get(groupPosition).getChildNames().size() : 0;
	}

	@Override
	public String getChild(int groupPosition, int childPosition) {
		if (mChilds.get(groupPosition).getChildNames() != null
				&& mChilds.get(groupPosition).getChildNames().size() > 0)
			return mChilds.get(groupPosition).getChildNames()
					.get(childPosition).toString();
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isExpanded, View convertView, ViewGroup parent) {
		ChildHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.child_child_item, null);
			holder = new ChildHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ChildHolder) convertView.getTag();
		}
		holder.update(getChild(groupPosition, childPosition));
		return convertView;
	}

	/**
	 *  Holder优化
	 * */
	class ChildHolder {

		private TextView childChildTV;

		public ChildHolder(View v) {
			childChildTV = (TextView) v.findViewById(R.id.childChildTV);
		}

		public void update(String str) {
			childChildTV.setText(str);
			childChildTV.setTextColor(Color.parseColor("#00ffff"));
		}
	}

	@Override
	public Object getGroup(int groupPosition) {
		if (mChilds != null && mChilds.size() > 0)
			return mChilds.get(groupPosition);
		return null;
	}

	@Override
	public int getGroupCount() {
		return mChilds != null ? mChilds.size() : 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.child_group_item, null);
			holder = new GroupHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (GroupHolder) convertView.getTag();
		}
		holder.update(mChilds.get(groupPosition));
		return convertView;
	}

	/**
	 * Holder优化
	 * */
	class GroupHolder {

		private TextView childGroupTV;

		public GroupHolder(View v) {
			childGroupTV = (TextView) v.findViewById(R.id.childGroupTV);
		}

		public void update(ChildEntity model) {
			childGroupTV.setText(model.getGroupName());
			childGroupTV.setTextColor(model.getGroupColor());
		}
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		/**
		 * ==============================================
		 * 此处必须返回true，否则无法响应子项的点击事件===============
		 * ==============================================
		 **/
		return true;
	}

}
