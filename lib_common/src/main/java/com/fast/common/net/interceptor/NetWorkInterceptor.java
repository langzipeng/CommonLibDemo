package com.fast.common.net.interceptor;

import com.blankj.utilcode.util.LogUtils;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: John
 * E-mail: 634930172@qq.com
 * Date: 2018/1/4 0004 17:43
 * <p/>
 * Description:网络请求拦截器
 */

public class NetWorkInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        // 拦截请求，获取到该次请求的request
        Request request = chain.request();
        LogUtils.e(">>>>>>NetWorkInterceptor:"+request.method());
        // 执行本次网络请求操作，返回response信息
        Response response = chain.proceed(request);
        return  response;
    }
}
