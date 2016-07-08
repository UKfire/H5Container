package com.ytying.h5container.webview;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by kefan.wkf on 16/7/8.
 */
public class DoWebChromeClient extends WebChromeClient {

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {

        return false;
    }
}
