package com.fast.common.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fast.common.utils.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.fast.common.R;
import com.fast.common.receiver.BaseSignOutReceiver;
import com.fast.common.utils.EventBusUtils;
import com.fast.common.widget.LoadingDialog;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * 基类 BaseMvpActivity
 */
public abstract class BaseActivity extends RxAppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private Unbinder mUnBinder;
    private ViewStub mEmptyView;
    protected Context mContext;
    //加载弹窗
    private LoadingDialog mLoadingDialog = null;
    //强制登出广播
    private BaseSignOutReceiver receive;
    //刷新
    protected SmartRefreshLayout mRefreshLayout;
    private FrameLayout mFlContent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mContext = this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initBaseContentView();

        initBaseView();

        if (isTopBar()) {
            ViewStub viewStub = findViewById(R.id.vs_top_bar);
            viewStub.setVisibility(View.VISIBLE);
        }
        mRefreshLayout.setEnableRefresh(isRefresh());
        mRefreshLayout.setEnableLoadMore(isLoadMore());
        initBaseListener();

        Intent intent = getIntent();
        if (intent != null)
            getIntent(intent);
        mUnBinder = ButterKnife.bind(this);
        if (useEventBus()) {
            EventBusUtils.register(this);
        }

        //注册离线广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lzp.commonlib.LOG_OUT");
        receive = new BaseSignOutReceiver();
        registerReceiver(receive, intentFilter);


    }


    /**
     * 是否使用eventBus
     *
     * @return
     */
    protected boolean useEventBus() {
        return false;
    }


    /**
     * 是否需要ActionBar
     */
    protected boolean isTopBar() {
        return false;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        if (useEventBus()) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBusUtils.unregister(this);
            }
        }

        if (receive != null) {
            unregisterReceiver(receive);
            receive = null;
        }

        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh();
            mRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        initView();
        initData2NET();
        initListener();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }


    /**
     * 获取布局 Id
     *
     * @return
     */
    protected abstract @LayoutRes
    int getLayoutId();

    /**
     * 获取 Intent 数据
     **/
    protected abstract void getIntent(Intent intent);

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
    protected abstract void initData2NET();


    //////////////////////////////////////////空页面方法/////////////////////////////////////////////////

    /**
     * 数据加载
     */
    protected void showLoadView() {
        showEmptyOrErrorView("", 0);
        showLoadingDialog();
    }

    /**
     * 数据加载完成
     */
    protected void hideLoadView() {
        hideEmptyView();
        hideLoadingDialog();
    }

    /**
     * 暂无数据
     */
    protected void showEmptyView() {
        showEmptyView(getString(R.string.no_data));
    }

    protected void showEmptyView(String text) {
        showEmptyOrErrorView(text, R.drawable.ic_no_data);
    }

    /**
     * 网络加载失败
     */
    protected void showErrorView2Net() {
        showErrorView2Net(getString(R.string.error_data_net));
    }

    protected void showErrorView2Net(String text) {
        showErrorView2Net(text, R.drawable.ic_no_net_icon);
    }


    protected void showErrorView2Net(String text, @DrawableRes int img) {
        initErrorView(text, img);
        findViewById(R.id.ll_empty).setOnClickListener(view -> {
            initData2NET();
            onPageClick();
        });
    }


    /**
     * 显示空界面
     *
     * @param text
     * @param img
     */
    protected void showEmptyOrErrorView(String text, @DrawableRes int img) {
        initErrorView(text, img);
        findViewById(R.id.ll_empty).setOnClickListener(view -> onPageClick());
    }

    /**
     * 隐藏空界面
     */
    protected void hideEmptyView() {
        if (mEmptyView != null) {
            mFlContent.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    /**
     * 空页面被点击
     */
    protected void onPageClick() {

    }

    private void initErrorView(String text, int img) {
        if (mEmptyView == null) {
            mEmptyView = findViewById(R.id.vs_empty);
        }
        mFlContent.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);

        ImageView iv_empty = findViewById(R.id.iv_empty);
        TextView tv_empty = findViewById(R.id.tv_empty);

        if (img == 0) {
            iv_empty.setVisibility(View.INVISIBLE);
        } else {
            iv_empty.setBackgroundResource(img);
            iv_empty.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(text)) {
            tv_empty.setVisibility(View.INVISIBLE);
        } else {
            tv_empty.setText(text);
            tv_empty.setVisibility(View.VISIBLE);
        }

    }


    //////////////////////////////////////////权限相关/////////////////////////////////////////////////

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //使用EasyPermissionHelper注入回调
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 权限判断
     *
     * @param perms
     * @return
     */
    protected boolean hasPermissions(String[] perms) {
        return EasyPermissions.hasPermissions(this, perms);
    }

    /**
     * 请求权限
     *
     * @param reason
     * @param requestCode
     * @param perms
     */
    protected void requestPermissions(String reason, int requestCode, String[] perms) {

        EasyPermissions.requestPermissions(this,
                reason,
                requestCode,
                perms);
    }


    /**
     * 权限申请成功的回调
     *
     * @param requestCode 申请权限时的请求码
     * @param perms       申请成功的权限集合
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        getPermissionsSuccess(requestCode, perms);
    }

    /**
     * 权限申请拒绝的回调
     *
     * @param requestCode 申请权限时的请求码
     * @param perms       申请拒绝的权限集合
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        getPermissionsFail(requestCode, perms);
    }


    /**
     * 获取权限成功
     *
     * @param requestCode
     * @param perms
     */
    protected void getPermissionsSuccess(int requestCode, @NonNull List<String> perms) {

    }

    /**
     * 获取权限失败
     *
     * @param requestCode
     * @param perms
     */
    protected void getPermissionsFail(int requestCode, @NonNull List<String> perms) {
        ToastUtils.showShort("权限被拒绝");
    }

    //////////////////////////////////Base界面相关/////////////////////////////////////////

    private void initBaseContentView() {
        setContentView(R.layout.activity_base);
        mFlContent = findViewById(R.id.fl_content);
        mFlContent.addView(getLayoutInflater().inflate(getLayoutId(), null));

    }


    private void initBaseView() {

        mRefreshLayout = findViewById(R.id.refreshLayout);
    }

    private void initBaseListener() {

        if (isRefresh()) {
            mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    refresh();
                }
            });
        }

        if (isLoadMore()) {
            mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    loadMore();
                }
            });
        }


    }


    //////////////////////////////////////////进度框相关/////////////////////////////////////////////////

    /**
     * 显示进度框
     */
    protected void showLoadingDialog() {
        createLoadingDialog();
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    /**
     * 创建LoadingDialog
     */
    private void createLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
            mLoadingDialog.setOnLoadingListener(dialog -> finish());
        }
    }

    /**
     * 隐藏进度框
     */
    protected void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 设置进度框显示文本
     *
     * @param text
     */
    protected void setLoadingText(String text) {
        if (mLoadingDialog != null) {
            mLoadingDialog.setLoadingText(text);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////


}
