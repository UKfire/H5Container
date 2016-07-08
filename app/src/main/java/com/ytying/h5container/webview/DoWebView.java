package com.ytying.h5container.webview;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by kefan.wkf on 16/7/8.
 */
public class DoWebView extends WebView {

    private DoWebViewProxy proxy;

    public DoWebView(Context context) {
        super(context);
    }

    public DoWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
