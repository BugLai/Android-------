package com.tqxktbyw.study;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * class ExpandableView
 * @author Garfield
 * @version 1.0
 */
public class ExpandableView extends LinearLayout {
    public final int handle;
    public final int content;

    private View mHandle;
    private View mContent;

    private OnExpandListener mOnExpandListener;
    private OnHandleClickListener mOnHandleClickListener;

    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandableView);
        handle  = a.getResourceId(R.styleable.ExpandableView_handle, 0);
        content = a.getResourceId(R.styleable.ExpandableView_content, 0);
        a.recycle();
    }

    /**
     * Change the expanded state of the view.
     * @param expanded The new expanded state.
     * @see #isExpanded()
     */
    public void setExpanded(boolean expanded) {
        mContent.setVisibility(expanded ? VISIBLE : GONE);
        if (mOnExpandListener != null) {
            mOnExpandListener.onExpand(this, expanded);
        }
    }

    /**
     * Change the expanded state of the view to the inverse of its current state.
     */
    public void toggle() {
        setExpanded(mContent.getVisibility() == GONE);
    }

    /**
     * Returns the current expanded state of the view.
     * @return <tt>true</tt> if the view was expanded;
     * <tt>false</tt> otherwise.
     * @see #setExpanded(boolean)
     */
    public boolean isExpanded() {
        return (mContent.getVisibility() == VISIBLE);
    }

    /**
     * Returns the handle of the expandable view.
     * @return The View reprenseting the handle of the expandable view,
     * identified by the "handle" id in XML.
     * @see #getContent()
     */
    public View getHandle() {
        return mHandle;
    }

    /**
     * Returns the content of the expandable view.
     * @return The View reprenseting the content of the expandable view,
     * identified by the "content" id in XML.
     * @see #getHandle()
     */
    public View getContent() {
        return mContent;
    }

    public final void setOnExpandListener(OnExpandListener listener) {
        mOnExpandListener = listener;
    }

    public final void setOnHandleClickListener(OnHandleClickListener listener) {
        mOnHandleClickListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mHandle  = findViewById(handle);
        mContent = findViewById(content);

        mContent.setVisibility(GONE);
        mHandle.setOnClickListener(mOnClickListener);
    }

    private final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mOnHandleClickListener == null || !mOnHandleClickListener.onClick(ExpandableView.this, view)) {
                toggle();
            }
        }
    };

    /**
     * Used for being notified when the view is expanded.
     */
    public interface OnExpandListener {
        /**
         * Callback method to be invoked when the view has been expanded.
         * @param view The view was expanded.
         * @param expanded <tt>true</tt> if the view was expanded; <tt>false</tt> otherwise.
         */
        void onExpand(ExpandableView view, boolean expanded);
    }

    /**
     * Used for being notified when the handle view is clicked.
     */
    public interface OnHandleClickListener {
        /**
         * Callback method to be invoked when the handle view has been clicked.
         * @param parent The view that was clicked.
         * @param handle The handle view that was clicked.
         * @return <tt>true</tt> if the listener has consumed the event; <tt>false</tt> otherwise.
         */
        boolean onClick(ExpandableView parent, View handle);
    };
}