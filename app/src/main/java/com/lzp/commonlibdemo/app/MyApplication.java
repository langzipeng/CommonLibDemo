package com.lzp.commonlibdemo.app;

import android.content.res.Configuration;

import com.fast.common.app.BaseApplication;


/**
 * MyApplication
 */
public class MyApplication extends BaseApplication {


    /**
     * 程序创建的时候执行
     */
    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 低内存的时候执行
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * 程序在内存清理的时候执行
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    /**
     * 配置改变时触发这个方法
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}
