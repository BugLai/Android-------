package com.tqxktbyw.study;

import java.io.File;

import com.tqxktbyw.videoplayer.VideoView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.MediaController;

public class LessonPlayer extends Activity implements OnErrorListener, OnPreparedListener,OnCompletionListener {
    private VideoView mVideoLesson;
    private MediaController mMediaController;
    private String uri;
    private int stopposition = 10000;
    private int prefacePosition;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lesson_player_test);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        Intent intent  = getIntent();
        uri = intent.getStringExtra(EastStudy.PLAYING_URI);
        //uri = "file:///android_asset/11.mp4";
        //uri = "file:///mnt/sdcard/qpad.mp4";
        //uri = "file:///mnt/sdcard/tqpad4xktb/11.mp4";

        mMediaController = new MediaController(this);
        mVideoLesson = (VideoView)findViewById(R.id.lesson_player);
        mVideoLesson.setMediaController(mMediaController);
        mVideoLesson.setVideoURI(Uri.parse(uri));
        new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				//super.run();
				while(prefacePosition <= stopposition){
					prefacePosition = mVideoLesson.getCurrentPosition();
				}
				mVideoLesson.pause();
			}
        	
        }.start();
        //mVideoLesson.start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mVideoLesson.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoLesson.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // mVideoLesson.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoLesson.stopPlayback();
 
		
		 File file = new File(uri);
         file.delete();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        Log.i("LessonPlayer", "onError");
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
    }
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
	 
		super.onConfigurationChanged(newConfig);
	}
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
	       /*if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {       

	           mWebView.goBack();       
	           
	           return true;       

	       }*/
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(LessonPlayer.this);
	
			builder.setMessage(R.string.view_exit_msg);
	
			builder.setTitle(R.string.title_dialog_alert);
	
			builder.setPositiveButton("确认",
					new android.content.DialogInterface.OnClickListener() {
	
						@Override
						public void onClick(DialogInterface dialog, int which) {
	
							dialog.dismiss();
	
							LessonPlayer.this.finish();
						}
	
					});
	
			builder.setNegativeButton("取消",
					new android.content.DialogInterface.OnClickListener() {
	
						@Override
						public void onClick(DialogInterface dialog, int which) {
	
							dialog.dismiss();
	
						}
	
					});
	
			builder.create().show();
		
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		mVideoLesson.stopPlayback();
		LessonPlayer.this.finish();
	}
}