package com.example.test.jd_home.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.jd_home.adapter.ListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.ArrayList;
import java.util.List;

public class OtherPageFragment extends BaseFragment {

    RecyclerView recyclerView;
    SmartRefreshLayout smartRefreshLayout;

    public static OtherPageFragment getInstance() {
        OtherPageFragment fragment = new OtherPageFragment();
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_page_other;
    }

    @Override
    public void initView() {
        smartRefreshLayout = contentView.findViewById(R.id.smartRefreshLayout);
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
        recyclerView = contentView.findViewById(R.id.recyclerView);
    }

    @Override
    public void initClick() {

    }

    @Override
    public void initData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("Item" + i);
        }
        ListAdapter adapter = new ListAdapter(mContext, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(adapter);
    }


}
