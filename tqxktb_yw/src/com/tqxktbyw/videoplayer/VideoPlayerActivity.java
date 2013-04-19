package com.tqxktbyw.videoplayer;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.tqxktbyw.study.EastStudy;
import com.tqxktbyw.study.R;
import com.tqxktbyw.videoplayer.VideoView.MySizeChangeLinstener;

public class VideoPlayerActivity extends Activity
{

	private final static String TAG = "VideoPlayerActivity";

	// private Uri videoListUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	// private static int position ;
	// private static boolean backFromAD = false;
	private int playedTime;

	// private AdView adView;

	private VideoView vv = null;
	private SeekBar seekBar = null;
	private TextView durationTextView = null;
	private TextView playedTextView = null;
	private GestureDetector mGestureDetector = null;
	// private AudioManager mAudioManager = null;

	// private int maxVolume = 0;
	// private int currentVolume = 0;

	private ImageButton bn2 = null;
	private ImageButton bn3 = null;
	private ImageButton bn4 = null;
	// private ImageButton bn5 = null;

	private View controlView = null;
	private PopupWindow controler = null;

	// private SoundView mSoundView = null;
	// private PopupWindow mSoundWindow = null;

	private static int screenWidth = 0;
	private static int screenHeight = 0;
	private static int controlHeight = 105;
	private static int controlWidth = 500;

	private final static int TIME = 6868;

