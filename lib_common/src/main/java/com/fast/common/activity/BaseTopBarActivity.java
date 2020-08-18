package com.fast.common.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.fast.common.R;
import com.fast.common.mvp.BasePresenter;
import com.fast.common.widget.TopBar;

/**
 * 所有带actionBar的Activity基类
 */

public abstract class BaseTopBarActivity<T extends BasePresenter> extends BaseMvpActivity<T> {

    protected TopBar mTopBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTopBar = findViewById(R.id.top_bar);

        if (isLeftBackListener()) {
            mTopBar.setLeftBtnFirstIcon(R.drawable.btn_top_back_white);
            initTopBarListener();
        }

    }

    protected void setTitleText(String title) {
        if (mTopBar != null) {
            mTopBar.setTitleText(title);
        }
    }

    protected void setTitleText(int title) {
        if (mTopBar != null) {
            mTopBar.setTitleText(getString(title));
        }
    }

    @Override
    protected boolean isTopBar() {
        return true;
    }

    /**
     * 是否需要返回按钮和监听
     *
     * @return
     */
    protected boolean isLeftBackListener() {
        return true;
    }


    private void initTopBarListener() {

        mTopBar.setTopBarBtnPressListener(new TopBar.TopBarBtnPressListener() {
            @Override
            public void onTopBarBtnPressed(int buttonIndex) {
                if (buttonIndex == TopBar.LEFT_BTN_FIRST) {
                    finish();
                }
            }
        });

    }

}
