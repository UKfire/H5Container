package com.ytying.h5container.refresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by kefan.wkf on 16/7/19.
 */
public class H5PullContainer extends FrameLayout {

    public static final int DEFAULT_DURATION = 400;

    private enum State {
        STATE_FIT_CONTENT, STATE_OPEN, STATE_OVER, STATE_FIT_EXTRAS
    }

    protected State state = State.STATE_FIT_CONTENT;
    private Finger finger = new Finger();
    private H5PullAdapter pullAdapter;
    private View contentView;
    private boolean overScrolled;

    private int lastY;
    private int lastX;
    private int lastInterceptX;
    private int lastInterceptY;

    private View headerView;
    protected int headerHeight;

    public H5PullContainer(Context context) {
        super(context);
    }

    public H5PullContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public H5PullContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        int y = (int) event.getY();
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = lastInterceptY - y;
                int deltaX = lastInterceptX - x;
                int topY = contentView.getTop();
                if (Math.abs(deltaY) >= Math.abs(deltaX) && deltaY < 0) {
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = true;
                break;
            default:
                break;
        }
        lastInterceptY = y;
        lastInterceptX = x;
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetY = lastY - y;
                int offsetX = lastX - x;
                if (Math.abs(offsetY) > Math.abs(offsetX))
                    offsetY /= 2;
                moveOffset(-offsetY);
                break;
            case MotionEvent.ACTION_UP:
                if (contentView.getTop() > headerHeight) {
                    int offset = contentView.getTop() - headerHeight;
                    finger.recover(offset);
                    if (pullAdapter != null)
                        pullAdapter.onFinish();
                    else
                        fitContentView();
                } else {
                    fitContentView();
                }
                break;
            default:
                break;
        }
        lastY = y;
        lastX = x;
        return true;
    }


    /**
     * 手指触屏滑动
     *
     * @param offset
     */
    private void moveOffset(int offset) {
        if (null == contentView)
            return;
        if (offset < 0 && contentView.getTop() < 0)
            return;

        if (offset < 0) {     //向上滑动
            if (contentView.getTop() <= 0) {
                return;
            } else {
                contentView.offsetTopAndBottom(offset);
            }
        } else {     //向下滑动
            if (contentView.getTop() < 300)
                contentView.offsetTopAndBottom(offset);
            else if (contentView.getTop() > 300 && contentView.getTop() < 350)
                contentView.offsetTopAndBottom(offset / 3);
            else if (contentView.getTop() >= 350 && contentView.getTop() < 400)
                contentView.offsetTopAndBottom(offset / 5);
            else if (contentView.getTop() >= 400 && contentView.getTop() < 450)
                contentView.offsetTopAndBottom(offset / 7);
        }

    }

    /**
     * 自动回滚器
     */
    private class Finger implements Runnable {

        private Scroller scroller;
        private int lastScrollY;
        private boolean finished;

        public Finger() {
            scroller = new Scroller(getContext());
            finished = true;
        }

        @Override
        public void run() {
            boolean offset = scroller.computeScrollOffset();
            if (offset) {
                moveOffset(lastScrollY - scroller.getCurrY());
                lastScrollY = scroller.getCurrY();
                post(this);
            } else {
                finished = true;
                removeCallbacks(this);
            }
        }

        public void recover(int offset) {
            removeCallbacks(this);
            lastScrollY = 0;
            finished = false;
            scroller.startScroll(0, 0, 0, offset, DEFAULT_DURATION);
            post(this);
        }

        public boolean isFinished() {
            return finished;
        }
    }

    public void fitContentView() {
        if (null == contentView)
            return;
        int top = contentView.getTop();
        if (top > 0)
            finger.recover(top);
    }


    /**
     * 是否有HeaderView
     *
     * @return
     */
    private boolean hasHeader() {
        if (null == headerView)
            return false;
        return true;
    }

    /**
     * 是否能下拉刷新
     *
     * @return
     */
    private boolean canPull() {
        return false;
    }

    //-------------setter or getter-------------
    public void setContentView(View contentView) {
        this.contentView = contentView;
        addView(contentView, 0, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setPullAdapter(H5PullAdapter adapter) {
        this.pullAdapter = adapter;
        updateHeaderView();
    }

    private void updateHeaderView() {
        if (headerView != null) {
            removeView(headerView);
            headerView = null;
        }

        if (getChildCount() < 1) {
            throw new IllegalStateException("content view not added yet");
        }

        headerView = pullAdapter.getHeaderView();
        if (null == headerView)
            return;

        headerView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        headerHeight = headerView.getMeasuredHeight();
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, headerHeight);
        addView(headerView, 0, params);
    }


}
