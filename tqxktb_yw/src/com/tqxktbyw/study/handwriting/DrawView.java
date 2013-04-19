package com.tqxktbyw.study.handwriting;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
	private List<Path> listpath;
	private Path mpath;
	private Paint paint;
	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 1;

	public DrawView(Context context) {
		super(context);
		setFocusable(true); // necessary for getting the touch events
		listpath = new ArrayList<Path>();
		mpath = new Path();
		paint = new Paint();
		
		
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(0xFFFF0000);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(18);
		
		
		
//		paint.setColor(Color.BLUE);
//		paint.setStyle(Paint.Style.STROKE);
//		paint.setStrokeWidth(10);
	}

	// the method that draws the balls
	@Override
	protected void onDraw(Canvas canvas) {
		// canvas.drawColor(0xFFCCCCCC); //if you want another background color
		// draw the balls on the canvas
		canvas.drawColor(android.R.color.transparent);
		if (listpath.size() > 0) {
			for (Path p : listpath) {
				canvas.drawPath(p, paint);
			}
		} else {
			canvas.drawPath(mpath, paint);
		}

	}

	// events when touching the screen
	public boolean onTouchEvent(MotionEvent event) {
		float X = event.getX();
		float Y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // touch down so check if the finger is on
										// a ball
			mpath.moveTo(X, Y);
			
			mX = X;
	        mY = Y;
			break;
		case MotionEvent.ACTION_MOVE: // touch drag with the ball
			// move the pan the same as the finger
			//mpath.lineTo(X, Y);

			 float dx = Math.abs(X- mX);
		        float dy = Math.abs(Y- mY);
		        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
		        	mpath.quadTo(mX, mY, (X + mX) / 2, (Y + mY) / 2);
		            mX = X;
		            mY = Y;
		        }
			
			
			
			break;

		case MotionEvent.ACTION_UP:
			// touch drop - just do things here after dropping
			// mpath.close();
			 mpath.lineTo(mX, mY);
		//	listpath.add(mpath);
			// mpath.rewind();
			break;
		}
		// redraw the canvas
		invalidate();
		return true;
	}
}
