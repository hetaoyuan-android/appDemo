package com.example.test.widget;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.test.R;


import java.util.List;

public class ListDialogAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int[] mColor;

    public ListDialogAdapter(@Nullable List<String> data) {
        super(R.layout.item_dialog_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_lab, item);
        if (mColor != null && mColor.length != 0) {
            int id = mColor[helper.getAdapterPosition()];
            if (id != 0) {
                helper.setTextColor(R.id.tv_lab, mContext.getResources().getColor(id));
            }
        }
    }

    public void setColor(int[] color) {
        mColor = color;

    }
}
