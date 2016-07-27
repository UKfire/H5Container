package com.ytying.h5container.refresh;

import android.view.View;

/**
 * Created by kefan.wkf on 16/7/19.
 */
public interface H5PullAdapter {

    boolean canPull();

    boolean canRefresh();

    View getHeaderView();

    void onOpen();

    void onOver();

    void onLoading();

    void onFinish();
}
