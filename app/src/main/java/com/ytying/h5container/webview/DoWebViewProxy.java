package com.ytying.h5container.webview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;

import com.ytying.h5container.api.H5Message;
import com.ytying.h5container.cache.H5CacheManage;
import com.ytying.h5container.model.DoWebResourceResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by kefan.wkf on 16/7/8.
 */
public class DoWebViewProxy {

    private Context context;
    private DoWebView webView;

    private H5Message mH5MsgCallback;

    //当前WebView界面的网络环境，是否为WIFI
    private boolean mIsWifi = false;

    public DoWebViewProxy(Context context) {
        this.context = context;
        webView = new DoWebView(context);
        //初始化bridge
        initJSBridgeManager();
        setWebViewCallBack();
    }

    public DoWebViewProxy(DoWebView webView) {
        context = webView.getContext();
        this.webView = webView;
        //初始化bridge
        initJSBridgeManager();
        setWebViewCallBack();
    }

    private void setWebViewCallBack() {

    }

    private void initJSBridgeManager() {

    }

    //---------WebView started---------
    public void clearCache(boolean flag) {
        getWebView().clearCache(flag);
    }

    public void reload() {
        Object url = getWebView().getUrl();
        getWebView().loadUrl(url.toString());
    }

    public void clearFormData() {
        getWebView().clearFormData();
    }

    public void loadUrl(String url) {
        getWebView().loadUrl(url);
    }

    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        getWebView().loadUrl(url, additionalHttpHeaders);
    }

    public void postUrl(String url, byte[] postData) {
        getWebView().postUrl(url, postData);
    }

    public void onResume() {
        getWebView().onResume();
    }

    public void onPause() {
        getWebView().onPause();
    }

    public boolean canGoBack() {
        return getWebView().canGoBack();
    }

    public void goBack() {
        getWebView().goBack();
    }

    public IBinder getWindowToken() {
        return getWebView().getWindowToken();
    }

    public void stopLoading() {
        getWebView().stopLoading();
    }

    public String getUrl() {
        return getWebView().getUrl();
    }

    public void setOnTouchListener(View.OnTouchListener l) {
        getWebView().setOnTouchListener(l);
    }

    /**
     * 屏幕截图
     *
     * @return
     */
    public Bitmap captureWebView() {
        Picture pic = getWebView().capturePicture();
        Bitmap bmp = Bitmap.createBitmap(pic.getWidth(), pic.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        pic.draw(canvas);
        return bmp;
    }

    //---------WebClient started---------
    public DoWebResourceResponse shouldInterceptRequest(String url) {
        //由于4.0以下的系统，可能找不到WebResourceResponse这个类，因此4.0以下的系统不做缓存
        if (Build.VERSION.SDK_INT >= 14) {
            try {
                //如果客户端要拦截此URL，则返回空数据
                Object obj = onMessage(H5Message.Type.SHOULD_INTERCEPT_REQUEST, url);
                if (obj != null && ((Boolean) obj)) {
                    InputStream is = new ByteArrayInputStream("".getBytes());
                    DoWebResourceResponse ret = new DoWebResourceResponse("", null, is);
                    return ret;
                }
                DoWebResourceResponse response = H5CacheManage.getInstance().loadWebResource(url, mIsWifi);
                return response;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Object onMessage(H5Message.Type t, Object data) {
        if (mH5MsgCallback != null)
            return mH5MsgCallback.onMessage(t, data);
        return null;
    }

    // 设置网页加载的进度条
    public void onProgressChanged(int newProgress) {
        if (newProgress >= 99) {
            onMessage(H5Message.Type.HIDE_WEBVIEW_LOADING_VIEW, null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onMessage(H5Message.Type.START_CHECK_WEBVIEW_TITLE_TEXT, null);
                }
            }, 300);
        } else {
            onMessage(H5Message.Type.SET_WEBVIEW_PROGRESSBAR, String.valueOf(newProgress));
        }
    }

    public void onReceivedTitle(String title) {
        onMessage(H5Message.Type.RECEIVED_HTML_TITLE, title);
    }

    public void dealMessage(String message) {

    }

    public void onConsoleMessage(String message, int lineNumber, String sourceID) {

    }

    //get or set
    public void setH5MessageCallback(H5Message h5msg) {
        mH5MsgCallback = h5msg;
    }

    public Context getContext() {
        return context;
    }

    public DoWebView getWebView() {
        if (null == webView) {
            return new DoWebView(context);
        }
        return webView;
    }

    public boolean ismIsWifi() {
        return mIsWifi;
    }


}
