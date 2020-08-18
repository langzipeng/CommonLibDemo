package com.fast.common.webview;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.LogUtils;
import com.fast.common.activity.BaseTopBarActivity;
import com.fast.common.widget.X5WebView;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import com.fast.common.R;
import com.fast.common.mvp.BasePresenter;
import com.fast.common.widget.TopBar;

/**
 * WebViewActivity
 */

public class WebViewActivity extends BaseTopBarActivity {

    private ViewGroup mViewParent;

    private X5WebView mWebView;

    public static void actionStart(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void getIntent(Intent intent) {

    }

    @Override
    protected void initView() {

        mViewParent = findViewById(R.id.webView);

        mWebView = new X5WebView(this, null);

        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        mWebView.setWebViewClient(client);
        mWebView.loadUrl(getIntent().getStringExtra("url"));
        mWebView.getX5WebViewExtension();

        if (mWebView.getX5WebViewExtension() != null) {
            LogUtils.e("TBS-X5", "已加载");
        } else {
            LogUtils.e("TBS-X5", "未加载");
        }

        mTopBar.setLeftBtnFirstIcon(R.drawable.btn_top_back_white);
        mTopBar.setRightBtnFirstIcon(R.drawable.btn_web_refresh);
    }

    /**
     * 初始化监听器的代码写在这个方法中
     */
    @Override
    protected void initListener() {
        mTopBar.setTopBarBtnPressListener(new TopBar.TopBarBtnPressListener() {
            @Override
            public void onTopBarBtnPressed(int buttonIndex) {//返回
                if (buttonIndex == TopBar.LEFT_BTN_FIRST) {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                } else if (buttonIndex == TopBar.RIGHT_BTN_FIRST) {//刷新
                    mWebView.reload();
                }
            }
        });
    }

    /**
     * 初始数据的代码写在这个方法中，用于从服务器获取数据
     */
    @Override
    protected void initData2NET() {

    }

    @Override
    protected boolean isLeftBackListener() {
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishJudge();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    private void finishJudge() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }


    private WebViewClient client = new WebViewClient() {
        /**
         * 防止加载网页时调起系统浏览器
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
            sslErrorHandler.proceed();// 接受所有网站的证书
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            setTitleText(view.getTitle());
            setTitleText(getIntent().getStringExtra("title"));
        }
    };

}
