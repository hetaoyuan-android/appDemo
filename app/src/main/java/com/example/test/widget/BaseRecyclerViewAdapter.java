package com.example.test.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.test.R;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements View.OnClickListener {

    // 上下文对象
    protected Context mContext;
    // 数据列表
    protected List<T> mList = new ArrayList<>();;
    // 布局id
    private int mLayoutId;

    // 空数据时，显示空布局类型
    private final int TYPE_EMPTY = 1;
    // 正常数据
    private final int TYPE_NORMAL = 0;

    // item点击事件（这是自定义的）
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;

    /**
     * 构造函数，初始化适配器，传入上下文对象。
     *
     * @param context 上下文对象
     */
    public BaseRecyclerViewAdapter(Context context, int layoutId) {
        mContext = context;
        mLayoutId = layoutId;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        //若为空布局类型，则直接返回空的RecyclerView.ViewHolder
        if (viewType == TYPE_EMPTY) {
            view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item_empty, parent, false);
            return (VH) new EmptyViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
            // 设置点击事件
            view.setOnClickListener(this);
        }
        return (VH) new BaseRecycleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.itemView.setTag(position);
        if (mList != null && mList.size() > 0) {
            T bean = mList.get(position);
            onBindData(holder, bean, position);
        }
    }

    public abstract void onBindData(VH holder, T bean, int position);

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener!=null) {
            mOnItemClickListener.onItemClickListener(v, (Integer) v.getTag());
        }
    }

    /**
     * 获取指定位置项的视图类型。
     *
     * @param position 项的位置
     * @return 视图类型
     */
    @Override
    public int getItemViewType(int position) {
        if (mList == null || mList.size() <= 0) {
            return TYPE_EMPTY;
        }
        return TYPE_NORMAL;
    }

    /**
     * 获取适配器中的项数。
     *
     * @return 项数
     */
    @Override
    public int getItemCount() {
        if (mList == null) {
            return TYPE_EMPTY;
        }
        return mList.isEmpty() ? TYPE_EMPTY : mList.size();
    }

    /**
     * 设置列表项点击事件监听器。
     */
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    /**
     * 设置子项内部控件点击事件监听器。
     */
    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemClickListener) {
        this.onItemLongClickListener = mOnItemClickListener;
    }

    /**
     * 获取适配器中的数据列表。
     *
     * @return 数据列表
     */
    public List<T> getData() {
        return mList;
    }

    /**
     * 设置适配器的数据列表，并通知适配器数据发生变化。
     *
     * @param list 新的数据列表
     */
    public void setData(List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }

    /**
     * 同步地向适配器中添加数据列表，并通知适配器数据发生变化。
     * @param list 要添加的数据列表
     */
    public synchronized void addData(List<T> list) {
        if (mList == null)
            mList = new ArrayList();
        if (list != null) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    /**
     * 同步地向适配器中添加单个数据项，并通知适配器数据发生变化。
     *
     * @param object 要添加的数据项
     */
    public synchronized void addData(T object) {
        if (mList == null)
            mList = new ArrayList();
        if (object != null) {
            mList.add(object);
            notifyDataSetChanged();
        }
    }

    /**
     * 清空适配器中的数据列表，并通知适配器数据发生变化。
     */
    public void clearData() {
        if (mList != null) {
            mList.clear();
        }
        notifyDataSetChanged();
    }


    /**
     * ViewHolder 用于空布局
     */
    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        // 在这里添加空布局的控件，例如一个 TextView
        private TextView emptyTextView;

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
            // 初始化空布局的控件
            emptyTextView = itemView.findViewById(R.id.tv_empty);
        }
    }


    public interface OnItemClickListener {
        void onItemClickListener(View v, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClickListener(View v, int position);
    }

}
