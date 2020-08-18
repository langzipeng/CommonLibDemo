package com.fast.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.fast.common.R;
import com.fast.common.mvp.BasePresenter;
import com.fast.common.mvp.IView;
import com.fast.common.utils.EventBusUtils;
import com.fast.common.widget.TopBar;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @desc fragment 基类
 */
public abstract class BaseFragment<T extends BasePresenter> extends RxFragment implements IView{

    /**
     * 将代理类通用行为抽出来
     */
    protected T mPresenter;

    private Unbinder unBinder;

    protected BaseActivity mActivity;
    /**
     * 缓存Fragment view
     */
    protected View mRootView;

    protected TopBar mTopBar;

    protected SmartRefreshLayout mRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {

            initBaseContentView(inflater, container);
            initBaseView();
            if (isTopBar()) {
                mTopBar.setVisibility(View.VISIBLE);
            }
            mRefreshLayout.setEnableRefresh(isRefresh());
            mRefreshLayout.setEnableLoadMore(isLoadMore());
            initBaseListener();

            unBinder = ButterKnife.bind(this, mRootView);
            if (useEventBus()) {
                EventBusUtils.register(this);
            }
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        initView();
        initListener();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) this.getActivity();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh();
            mRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (unBinder != null) {
            unBinder.unbind();
        }
        if (useEventBus()) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBusUtils.unregister(this);
            }
        }
        //initLeakCanary();
    }


    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    @Override
    public void showLoading() {
        mActivity.showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        mActivity.hideLoadingDialog();
    }


    /**
     * 是否需要ActionBar
     */
    protected boolean isTopBar() {
        return true;
    }


    /**
     * 是否需要刷新
     */
    protected boolean isRefresh() {
        return false;
    }

    /**
     * 是否需要下拉加载
     */
    protected boolean isLoadMore() {
        return false;
    }


    /**
     * 刷新
     */
    protected void refresh() {

    }

    /**
     * 下拉加载
     */
    protected void loadMore() {

    }

    /**
     * 返回一个用于显示界面的布局id
     */
    protected abstract @LayoutRes
    int getLayoutId();

    protected abstract T createPresenter();

    /**
     * 初始化View的代码写在这个方法中
     */
    protected abstract void initView();

    /**
     * 初始化监听器的代码写在这个方法中
     */
    protected abstract void initListener();

    /**
     * 初始数据的代码写在这个方法中，用于从服务器获取数据
     */
    protected abstract void initData();

    protected abstract boolean useEventBus();


    //////////////////////////////////Base界面相关/////////////////////////////////////////

    private void initBaseContentView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        getLayoutInflater().inflate(getLayoutId(), (ViewGroup) view.findViewById(R.id.fl_content), true);
        mRootView = view;
    }


    private void initBaseView() {

        mTopBar = mRootView.findViewById(R.id.top_bar);
        mRefreshLayout = mRootView.findViewById(R.id.refreshLayout);
    }

    private void initBaseListener() {

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refresh();
            }
        });

        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadMore();
            }
        });
    }


    ///////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getBaseActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getBaseActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }


}
