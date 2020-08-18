package com.lzp.commonlibdemo.empty;

import android.content.Intent;
import android.os.Bundle;

import com.lzp.commonlibdemo.R;
import com.fast.common.activity.BaseMvpActivity;
import com.fast.common.widget.TopBar;

import androidx.annotation.Nullable;
import butterknife.BindView;


/**
 * @author Langzipeng
 * @date 2019\9\25
 * @desc
 */
public class Activity extends BaseMvpActivity<Presenter> implements Contract.View {

    //////////////////////////////////控件ButterKnife初始化/////////////////////////////////////////

    //TopBar
    @BindView(R.id.top_bar)
    TopBar mTopBar;

    //////////////////////////////////////////////成员变量//////////////////////////////////////////


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected Presenter createPresenter() {
        return new Presenter();
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void getIntent(Intent intent) {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

        //TopBar监听
        mTopBar.setTopBarBtnPressListener(new TopBar.TopBarBtnPressListener() {
            @Override
            public void onTopBarBtnPressed(int buttonIndex) {
                if (buttonIndex == TopBar.LEFT_BTN_FIRST) {
                    finish();
                }

            }
        });


    }

    @Override
    protected void initData2NET() {

    }


}
