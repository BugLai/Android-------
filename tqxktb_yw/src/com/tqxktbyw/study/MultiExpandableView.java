package com.tqxktbyw.study;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.tqxktbyw.study.R;
import com.tqxktbyw.study.ExpandableView.OnExpandListener;
import com.tqxktbyw.study.ExpandableView.OnHandleClickListener;

/**
 * class MultiExpandableView
 * @author Garfield
 * @version 1.0
 */
public class MultiExpandableView extends ViewGroup {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL   = 1;

    public final int orientation;
    private final int mExpandedViewId;

    private ExpandableView mExpandedView;
    private OnItemExpandListener mOnItemExpandListener;

    public MultiExpandableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiExpandableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiExpandableView);
        orientation  = a.getInteger(R.styleable.MultiExpandableView_orientation, VERTICAL);
        mExpandedViewId = a.getResourceId(R.styleable.MultiExpandableView_expandedView, 0);
        a.recycle();
    }

    /**
     * Sets the expanded view of this container.
     * @param whichView The expanded view.
     * @see #setExpandedView(int)
     * @see #getExpandedView()
     */
    public void setExpandedView(ExpandableView whichView) {
        if (mExpandedView != whichView) {
            mExpandedView.setExpanded(false);
            mExpandedView = whichView;
            mExpandedView.setExpanded(true);
        }
    }

    /**
     * Sets the expanded view of this container.
     * @param resId The expanded view resource id.
     * @see #setExpandedView(ExpandableView)
     * @see #getExpandedView()
     */
    public final void setExpandedView(int resId) {
        setExpandedView((ExpandableView)findViewById(resId));
    }

    /**
     * Gets the expanded view.
     * @return the expanded view.
     * @see #setExpandedView(int)
     * @see #setExpandedView(ExpandableView)
     */
    public ExpandableView getExpandedView() {
        return mExpandedView;
    }

    public final void setOnItemExpandListener(OnItemExpandListener listener) {
        mOnItemExpandListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mExpandedView = (ExpandableView)findViewById(mExpandedViewId);
        mExpandedView.setExpanded(true);

        for (int i = 0, count = getChildCount(); i < count; ++i) {
            final ExpandableView child = (ExpandableView)getChildAt(i);
            child.setOnExpandListener(mOnExpandListener);
            child.setOnHandleClickListener(mOnHandleClickListener);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (orientation == VERTICAL) {
            layoutVertical(right - left);
        } else {
            layoutHorizontal(bottom - top);
        }
    }

    private void layoutVertical(int parentWidth) {
        for (int i = 0, top = 0, count = getChildCount(); i < count; ++i) {
            final View child = getChildAt(i);
            child.layout(0, top, parentWidth, top += child.getMeasuredHeight());
        }
    }

    private void layoutHorizontal(int parentHeight) {
        for (int i = 0, left = 0, count = getChildCount(); i < count; ++i) {
            final View child = getChildAt(i);
            child.layout(left, 0, left += child.getMeasuredWidth(), parentHeight);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (orientation == VERTICAL) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = getMeasuredHeight();
        for (int i = 0, count = getChildCount(); i < count; ++i) {
            final View child = getChildAt(i);
            if (mExpandedView != child) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                measuredHeight -= child.getMeasuredHeight();
            }
        }

        mExpandedView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.getMode(heightMeasureSpec)));
    }

    private void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = getMeasuredWidth();
        for (int i = 0, count = getChildCount(); i < count; ++i) {
            final View child = getChildAt(i);
            if (mExpandedView != child) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                measuredWidth -= child.getMeasuredWidth();
            }
        }

        mExpandedView.measure(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.getMode(widthMeasureSpec)), heightMeasureSpec);
    }

    private final OnHandleClickListener mOnHandleClickListener = new OnHandleClickListener() {
        @Override
        public boolean onClick(ExpandableView parent, View handle) {
            setExpandedView(parent);
            return true;
        }
    };

    private final OnExpandListener mOnExpandListener = new OnExpandListener() {
        @Override
        public void onExpand(ExpandableView view, boolean expanded) {
            if (mOnItemExpandListener != null) {
                mOnItemExpandListener.onItemExpand(MultiExpandableView.this, view, expanded);
            }
        }
    };

    /**
     * Used for being notified when a item is expanded.
     */
    public interface OnItemExpandListener {
        /**
         * Callback method to be invoked when a item in this container has been expanded.
         * @param parent The MultiExpandableView where the expanded happened.
         * @param view The item view that was expanded.
         * @param expanded <tt>true</tt> if the view was expanded; <tt>false</tt> otherwise.
         */
        void onItemExpand(MultiExpandableView parent, ExpandableView view, boolean expanded);
    }

}