package com.fast.common.net;

import com.fast.common.constants.AppBaseConfig;
import com.fast.common.app.BaseApplication;
import com.fast.common.net.converter.HttpCovertFactory;
import com.fast.common.net.interceptor.LoggerInterceptor;
import com.fast.common.utils.MMKVUtils;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitManager {

    //private static OkHttpClient client;
    //private static volatile Retrofit retrofit;

    /**
     * 配置网络请求头
     */
    public static HashMap<String, Object> getRequestHeader() {
        HashMap<String, Object> parameters = new HashMap<>();
        // 为接口统一添加access_token参数
        parameters.put("token", MMKVUtils.getMMKV().decodeString("token"));
        return parameters;
    }

    /**
     * 配置网络请求公共参数
     */
    public static HashMap<String, Object> getRequestParams() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("_appversion", "");
        parameters.put("_systemtype", "");
        parameters.put("_systemversion", "");
        parameters.put("_deviceid", "");
        parameters.put("_memberid", "");
        return parameters;
    }

    /**
     * 内部类单列设计模式
     */
    private RetrofitManager() {
    }

    private static class RetrofitManagerInstance {
        private final static RetrofitManager RETROFIT_MANAGER = new RetrofitManager();
    }

    public static RetrofitManager getInstance() {
        return RetrofitManagerInstance.RETROFIT_MANAGER;
    }

    private Retrofit getRetrofit() {

        Retrofit retrofit;
        retrofit = createRetrofit();
        return retrofit;
    }

    private Retrofit createRetrofit() {
        //添加一个log拦截器,打印所有的log
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        //可以设置请求过滤的水平,body,basic,headers
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //设置 请求的缓存的大小跟位置
        File cacheFile = new File(BaseApplication.getContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50); //50Mb 缓存的大小

        OkHttpClient client = new OkHttpClient
                .Builder()
//                          .cookieJar(new CookieJarImpl(new PersistentCookieStore(App.getContext()))); //cookie 相关
//                            .addInterceptor(new HeaderInterceptor(getRequestHeader())) // token过滤
//                            .addInterceptor(new ParameterInterceptor(getRequestParams()))  //公共参数添加
                //.addInterceptor(new CaheInterceptor(BaseApplication.getContext()))
                .addInterceptor(new LoggerInterceptor("===", true)) //日志拦截器
                //不加以下两行代码,https请求不到自签名的服务器
                .sslSocketFactory(createSSLSocketFactory())//创建一个证书对象
                .hostnameVerifier(new TrustAllHostnameVerifier())//校验名称,这个对象就是信任所有的主机,也就是信任所有https的请求
                // .cache(cache)  //添加缓存
                .retryOnConnectionFailure(false)//连接不上是否重连,false不重连
                .connectTimeout(10, TimeUnit.SECONDS)//连接超时时间
                .readTimeout(15, TimeUnit.SECONDS)//读取超时时间
                .writeTimeout(15, TimeUnit.SECONDS)//写入超时时间
                .build();

        // 获取retrofit的实例

        return new Retrofit
                .Builder()
                .baseUrl(AppBaseConfig.getConfig().getBaseUrl())  //baseUrl配置
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加rxjava2
                .addConverterFactory(HttpCovertFactory.create())//添加自定义Gson解析
                .build();
    }

    /**
     * 根据各模块业务接口 获取不同的retrofit service接口对象
     */
    public <T> T getRetrofitService(Class<T> cls) {

        return createRetrofit().create(cls);
    }

    /**
     * 实现https请求
     */
    private static SSLSocketFactory createSSLSocketFactory() {


        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    private static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 信任所有的服务器,返回true
     */
    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


}
