package com.example.test.skin;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * 作者: yuanhetao 
 * 时间: 2025/7/3 
 * 描述: 
 */
public class SkinItem {

    private View view;

    private List<SkinAttr> attrs;


    public SkinItem(View view, List<SkinAttr> attrs) {
        this.view = view;
        this.attrs = attrs;
    }

    public void apply() {
        if (view == null || attrs == null)
            return;
        for (SkinAttr attr : attrs) {
            String attrName = attr.attrName;
            String attrType = attr.attrType;
            String resName = attr.resName;
            int resId = attr.resId;
            if ("background".equals(attrName)) {
                if ("color".equals(attrType)) {
                    view.setBackgroundColor(SkinManager.getInstance().getColor(resName, resId));
                } else if ("drawable".equals(attrType)) {
                    view.setBackground(SkinManager.getInstance().getDrawable(resName, resId));
                }
            } else if ("textColor".equals(attrName)) {
                if (view instanceof TextView && "color".equals(attrType)) {
                    ((TextView) view).setTextColor(SkinManager.getInstance().getColor(resName, resId));
                }
            } else if ("src".equals(attrName)) {
                if ("drawable".equals(attrType) && view instanceof ImageView) {
                    Drawable drawable = SkinManager.getInstance().getDrawable(resName, resId);
                    if (drawable == null) return;
                    ((ImageView) view).setImageDrawable(drawable);
                }
            } else if ("textColorHint".equals(attrName)) {
                if (view instanceof TextView && "color".equals(attrType)) {
                    ((TextView) view).setHintTextColor(SkinManager.getInstance().getColor(resName, resId));
                }
            }
        }
    }

}

