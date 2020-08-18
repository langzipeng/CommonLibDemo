package com.lzp.commonlibdemo.empty;

import com.fast.common.mvp.BasePresenter;


/**
 *
 */
public class Presenter extends BasePresenter<Contract.Model,Contract.View> {

    @Override
    protected Contract.Model createModel() {
        return new Model();
    }



}
