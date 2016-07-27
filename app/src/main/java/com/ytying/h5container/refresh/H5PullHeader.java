package com.ytying.h5container.refresh;

/**
 * Created by kefan.wkf on 16/7/20.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ytying.h5container.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class H5PullHeader extends RelativeLayout {
    public static final String TAG = "H5PullHeader";
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private ProgressBar pbLoading;
    private View mRefreshLoadingLayout;
    private TextView tvTitle;
    private TextView tvSummary;

    /**
     * 下拉刷新的指示箭头
     */
    private ImageView mHintArrow;
    /**
     * 箭头向下翻转动画
     */
    private RotateAnimation mArrowAnimation;
    /**
     * 箭头向上翻转动画
     */
    private RotateAnimation mArrowReverseAnimation;


    public H5PullHeader(Context context) {
        super(context);
    }

    public H5PullHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getId() == NO_ID) {
            throw new RuntimeException("must set id");
        }
        if (isInEditMode())
            return;
        mHintArrow = (ImageView) findViewById(R.id.pullrefresh_image);
        mRefreshLoadingLayout = findViewById(R.id.pullrefresh_loading);
        pbLoading = (ProgressBar) findViewById(R.id.pullrefresh_progress);
        tvTitle = (TextView) findViewById(R.id.pullrefresh_title);
        tvSummary = (TextView) findViewById(R.id.pullrefresh_summary);
        setLastRefresh();

        mArrowAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mArrowAnimation.setInterpolator(new LinearInterpolator());
        mArrowAnimation.setDuration(200);
        mArrowAnimation.setFillAfter(true);

        mArrowReverseAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mArrowReverseAnimation.setInterpolator(new LinearInterpolator());
        mArrowReverseAnimation.setDuration(200);
        mArrowReverseAnimation.setFillAfter(true);
    }

    public void showOpen(boolean pullRefresh) {
//        pbLoading.setVisibility(View.INVISIBLE);
        tvTitle.setText("下拉刷新");

        mHintArrow.clearAnimation();
        mHintArrow.startAnimation(mArrowReverseAnimation);

        mRefreshLoadingLayout.setVisibility(pullRefresh ? View.VISIBLE : View.GONE);
    }

    public void showOver() {
        tvTitle.setText("松手刷新");
        mHintArrow.clearAnimation();
        mHintArrow.startAnimation(mArrowAnimation);
    }

    public void showLoading() {
        tvTitle.setText("正在刷新");
//        pbLoading.setVisibility(View.VISIBLE);
    }

    public void showFinish() {
//        pbLoading.setVisibility(View.INVISIBLE);
        setLastRefresh();
    }

    @SuppressLint("SimpleDateFormat")
    private void setLastRefresh() {
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        Date date = new Date(time);
        String formatedTime = sdf.format(date);
        formatedTime = String.format("上次更新 %s", formatedTime);
        tvSummary.setText(formatedTime);
    }
}
