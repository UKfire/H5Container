package com.ytying.h5container;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ytying.h5container.api.H5Message;
import com.ytying.h5container.refresh.H5PullAdapter;
import com.ytying.h5container.refresh.H5PullHeader;
import com.ytying.h5container.refresh.HerPullContainer;
import com.ytying.h5container.webview.DoWebViewProxy;

import java.util.HashMap;

/**
 * Created by kefan.wkf on 16/7/11.
 */
public class BaseWebviewFragment extends Fragment implements H5Message {

    //是否显示title
    protected boolean mIsTitleShow = true;
    protected Context mContext;
    protected HerPullContainer mPullContainer;
    protected DoWebViewProxy mWebView;
    protected View mMainView;
    protected Button mBtnWebViewErrorRefresh;
    protected Button mBtnWebViewErrorClose;
    protected View mHeaderView;
    protected View mDoWebViewErrorView;
    protected int mLeftBtnBgResId = -1;
    private String mHtmlHead;

    protected LinearLayout mLlBottomSelector;

    private ProgressBar progressBar;
    /**
     * 原始传进来的url
     */
    protected String mGenaralUrl = "";
    /**
     * 当前加载的url
     */
    protected String mCurrentUrl = "";
    /**
     * webview title 文案
     */
    protected String mTitle = "H5Container";

    /**
     * webview html 源码 数据，直接loadData(String)
     */
    protected String mDataSource;
    /**
     * webview url 参数map
     */
    protected HashMap<String, String> mUrlParamMap;

    /**
     * 启动时，是否是在一个新的activity中
     */
    protected boolean mIsNewActivity = false;

    //loading框
    private Dialog mLoadingDlg;

    //是否支持下拉刷新
    private boolean mEnablePullRefresh = false;


    public BaseWebviewFragment(String url, boolean isTitleShow) {
        this.mGenaralUrl = this.mCurrentUrl = url;
        this.mIsTitleShow = isTitleShow;
    }

    public BaseWebviewFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.webview_main_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMainView = view;

        mPullContainer = (HerPullContainer) mMainView.findViewById(R.id.h5pullcontainer);
        mWebView = new DoWebViewProxy(getActivity());
        mWebView.setH5MessageCallback(this);
        mPullContainer.setContentView(mWebView.getWebView());
        mPullContainer.setPullAdapter(pullAdapter);


        loadWebViewUrl(mCurrentUrl);
    }

    private H5PullAdapter pullAdapter = new H5PullAdapter() {

        private H5PullHeader pullHeader;

        @Override
        public void onLoading() {
            if (pullHeader != null && mEnablePullRefresh) {
                pullHeader.showLoading();
                mWebView.reload();
            }
        }

        @Override
        public boolean canRefresh() {
            return true;
        }

        @Override
        public boolean canPull() {
            return true;
        }

        @Override
        public View getHeaderView() {
            if (pullHeader == null) {
                pullHeader = (H5PullHeader) LayoutInflater.from(getActivity()).inflate(R.layout.h5_pull_header, mPullContainer, false);
            }
            return pullHeader;
        }

        @Override
        public void onOpen() {
            if (pullHeader != null) {
                pullHeader.showOpen(mEnablePullRefresh);
            }
        }

        @Override
        public void onOver() {
            if (pullHeader != null) {
                pullHeader.showOver();
            }
        }

        @Override
        public void onFinish() {
            if (pullHeader != null) {
                pullHeader.showFinish();
            }
            mPullContainer.fitContent();
        }
    };

    public void loadWebViewUrl(final String url) {
        if (null == url || null == mWebView)
            return;
        if (!TextUtils.isEmpty(url) && url.equals(mWebView.getUrl())) {
            mWebView.clearCache(true);
            mWebView.clearFormData();
            mWebView.loadUrl(mWebView.getUrl());
        } else {
            if (url.startsWith("http")) {
                loadUrl(url);
            }
        }
    }

    private void loadUrl(String url) {
        mCurrentUrl = url;
        mWebView.loadUrl(mCurrentUrl);
    }


    /**
     * 处理onMessage逻辑
     *
     * @param type
     * @param data
     * @return
     */
    @Override
    public Object onMessage(Type type, Object data) {
        switch (type) {
            case SHOULD_INTERCEPT_REQUEST:
                return false;
            case SET_PAGE_TITLE:
                break;
        }
        return null;
    }
}
