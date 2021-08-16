package com.example.test.tree.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.AbsListView.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.tree.entity.ChildEntity;
import com.example.test.tree.entity.ParentEntity;

/**
 *  父类分组的适配器
 */
public class ParentAdapter extends BaseExpandableListAdapter {

	private Context mContext;// 上下文

	private ArrayList<ParentEntity> mParents;// 数据源

	private OnChildTreeViewClickListener mTreeViewClickListener;// 点击子ExpandableListView子项的监听

	public ParentAdapter(Context context, ArrayList<ParentEntity> parents) {
		this.mContext = context;
		this.mParents = parents;
	}

	@Override
	public ChildEntity getChild(int groupPosition, int childPosition) {
		return mParents.get(groupPosition).getChilds().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mParents.get(groupPosition).getChilds() != null ? mParents
				.get(groupPosition).getChilds().size() : 0;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isExpanded, View convertView, ViewGroup parent) {

		final ExpandableListView eListView = getExpandableListView();

		ArrayList<ChildEntity> childs = new ArrayList<ChildEntity>();

		final ChildEntity child = getChild(groupPosition, childPosition);

		childs.add(child);

		final ChildAdapter childAdapter = new ChildAdapter(this.mContext,
				childs);

		eListView.setAdapter(childAdapter);

		/**
		 *
		 *  点击子ExpandableListView子项时，调用回调接口
		 * */
		eListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int groupIndex, int childIndex, long arg4) {
				Log.e("----", "-----");
				if (mTreeViewClickListener != null) {

					mTreeViewClickListener.onClickPosition(groupPosition,
							childPosition, childIndex);
				}
				return false;
			}
		});

		
		/**
		 *   子ExpandableListView展开时，因为group只有一项，所以子ExpandableListView的总高度=
		 *   （子ExpandableListView的child数量 + 1 ）* 每一项的高度
		 * */
		eListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				LayoutParams lp = new LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, (child
								.getChildNames().size() + 1)
								* (int) mContext.getResources().getDimension(
										R.dimen.parent_expandable_list_height));
				eListView.setLayoutParams(lp);
			}
		});

		/**
		 * 子ExpandableListView关闭时，此时只剩下group这一项，所以子ExpandableListView的总高度即为一项的高度
		 * */
		eListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {
			@Override
			public void onGroupCollapse(int groupPosition) {

				LayoutParams lp = new LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, (int) mContext
								.getResources().getDimension(
										R.dimen.parent_expandable_list_height));
				eListView.setLayoutParams(lp);
			}
		});

		//      设置分组单击监听事件
		eListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				boolean groupExpanded = parent.isGroupExpanded(groupPosition);
				if (groupExpanded) {
					parent.collapseGroup(groupPosition);
				} else {
					parent.expandGroup(groupPosition, true);
				}
				setIndicatorState(groupPosition, groupExpanded);
				return true;
			}
		});
		return eListView;

	}

	/**
	 *  动态创建子ExpandableListView
	 * */
	public ExpandableListView getExpandableListView() {
		ExpandableListView mExpandableListView = new ExpandableListView(
				mContext);
		LayoutParams lp = new LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, (int) mContext
						.getResources().getDimension(
								R.dimen.parent_expandable_list_height));
		mExpandableListView.setLayoutParams(lp);
		mExpandableListView.setDividerHeight(0);// 取消group项的分割线
		mExpandableListView.setChildDivider(null);// 取消child项的分割线
		mExpandableListView.setGroupIndicator(null);// 取消展开折叠的指示图标
		return mExpandableListView;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mParents.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return mParents != null ? mParents.size() : 0;
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
					R.layout.parent_group_item, null);
			holder = new GroupHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (GroupHolder) convertView.getTag();
		}
		holder.update(mParents.get(groupPosition));
		//      把位置和图标添加到Map
		mIndicators.put(groupPosition, holder.ivIndicator);
		//      根据分组状态设置Indicator
		setIndicatorState(groupPosition, isExpanded);
		return convertView;
	}

	/**
	 *  Holder优化
	 */
	class GroupHolder {

		private TextView parentGroupTV;
		private ImageView ivIndicator;

		public GroupHolder(View v) {
			parentGroupTV = v.findViewById(R.id.parentGroupTV);
			ivIndicator = v.findViewById(R.id.item_indicator);
		}

		public void update(ParentEntity model) {
			parentGroupTV.setText(model.getGroupName());
			parentGroupTV.setTextColor(model.getGroupColor());
		}
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	/**
	 *  设置点击子ExpandableListView子项的监听
	 * */
	public void setOnChildTreeViewClickListener(
			OnChildTreeViewClickListener treeViewClickListener) {
		this.mTreeViewClickListener = treeViewClickListener;
	}

	/**
	 *
	 *  点击子ExpandableListView子项的回调接口
	 * */
	public interface OnChildTreeViewClickListener {

		void onClickPosition(int parentPosition, int groupPosition,
                             int childPosition);
	}

	//                用于存放Indicator的集合
	private SparseArray<ImageView> mIndicators = new SparseArray<>();
	//            根据分组的展开闭合状态设置指示器
	public void setIndicatorState(int groupPosition, boolean isExpanded) {
		if (isExpanded) {
			mIndicators.get(groupPosition).setImageResource(R.drawable.expanded_icon);
		} else {
			mIndicators.get(groupPosition).setImageResource(R.drawable.expand_contact_icon);
		}
	}

}
