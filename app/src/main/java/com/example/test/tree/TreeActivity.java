package com.example.test.tree;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.tree.adapter.ParentAdapter;
import com.example.test.tree.entity.ChildEntity;
import com.example.test.tree.entity.ParentEntity;

import java.util.ArrayList;

public class TreeActivity extends AppCompatActivity implements ExpandableListView.OnGroupExpandListener,
        ParentAdapter.OnChildTreeViewClickListener {

    private Context mContext;

    private ExpandableListView eList;

    private ArrayList<ParentEntity> parents;

    private ParentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_tree);
        loadData();

        initEList();
    }

    /**
     *
     *   初始化菜单数据源
     * */
    private void loadData() {

        parents = new ArrayList<ParentEntity>();

        for (int i = 0; i < 5; i++) {

            ParentEntity parent = new ParentEntity();

            parent.setGroupName("联创高级中学" + i + "项");

            parent.setGroupColor(getResources().getColor(
                    R.color.color_141414));

            ArrayList<ChildEntity> childs = new ArrayList<ChildEntity>();

            for (int j = 0; j < 4; j++) {

                ChildEntity child = new ChildEntity();

                child.setGroupName("子类父分组第" + j + "项");

                child.setGroupColor(Color.parseColor("#000000"));

                ArrayList<String> childNames = new ArrayList<String>();

                ArrayList<Integer> childColors = new ArrayList<Integer>();

                for (int k = 0; k < 5; k++) {

                    childNames.add("子类第" + k + "项");

                    childColors.add(Color.parseColor("#ff00ff"));

                }

                child.setChildNames(childNames);

                childs.add(child);

            }

            parent.setChilds(childs);

            parents.add(parent);

        }
    }

    /**
     *
     *  初始化ExpandableListView
     * */
    private void initEList() {

        eList = (ExpandableListView) findViewById(R.id.eList);

        eList.setOnGroupExpandListener(this);

        adapter = new ParentAdapter(mContext, parents);

        eList.setAdapter(adapter);

        adapter.setOnChildTreeViewClickListener(this);

    }

    /**
     *
     *  点击子ExpandableListView的子项时，回调本方法，根据下标获取值来做相应的操作
     * */
    @Override
    public void onClickPosition(int parentPosition, int groupPosition,
                                int childPosition) {
        // do something
        String childName = parents.get(parentPosition).getChilds()
                .get(groupPosition).getChildNames().get(childPosition)
                .toString();
        Toast.makeText(
                mContext,
                "点击的下标为： parentPosition=" + parentPosition
                        + "   groupPosition=" + groupPosition
                        + "   childPosition=" + childPosition + "\n点击的是："
                        + childName, Toast.LENGTH_SHORT).show();
    }

    /**
     *
     * 展开一项，关闭其他项，保证每次只能展开一项
     * */
    @Override
    public void onGroupExpand(int groupPosition) {
        for (int i = 0; i < parents.size(); i++) {
            if (i != groupPosition) {
                eList.collapseGroup(i);
            }
        }
    }

}
