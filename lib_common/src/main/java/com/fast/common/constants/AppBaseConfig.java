package com.fast.common.constants;

/**
 * APP 的配置参数
 */
public class AppBaseConfig {


    private static final Config CONFIG = new Config();

    public static Config getConfig() {
        return CONFIG;
    }

    public static class Config {

        //基础路径
        private String mBaseUrl = "";

        public Config setBaseUrl(String baseUrl) {
            mBaseUrl = baseUrl;
            return this;
        }

        public String getBaseUrl() {
            return mBaseUrl;
        }


    }


}
