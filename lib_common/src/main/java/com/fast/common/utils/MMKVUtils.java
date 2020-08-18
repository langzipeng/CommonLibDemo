package com.fast.common.utils;

import com.tencent.mmkv.MMKV;

public class MMKVUtils {

    private static MMKV kv = null;

    public static MMKV getMMKV() {
        if (kv == null) {
            kv = MMKV.defaultMMKV();
        }

        return kv;
    }


}
