package com.fast.common.net.interceptor;

import android.text.TextUtils;
import android.util.Log;


import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * 日志打印拦截器
 */
public class LoggerInterceptor implements Interceptor {

    public static final String TAG = "Interceptor";
    private String tag;
    private boolean showResponse;

    public LoggerInterceptor(String tag, boolean showResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        this.showResponse = showResponse;
        this.tag = tag;
    }

    public LoggerInterceptor(String tag) {
        this(tag, false);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        //———请求之前————
        logForRequest(request);
        Response response = chain.proceed(request);
        //———请求之后————
        return logForResponse(response);
    }

    private Response logForResponse(Response response) {
        try {
            //===>response log
            Log.e(TAG, "========response'log=======");
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            Log.e(TAG, "code : " + clone.code());
            if (!TextUtils.isEmpty(clone.message()))
                Log.e(TAG, "message : " + clone.message());

            if (showResponse) {
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    if (mediaType != null) {
                        Log.e(TAG, "responseBody's contentType : " + mediaType.toString());
                        if (isText(mediaType)) {
                            String resp = body.string();
                            LogUtils.e("responseBody's content : " + unicodeToUTF_8(resp));

                            body = ResponseBody.create(mediaType, resp);
                            Log.e(TAG, "========response'log=======end");
                            return response.newBuilder().body(body).build();
                        } else {
                            Log.e(TAG, "responseBody's content : maybe [file part] , too large too print , ignored!");
                        }
                    }
                }
            }

            Log.e(TAG, "========response'log=======end");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();

            Log.e(TAG, "________request'log________");
            Log.e(TAG, "method : " + request.method());
            Log.e(TAG, "url : " + url);
            if (headers != null && headers.size() > 0) {
                Log.e(TAG, "headers : " + headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    Log.e(TAG, "requestBody's contentType : " + mediaType.toString());
                    if (isText(mediaType)) {
                        LogUtils.e("requestBody's content : " + bodyToString(request));
                    } else {
                        Log.e(TAG, "requestBody's content : maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            Log.e(TAG, "________request'log________end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            return mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml") ||
                    mediaType.subtype().equals("x-www-form-urlencoded");
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }


    /**
     * 解决中文乱码问题
     *
     * @param src
     * @return
     */
    public static String unicodeToUTF_8(String src) {
        if (null == src) {
            return null;
        }
        System.out.println("src: " + src);
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < src.length(); ) {
            char c = src.charAt(i);
            if (i + 6 < src.length() && c == '\\' && src.charAt(i + 1) == 'u') {
                String hex = src.substring(i + 2, i + 6);
                try {
                    out.append((char) Integer.parseInt(hex, 16));
                } catch (NumberFormatException nfe) {
                    nfe.fillInStackTrace();
                }
                i = i + 6;
            } else {
                out.append(src.charAt(i));
                ++i;
            }
        }
        return out.toString();

    }


}
