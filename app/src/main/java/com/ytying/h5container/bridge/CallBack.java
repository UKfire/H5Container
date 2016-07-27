package com.ytying.h5container.bridge;

import android.os.Handler;
import android.os.Looper;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.ref.WeakReference;

/**
 * Created by kefan.wkf on 16/7/8.
 */
public class CallBack {
    private static Handler mHandler = new Handler(Looper.myLooper().getMainLooper());
    private static final String CALLBACK_JS_FORMAT = "javascript:JSBridge.onFinish('%s',%s);";
    private String mPort;
    private WeakReference<WebView> mDoWebView;

    public CallBack(WebView doWebView, String port) {
        mPort = port;
        mDoWebView = new WeakReference<WebView>(doWebView);
    }

    public void apply(JSONObject jsonObject) {
        final String execJs = String.format(CALLBACK_JS_FORMAT, mPort, JSON.toJSONString(jsonObject));
        if (mDoWebView != null && mDoWebView.get() != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mDoWebView.get().loadUrl(execJs);
                }
            });
        }
    }


}
