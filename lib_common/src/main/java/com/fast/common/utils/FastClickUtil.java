package com.fast.common.utils;

import androidx.annotation.IntRange;

public class FastClickUtil {

    private FastClickUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        return isFastClick(MIN_CLICK_DELAY_TIME);
    }

    public static boolean isFastClick(@IntRange(from = 0) long internalTime) {
        boolean flag = true;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= internalTime) {
            flag = false;
        }
        lastClickTime = curClickTime;
        return flag;
    }
}
