package com.ytying.h5container.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by kefan.wkf on 16/7/8.
 */
public class DoWebView extends WebView {

    private DoWebViewProxy proxy;

    public DoWebView(Context context) {
        super(context);
        initDoWebViewWidget(context);
    }

    public DoWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDoWebViewWidget(context);
    }


    private void initDoWebViewWidget(final Context context) {
        // 取消滚动条
        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimetype, long contentLength) {
                //有些手机可能找不到那个页面，这里保护一下
                try {
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.startActivity(viewIntent);
                } catch (Exception e) {
                }
            }
        });

        WebSettings webSettings = this.getSettings();
        try {
            //加保护
            webSettings.setJavaScriptEnabled(true);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT > 11){
            webSettings.setDisplayZoomControls(false);
        }
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 5); // 缓存大小
        webSettings.setAppCachePath(context.getFilesDir() + "/appcache");
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        // 不让保存用户密码，保存的话会明文存放在/data/app/中，root的用户可以很容易拿到用户数据
        webSettings.setSavePassword(false);
        webSettings.setDatabaseEnabled(true);
        // 使webview支持localstorage必须设置此属性
        webSettings.setDatabasePath(context.getFilesDir() + "/webview_database");
        webSettings.setUseWideViewPort(true);
        // 硬件加速
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setLoadWithOverviewMode(true);

        //允许在HTTPS的情况下，加载HTTP资源
        if (Build.VERSION.SDK_INT > 20) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型：
         * 1、LayoutAlgorithm.NARROW_COLUMNS:适应内容大小
         * 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

    }


}
