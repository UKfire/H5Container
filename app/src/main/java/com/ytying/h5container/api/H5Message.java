package com.ytying.h5container.api;

/**
 * Created by kefan.wkf on 16/7/11.
 */
public interface H5Message {
    public static enum Type{
        SET_PAGE_TITLE,     //设置WebView标题
        SET_PAGE_LEFT_BTN,
        SET_PAGE_RIGHT_BTN,
        SET_UI,
        SHOULD_INTERCEPT_REQUEST,       //是否要拦截URL请求
        OPEN_PROGRESS_DIALOG,
        CLOSE_PROGRESS_DIALOG,
        HIDE_WEBVIEW_LOADING_VIEW,
        SHOW_WEBVIEW_LOADING_VIEW,
        SHOW_WEBVIEW_ERROR_VIEW,
        HIDE_WEBVIEW_ERROR_VIEW,
        SET_WEBVIEW_PROGRESSBAR,
        CLOSE_FRAGMENT,
        START_CHECK_WEBVIEW_TITLE_TEXT,
        RECEIVED_HTML_TITLE
    }

    public Object onMessage(Type t,Object data);
}
