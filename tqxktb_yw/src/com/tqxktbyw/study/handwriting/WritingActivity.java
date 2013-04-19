package com.tqxktbyw.study.handwriting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Date;

import com.tqxktbyw.study.EastStudy;

import android.app.Activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

public class WritingActivity extends Activity {

	protected static final Context context = null;
	static String path = EastStudy.DOWNLOAD_PATH + "/tempic";
	private DrawView dv;

	private static final int SELECT_IMAGE = 0x123;
	public Intent intent;
	public Bundle bud;
	public String imag_name;
	String file_path;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		intent = this.getIntent();
		bud = intent.getExtras(); /* 取得Bundle对象中的数据 */
		String background = bud.getString("background");
		if (background.equals("从图库中选择")) {
			try {
				Intent getImage = new Intent(Intent.ACTION_PICK,
						MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				startActivityForResult(getImage, SELECT_IMAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (background.equals("默认背景")) {
			dv = new DrawView(this);
			dv.setBackgroundColor(0xffffffff);
			setContentView(dv);
			Toast.makeText(WritingActivity.this, "按menu键可保存",
					Toast.LENGTH_SHORT).show();

			
		}
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data1) {
		// TODO Auto-generated method stub
if(data1!=null){
		Uri path = data1.getData();
		ContentResolver resolver = getContentResolver();
		Bitmap cameraBitmap;
		try {
			cameraBitmap = MediaStore.Images.Media.getBitmap(resolver, path);
			BitmapDrawable bd = new BitmapDrawable(cameraBitmap);
			dv = new DrawView(this);
			dv.setBackgroundDrawable(bd);

			setContentView(dv);
			Toast.makeText(WritingActivity.this, "按menu键可保存",
					Toast.LENGTH_SHORT).show();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}else{
	
	WritingActivity.this.setResult(10, intent);
	WritingActivity.this.finish();
}
		super.onActivityResult(requestCode, resultCode, data1);
	}



	private static final int SAVE_ID = Menu.FIRST;
	private static final int OPEN_ID = Menu.FIRST + 1;
	private static final int LOCK_ID = Menu.FIRST + 2;
	private static final int DELETE_ID = Menu.FIRST + 3;
	private static final int PULL_ID = Menu.FIRST + 4;

	@Override
	// 添加Menu菜单
	public boolean onCreateOptionsMenu(Menu menu) {

		super.onCreateOptionsMenu(menu);
		menu.add(0, SAVE_ID, 0, "保存").setShortcut('3', 'c');
		menu.add(0, OPEN_ID, 0, "图库").setShortcut('4', 's');
		menu.add(0, LOCK_ID, 0, "锁定").setShortcut('5', 'z');
		menu.add(0, DELETE_ID, 0, "删除").setShortcut('5', 'z');
		menu.add(0, PULL_ID, 0, "拖拽").setShortcut('5', 'z');
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	// 点击Menu菜单事件
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case SAVE_ID:
			Date dt = new Date();
			Long time = dt.getTime();// 这就是距离1970年1月1日0点0分0秒的毫秒数
			String timeString = String.valueOf(time);
			imag_name = timeString;
			shootView(dv, timeString + ".png");
			bud.putString("filepath", path);
			bud.putString("filename", timeString);
			intent.putExtras(bud);
			WritingActivity.this.setResult(6, intent);
			WritingActivity.this.finish();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public static void shootView(View view, String path) {
		try {
			savePic(mScreenCapture(view), path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 保存图片
	private static void savePic(Bitmap b, String strFileName)
			throws IOException {

		File f = new File(path);
		//f.createNewFile();
		if (!f.exists()) {
            f.mkdir();
        }
		File file2 = new File(f, strFileName);
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		b.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 截取手写view
	private static Bitmap mScreenCapture(View view) {

		view.setDrawingCacheEnabled(true);// 画布隐藏

		view.buildDrawingCache(); // 建一个画布

		Bitmap b1 = view.getDrawingCache(); // 获取绘画的位图

		int width = view.getWidth();
		int height = view.getHeight();

		Bitmap b = Bitmap.createBitmap(b1, 0, 0, width, height); // 返回一个位图（source，int

		view.destroyDrawingCache(); // 释放绘图缓存
		return b;
	}
}