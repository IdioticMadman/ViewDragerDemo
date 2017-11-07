package com.robert.viewdragdemo;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * @author: robert
 * @date: 2017-11-05
 * @time: 16:45
 * @说明:
 */

public class DragVideoContainer extends RelativeLayout {
    private static final String TAG = DragVideoContainer.class.getSimpleName();
    private final ViewDragHelper mDragHelper;
    private View dragView;
    private int mDragViewLeft = -1;
    private int mDragViewTop = -1;

    public DragVideoContainer(Context context) {
        this(context, null);
    }

    public DragVideoContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {


            //返回true表示我们可以拖动这个child view
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child.getId() == R.id.drag_view;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - child.getWidth() - leftBound;
                return Math.min(Math.max(left, leftBound), rightBound);
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                final int topBound = getPaddingTop();
                final int bottomBound = getBottom() - child.getHeight() - topBound;
                return Math.min(Math.max(top, topBound), bottomBound);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (changedView.getId() == R.id.drag_view) {
                    mDragViewLeft = changedView.getLeft();
                    mDragViewTop = changedView.getTop();
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isCanDragge = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                final View toCapture = findTopChildUnder((int) x, (int) y);
                isCanDragge = toCapture != null && (toCapture == dragView);
                break;
            }
        }
        if (isCanDragge) {
            mDragHelper.processTouchEvent(ev);
            return super.onInterceptTouchEvent(ev);
        } else {
            return mDragHelper.shouldInterceptTouchEvent(ev);
        }
    }

    public View findTopChildUnder(int x, int y) {
        if (x >= dragView.getLeft() && x < dragView.getRight()
                && y >= dragView.getTop() && y < dragView.getBottom()) {
            return dragView;
        }
        return null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mDragViewLeft != -1 && mDragViewTop != -1) {
            dragView.layout(mDragViewLeft, mDragViewTop, mDragViewLeft + dragView.getWidth(), mDragViewTop + dragView.getHeight());
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        dragView = findViewById(R.id.drag_view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

}
