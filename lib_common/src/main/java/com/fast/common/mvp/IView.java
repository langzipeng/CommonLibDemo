package com.fast.common.mvp;

/**
 * IView
 */
public interface IView {


    //显示loading
    void showLoading();

    //隐藏loading
    void hideLoading();

    //显示吐司
    void showError(String msg);

    //显示网络加载失败
    void showError2Net(String msg);

}