	private String uri = "";
	private boolean isControllerShow = true;
	private boolean isPaused = false;
	private boolean isFullScreen = false;
	// private boolean isSilent = false;
	// private boolean isSoundShow = false;
	private final static int skipStep = 60 * 2 * 1000;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.lesson_player);

		Log.d("OnCreate", getIntent().toString());

		controlView = getLayoutInflater().inflate(R.layout.controler, null);
		controler = new PopupWindow(controlView);
		controler.setBackgroundDrawable(getResources().getDrawable(R.drawable.playerground));
		durationTextView = (TextView) controlView.findViewById(R.id.duration);
		playedTextView = (TextView) controlView.findViewById(R.id.has_played);

		bn2 = (ImageButton) controlView.findViewById(R.id.button2);
		bn3 = (ImageButton) controlView.findViewById(R.id.button3);
		bn4 = (ImageButton) controlView.findViewById(R.id.button4);

		vv = (VideoView) findViewById(R.id.lesson_player);

		Intent intent = getIntent();
		uri = intent.getStringExtra(EastStudy.PLAYING_URI);

		if (uri != "")
		{
			if (vv.getVideoHeight() == 0)
			{
				vv.setVideoURI(Uri.parse(uri));
			}
			bn3.setImageResource(R.drawable.pause);
		} else
		{
			bn3.setImageResource(R.drawable.play);
		}

		vv.setMySizeChangeLinstener(new MySizeChangeLinstener()
		{

			@Override
			public void doMyThings()
			{
				setVideoScale(SCREEN_DEFAULT);
			}

		});
		bn2.setAlpha(0xBB);
		bn3.setAlpha(0xBB);
		bn4.setAlpha(0xBB);

		bn4.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				int seekpos = vv.getCurrentPosition() + skipStep;
				if (seekpos >= vv.getDuration())
					vv.seekTo(0);
				else
					vv.seekTo(seekpos);

			}

		});

		bn3.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				cancelDelayHide();
				if (isPaused)
				{
					vv.start();
					bn3.setImageResource(R.drawable.pause);
					hideControllerDelay();
				} else
				{
					vv.pause();
					bn3.setImageResource(R.drawable.play);
				}
				isPaused = !isPaused;

			}

		});

		bn2.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				int seekpos = vv.getCurrentPosition() - skipStep;
				if (seekpos > 0)
					vv.seekTo(seekpos);
				else
					vv.seekTo(0);
			}

		});

		seekBar = (SeekBar) controlView.findViewById(R.id.seekbar);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{

			@Override
			public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser)
			{
				if (fromUser)
				{
					vv.seekTo(progress);
				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0)
			{
				myHandler.removeMessages(HIDE_CONTROLER);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
				myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
			}
		});

		getScreenSize();

		mGestureDetector = new GestureDetector(new SimpleOnGestureListener()
		{

			@Override
			public boolean onDoubleTap(MotionEvent e)
			{
				if (isFullScreen)
				{
					// setVideoScale(SCREEN_DEFAULT);
				} else
				{
					setVideoScale(SCREEN_FULL);
				}
				// isFullScreen = !isFullScreen;
				Log.d(TAG, "onDoubleTap");

				if (isControllerShow)
				{
					showController();
				}
				// return super.onDoubleTap(e);
				return true;
			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent e)
			{
				if (!isControllerShow)
				{
					showController();
					hideControllerDelay();
				} else
				{
					cancelDelayHide();
					hideController();
				}
				// return super.onSingleTapConfirmed(e);
				return true;
			}

			@Override
			public void onLongPress(MotionEvent e)
			{
				if (isPaused)
				{
					vv.start();
					bn3.setImageResource(R.drawable.pause);
					cancelDelayHide();
					hideControllerDelay();
				} else
				{
					vv.pause();
					bn3.setImageResource(R.drawable.play);
					cancelDelayHide();
					showController();
				}
				isPaused = !isPaused;
				// super.onLongPress(e);
			}
		});

		vv.setOnPreparedListener(new OnPreparedListener()
		{

			@Override
			public void onPrepared(MediaPlayer arg0)
			{
				setVideoScale(SCREEN_FULL);
				isFullScreen = true;
				if (isControllerShow)
				{
					showController();
				}

				int i = vv.getDuration();
				Log.d("onCompletion", "" + i);
				seekBar.setMax(i);
				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				durationTextView.setText(String.format("%02d:%02d:%02d", hour, minute, second));

				if (controler != null && vv.isShown())
				{
					controler.showAtLocation(vv, Gravity.BOTTOM, 0, 0);
					controler.update(0, 0, controlWidth, controlHeight);
				}

				vv.start();
				bn3.setImageResource(R.drawable.pause);
				hideControllerDelay();
				myHandler.sendEmptyMessage(PROGRESS_CHANGED);
			}
		});

		vv.setOnCompletionListener(new OnCompletionListener()
		{

			@Override
			public void onCompletion(MediaPlayer arg0)
			{
				/*
				 * int n = playList.size(); if(++position < n){
				 * vv.setVideoPath(playList.get(position).path); }else{
				 * VideoPlayerActivity.this.finish(); }
				 */
				vv.stopPlayback();
				VideoPlayerActivity.this.finish();
			}
		});
	}

	private final static int PROGRESS_CHANGED = 0;
	private final static int HIDE_CONTROLER = 1;

	Handler myHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{

			case PROGRESS_CHANGED:

				int i = vv.getCurrentPosition();
				seekBar.setProgress(i);

				i /= 1000;
				int minute = i / 60;
				int hour = minute / 60;
				int second = i % 60;
				minute %= 60;
				playedTextView.setText(String.format("%02d:%02d:%02d", hour, minute, second));

				sendEmptyMessage(PROGRESS_CHANGED);
				break;

			case HIDE_CONTROLER:
				hideController();
				break;
			}

			super.handleMessage(msg);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		/*
		 * if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
		 * 
		 * mWebView.goBack();
		 * 
		 * return true;
		 * 
		 * }
		 */
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(VideoPlayerActivity.this);

			builder.setMessage(R.string.view_exit_msg);

			builder.setTitle(R.string.title_dialog_alert);

			builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{

					dialog.dismiss();

					VideoPlayerActivity.this.finish();
				}

			});

			builder.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{

					dialog.dismiss();

				}

			});

			builder.create().show();

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		boolean result = mGestureDetector.onTouchEvent(event);

		if (!result)
		{
			if (event.getAction() == MotionEvent.ACTION_UP)
			{

				/*
				 * if(!isControllerShow){ showController();
				 * hideControllerDelay(); }else { cancelDelayHide();
				 * hideController(); }
				 */
			}
			result = super.onTouchEvent(event);
		}

		return result;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		getScreenSize();
		if (isControllerShow)
		{

			cancelDelayHide();
			hideController();
			showController();
			hideControllerDelay();
		}

		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPause()
	{
		playedTime = vv.getCurrentPosition();
		vv.pause();
		bn3.setImageResource(R.drawable.play);
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		vv.seekTo(playedTime);
		vv.start();
		if (vv.getVideoHeight() != 0)
		{
			bn3.setImageResource(R.drawable.pause);
			hideControllerDelay();
		}
		Log.d("REQUEST", "NEW AD !");

		super.onResume();
	}

	@Override
	protected void onDestroy()
	{
		if (controler != null)
		{
			controler.dismiss();
		}
		/*
		 * if(mSoundWindow.isShowing()){ mSoundWindow.dismiss(); }
		 */

		myHandler.removeMessages(PROGRESS_CHANGED);
		myHandler.removeMessages(HIDE_CONTROLER);

		File file = new File(uri);
		file.delete();
		super.onDestroy();
	}

	private void getScreenSize()
	{
		Display display = getWindowManager().getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();
		// controlHeight = screenHeight/4;
	}

	private void hideController()
	{
		if (controler.isShowing())
		{
			// controler.update(0,0,0, 0);
			// isControllerShow = false;
		}

	}

	private void hideControllerDelay()
	{
		myHandler.sendEmptyMessageDelayed(HIDE_CONTROLER, TIME);
	}

	private void showController()
	{
		controler.update(0, 0, controlWidth, controlHeight);

		isControllerShow = true;
	}

	private void cancelDelayHide()
	{
		myHandler.removeMessages(HIDE_CONTROLER);
	}

	private final static int SCREEN_FULL = 0;
	private final static int SCREEN_DEFAULT = 1;

	private void setVideoScale(int flag)
	{

		// LayoutParams lp = vv.getLayoutParams();

		switch (flag)
		{
		case SCREEN_FULL:

			Log.d(TAG, "screenWidth: " + screenWidth + " screenHeight: " + screenHeight);
			vv.setVideoScale(screenWidth, screenHeight);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

			break;

		case SCREEN_DEFAULT:

			int videoWidth = vv.getVideoWidth();
			int videoHeight = vv.getVideoHeight();
			int mWidth = screenWidth;
			int mHeight = screenHeight - 25;

			if (videoWidth > 0 && videoHeight > 0)
			{
				if (videoWidth * mHeight > mWidth * videoHeight)
				{
					// Log.i("@@@", "image too tall, correcting");
					mHeight = mWidth * videoHeight / videoWidth;
				} else if (videoWidth * mHeight < mWidth * videoHeight)
				{
					// Log.i("@@@", "image too wide, correcting");
					mWidth = mHeight * videoWidth / videoHeight;
				} else
				{

				}
			}

			vv.setVideoScale(mWidth, mHeight);

			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

			break;
		}
	}

}