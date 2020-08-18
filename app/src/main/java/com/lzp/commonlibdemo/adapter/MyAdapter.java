package com.lzp.commonlibdemo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzp.commonlibdemo.R;
import com.lzp.commonlibdemo.entity.Model;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

public class MyAdapter extends BaseQuickAdapter<Model, BaseViewHolder> {

    public MyAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    public MyAdapter(@LayoutRes int layoutResId, @Nullable List<Model> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Model item) {

        viewHolder.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_content, item.getContent())
                .setImageResource(R.id.iv_img, R.mipmap.ic_launcher)
                .addOnClickListener(R.id.tv_title)
                .addOnClickListener(R.id.iv_img);
        //获取当前条目position
        // int position = viewHolder.getLayoutPosition();

    }

}