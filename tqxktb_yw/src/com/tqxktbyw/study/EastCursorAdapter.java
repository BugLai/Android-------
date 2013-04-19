package com.tqxktbyw.study;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tqxktbyw.study.EastStudy.BaseColumns;

public class EastCursorAdapter extends BaseAdapter {
    private static final String EMPTY_STRING = "";

    private Context mContext;
    private Cursor mCursor;
    private int mTextResId;
    private int mLayoutResId;

    public EastCursorAdapter(Context context, Cursor cursor, int layoutResId, int textResId) {
        mContext = context;
        mCursor  = cursor;
        mTextResId   = textResId;
        mLayoutResId = layoutResId;
    }

    public final Cursor getCursor() {
        return mCursor;
    }
    
    public final Cursor detach() {
        Cursor cursor = mCursor;
        mCursor = null;

        return cursor;
    }

    public final void changeCursor(Cursor cursor) {
        if (cursor == mCursor) {
            return;
        }

        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = cursor;
        if (mCursor != null) {
            notifyDataSetChanged();
        } else {
            notifyDataSetInvalidated();
        }
    }

    public final String getItemText(int position) {
        String text = EMPTY_STRING;
        if (mCursor != null && mCursor.moveToPosition(position)) {
            text = mCursor.getString(BaseColumns.COLUMN_NAME_INDEX);
        }

        return text;
    }

    @Override
    public int getCount() {
        return (mCursor != null ? mCursor.getCount() : 0);
    }

    @Override
    public Object getItem(int position) {
        if (mCursor != null) {
            mCursor.moveToPosition(position);
        }

        return mCursor;
    }

    @Override
    public long getItemId(int position) {
        long id = 0;
        if (mCursor != null && mCursor.moveToPosition(position)) {
            id = mCursor.getLong(BaseColumns.COLUMN_ID_INDEX);
        }

        return id;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(mLayoutResId, null);
        }

        ((TextView)view.findViewById(mTextResId)).setText(getItemText(position));
        return view;
    }
}
