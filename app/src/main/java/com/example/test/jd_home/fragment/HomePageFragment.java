package com.example.test.jd_home.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.jd_home.adapter.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends BaseFragment {

    RecyclerView recyclerView;

    public static HomePageFragment getInstance() {
        HomePageFragment fragment = new HomePageFragment();
        return fragment;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home_page_home;
    }

    @Override
    public void initView() {
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
