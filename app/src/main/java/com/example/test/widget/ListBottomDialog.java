package com.example.test.widget;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.test.R;

import java.util.List;

/**
 * 底部弹出item选择dialog
 *
 * @author : zhupeizhen
 * 2021/1/13
 */
public class ListBottomDialog {


    private int[] mColor;
    private AlertDialog mAlertDialog;
    Context context;
    List<String> list;
    BaseQuickAdapter.OnItemClickListener listener;

    private BaseQuickAdapter mAdapter;
    private boolean isAddFooter = true;
    private String mCancelText;
    private String mTitleText;

    public ListBottomDialog(Context context, List<String> list, BaseQuickAdapter.OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public ListBottomDialog(Context context, List<String> list, String mTitleText, BaseQuickAdapter.OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.mTitleText = mTitleText;
    }

    /**
     * 底部弹出item选择dialog
     * @param context
     * @param list item显示内容
     * @param color 设置各个item字体的颜色，0为不设置
     * @param listener
     */
    public ListBottomDialog(Context context, List<String> list, int[] color, BaseQuickAdapter.OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.mColor = color;
    }

    public void setListener(BaseQuickAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void showListDialog() {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(context)
                    .setContentView(R.layout.dialo_list)
                    .setBottom()
                    .fullScreen()
                    .setCancleable(true)
                    .setAnimations(R.style.CommentDialogAnim)
                    .setDimEnable(true)
                    .show();
            RecyclerView recyclerView = mAlertDialog.getView(R.id.rcv_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if (mAdapter == null) {
                mAdapter = new ListDialogAdapter(list);
            }
            if (mColor != null && mColor.length == list.size() && mAdapter instanceof ListDialogAdapter) {
                ((ListDialogAdapter) mAdapter).setColor(mColor);
            }
            if (isAddFooter) {
                View inflate = View.inflate(context, R.layout.item_dialog_cancel, null);
                if (!TextUtils.isEmpty(mCancelText)){
                    ((TextView)inflate.findViewById(R.id.btv_cancel)).setText(mCancelText);
                }
                inflate.setOnClickListener(view -> {
                    mAlertDialog.dismiss();
                });
                mAdapter.setFooterView(inflate);
            }

            if (!TextUtils.isEmpty(mTitleText)){
                View inflate = View.inflate(context, R.layout.item_dialog_title, null);
                ((TextView)inflate.findViewById(R.id.btv_title)).setText(mTitleText);
                inflate.setOnClickListener(view -> {
                    mAlertDialog.dismiss();
                });
                mAdapter.setHeaderView(inflate);
            }
            if (listener != null) {
                mAdapter.setOnItemClickListener(listener);
            }
            recyclerView.setAdapter(mAdapter);

        } else {
            if (!TextUtils.isEmpty(mTitleText)){
                View inflate = View.inflate(context, R.layout.item_dialog_title, null);
                ((TextView)inflate.findViewById(R.id.btv_title)).setText(mTitleText);
                inflate.setOnClickListener(view -> {
                    mAlertDialog.dismiss();
                });
                mAdapter.setHeaderView(inflate);
            }
            mAlertDialog.show();
        }
    }

    public void dismiss() {

        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }

    }

    public void setTitle(String title){
        this.mTitleText = title;
    }

    public void setAdapter(BaseQuickAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void setAddFooter(boolean addFooter) {
        isAddFooter = addFooter;
    }
    public void setCancelText(String cancelText){
        this.mCancelText = cancelText;
    }


    public void setNewData(List<String> list) {
        this.list.clear();
        this.list.addAll(list);
        mAdapter.notifyDataSetChanged();
    }
}
