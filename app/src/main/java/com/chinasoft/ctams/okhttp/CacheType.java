package com.chinasoft.ctams.okhttp;

import android.support.annotation.IntDef;

/**
 * ONLY_NETWORK  只读取网络;  ONLY_CACHED 只读取内存;  CACHED_ELSE_NETWORK 先读取内存,后读取网络
 * NETWORK_ELSE_CACHED 先读取网络后读取内存
 */
@IntDef({CacheType.ONLY_NETWORK,CacheType.ONLY_CACHED,CacheType.CACHED_ELSE_NETWORK,CacheType.NETWORK_ELSE_CACHED})
public @interface CacheType {
    int ONLY_NETWORK = 0;
    int ONLY_CACHED = 1;
    int CACHED_ELSE_NETWORK =2;
    int NETWORK_ELSE_CACHED = 3;


}
