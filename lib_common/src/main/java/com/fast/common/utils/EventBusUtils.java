package com.fast.common.utils;


import com.fast.common.bean.Event;

import org.greenrobot.eventbus.EventBus;

/**
 * Describe：EventBusUtils
 * Created by wtq on 2018/10/22.
 */

public class EventBusUtils {

    /**
     * 注册事件
     */
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    /**
     * 解除事件
     */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /**
     * 发送普通事件
     */
    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 发送粘性事件
     */
    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);
    }



    //...
}
