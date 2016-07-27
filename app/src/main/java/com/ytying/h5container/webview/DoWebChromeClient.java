package com.ytying.h5container.webview;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by kefan.wkf on 16/7/8.
 */
public class DoWebChromeClient extends WebChromeClient {

    private final String JS_PROMPT_TAG = "do_android://";
    private DoWebViewProxy mWebView;
    private Handler mHandler = new Handler(Looper.myLooper());

    public DoWebChromeClient(DoWebViewProxy proxy) {
        mWebView = proxy;
    }

    @Override
    public boolean onJsPrompt(final WebView view, String url, final String message, String defaultValue, JsPromptResult result) {
        Log.v("xixixi", url);
        Log.v("xixixi", message);
        Log.v("xixixi", defaultValue);

        Log.v("vane", "2 === " + message);

        if (!TextUtils.isEmpty(message) && message.startsWith(JS_PROMPT_TAG)) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        mWebView.dealMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        result.cancel();
        return true;
    }

    // 设置网页加载的进度条
    @Override
    public void onProgressChanged(final WebView view, int newProgress) {
        mWebView.onProgressChanged(newProgress);
    }

    public void onConsoleMessage(String message, int lineNumber, String sourceID) {
        mWebView.onConsoleMessage(message, lineNumber, sourceID);
    }
}
