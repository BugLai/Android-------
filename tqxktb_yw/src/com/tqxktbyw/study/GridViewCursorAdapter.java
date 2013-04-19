package com.tqxktbyw.study;

import com.tqxktbyw.study.EastStudy.BaseColumns;
import com.tqxktbyw.study.EastStudy.Grade;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class GridViewCursorAdapter extends BaseAdapter {
    private static final String EMPTY_STRING = "";

    private Context mContext;
    private Cursor mCursor;
    private int mLayoutResId;
    private int bgResId;
    BitmapFactory.Options opts;

    public GridViewCursorAdapter(Context context, Cursor cursor, int layoutResId,int _bgResId) {
        mContext = context;
        mCursor  = cursor;
        mLayoutResId = layoutResId;
        bgResId = _bgResId;
        opts = new BitmapFactory.Options();
        opts.inTargetDensity = 240;
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

    public final String getItemText(int position,int txtpostion) {
        String text = EMPTY_STRING;
        if (mCursor != null && mCursor.moveToPosition(position)) {
            //text = mCursor.getString(BaseColumns.COLUMN_NAME_INDEX);
        	text = mCursor.getString(txtpostion);
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
        
        TextView tv =(TextView)view.findViewById(R.id.text);
        tv.setText(getItemText(position,Grade.COLUMN_NAME_INDEX));
        view.setTag(getItemId(position));
        return view;
    }
   
}
