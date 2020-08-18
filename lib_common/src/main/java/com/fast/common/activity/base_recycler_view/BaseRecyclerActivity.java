package com.fast.common.activity.base_recycler_view;


import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.fast.common.R;
import com.fast.common.activity.BaseTopBarActivity;
import com.fast.common.mvp.BasePresenter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * BaseRecyclerActivity
 */
public abstract class BaseRecyclerActivity<T extends BasePresenter,
        K extends BaseQuickAdapter> extends BaseTopBarActivity<T> {

    protected RecyclerView mRecyclerView;

    protected K mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRecyclerView();
    }


    @Override
    protected final int getLayoutId() {
        return R.layout.include_base_recycler;
    }


    /**
     * 是否需要刷新
     */
    @Override
    protected boolean isRefresh() {
        return true;
    }

    /**
     * 是否需要下拉加载
     */
    @Override
    protected boolean isLoadMore() {
        return true;
    }


    protected RecyclerView.LayoutManager getLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }


    protected abstract K getAdapter();


    protected void onItemClickListener(BaseQuickAdapter adapter, View view, int position) {

    }

    protected void onItemChildClickListener(BaseQuickAdapter adapter, View view, int position) {

    }


    protected void initRecyclerView() {

        mRecyclerView = findViewById(R.id.base_recyclerview);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);
        //开启动画（默认为渐显效果）
        mAdapter.openLoadAnimation();

        mRecyclerView.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {//Item点击
                onItemClickListener(adapter, view, position);
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {//子类点击
                onItemChildClickListener(adapter, view, position);

            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });

    }

}
