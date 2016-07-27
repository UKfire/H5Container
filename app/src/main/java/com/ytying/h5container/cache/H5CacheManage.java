package com.ytying.h5container.cache;

import com.ytying.h5container.model.DoWebResourceResponse;

/**
 * Created by kefan.wkf on 16/7/11.
 */
public class H5CacheManage {

    private static H5CacheManage mInstance;

    public static H5CacheManage getInstance() {
        if (mInstance == null) {
            synchronized (H5CacheManage.class) {
                if (mInstance == null) {
                    mInstance = new H5CacheManage();
                }
            }
        }
        return mInstance;
    }

    /**
     * 通过各种方式获取Web资源
     * @param resourceurl
     * @param isWifi
     * @return
     */
    public DoWebResourceResponse loadWebResource(String resourceurl, boolean isWifi) {
        return null;
    }
}
