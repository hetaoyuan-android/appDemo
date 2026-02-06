package com.example.test.skin;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.BaseActivity;
import com.example.test.R;
import com.example.test.skin.model.AppearanceModel;

import java.util.ArrayList;
import java.util.List;

public class SkinActivity extends BaseActivity {

    AppearanceAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_skin;
    }

    @Override
    public void initView() {
        List<AppearanceModel> list = new ArrayList<>();
        AppearanceModel normal = new AppearanceModel();
        normal.bg_color = getColor(R.color.skin_bg1);
        normal.text_color = getColor(R.color.skin_text1);
        normal.theme = ThemeConstant.NORMAL;

        AppearanceModel dark = new AppearanceModel();
        dark.bg_color = getColor(R.color.skin_bg1_dark);
        dark.text_color = getColor(R.color.skin_text1_dark);
        dark.theme = ThemeConstant.DARK;

        list.add(normal);
        list.add(dark);
        adapter = new AppearanceAdapter(this, list, null);
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }
}