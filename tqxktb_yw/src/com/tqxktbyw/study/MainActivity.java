package com.tqxktbyw.study;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.CursorAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tqxktbyw.study.DownloadManager.OnDownloadStatusListener;
import com.tqxktbyw.study.EastStudy.BaseColumns;
import com.tqxktbyw.study.EastStudy.Course;
import com.tqxktbyw.study.EastStudy.Grade;
import com.tqxktbyw.study.EastStudy.Join;
import com.tqxktbyw.study.EastStudy.Lesson;
import com.tqxktbyw.study.EastStudy.StudyMethodInfo;
import com.tqxktbyw.study.EastStudy.Teacher;
import com.tqxktbyw.study.EastStudy.UserInfo;
import com.tqxktbyw.study.EastStudy.Version;
import com.tqxktbyw.study.database.EastStudyDatabase;
import com.tqxktbyw.study.entity.BaseInfo;
import com.tqxktbyw.study.entity.ConstUtiles;
import com.tqxktbyw.study.entity.CourseJoin;
import com.tqxktbyw.study.entity.GradeSubjectInfos;
import com.tqxktbyw.study.entity.JsonUtiles;
import com.tqxktbyw.study.entity.LessonCheckInfo;
import com.tqxktbyw.study.entity.LoginInfos;
import com.tqxktbyw.study.entity.LoginXmlFileInfo;
import com.tqxktbyw.study.entity.MyConnector;
import com.tqxktbyw.study.entity.MyLessonInfo;
import com.tqxktbyw.study.entity.SMPic;
import com.tqxktbyw.study.entity.StudyMethod;
import com.tqxktbyw.study.entity.StudyMethodDetail;
import com.tqxktbyw.study.entity.UserLogInfo;
import com.tqxktbyw.study.entity.VerInfo;
import com.tqxktbyw.study.packer.ListFile;
import com.tqxktbyw.study.provider.Downloads;

public class MainActivity extends ExpandableListActivity implements OnClickListener, OnDownloadStatusListener
{
	private static final String TAG = "tqxktbywActivity";
	private static final String PREFERENCES_NAME = "tqxktbyw";
	private static final String SAVE_PATH = "savePath";
	// private static final String AUTO_ORIENTATION = "autoOrientation";
	private static final String SAVE_DOWNLOAD = "saveDownload";
	private static final String DOWNLOAD_CONFIRM = "downloadConfirm";
	private static final String SELECTED_SITE = "selectedSite";
	private static final String CBGID = "currentbrowsergid";

	private static final String LOGIN_URL = "http://pad2.tqpad.com/login/login_subject.php";
	private static final String APK_PATH = "/mnt/sdcard/tqapk";
	private static final int APP_ID = 4;
	private static final String ENCODE = "GBK";
	private static final int SID = 1;
	private boolean daoru;
	private String randSerialNo = "";
	private String mvLessonUrl = "";
	private String mzLessonUrl = "";
	private String mpLessonUrl = "";

	private static final int MAX_THREAD_COUNT = 5;

	private static final int DOWNLOAD_DIALOG_ID = 1;
	private static final int NETWORK_DIALOG_ID = 2;
	private static final int SITE_SELECT_DIALOG_ID = 3;
	private static final int LOGIN_OUT_DIALOG_ID = 4;
	private boolean bLoginFail = false;
	 List<StudyMethodDetail> listStudy2 = null ;
	private DownloadInfo mDownloadInfo;
	private OtherFunctionsInfo mOtherFunctionsInfo;

	private static  EastStudyDatabase mDatabase;
	private SharedPreferences mSharedPreferences;
	// private MultiExpandableView mMultiExpandableView;
	// private VideoDec vd;

	// private String mWifiMacAddress = "0092C3A779C4";
	private String mWifiMacAddress = "";
	private boolean mNetworkAvailability;
	private DownloadParam mDownloadParam = new DownloadParam();

	private UserLogInfo uli = new UserLogInfo();
	long ct = (new Date()).getTime();
	// private VerifyInfo vi;
	private LoginInfos jis;
	GradeSubjectInfos mycourseright;
	List<MyLessonInfo> mylessonright;
	List<Integer> li;
	int uid = 0;
	// private boolean canPlay = false;
	HashMap<Long, Boolean> playRightList = new HashMap<Long, Boolean>();

	// LessonCheckInfo lci;
	private ExpandableListView mListView;
	private ExpandListViewAdapter mAdapter;
	private ListView mCourseLessonList;
	private View mCourseHoriRoot;
	MyCourseLessonAdapter courseLessonAdapter;
	static long current_gid;
	LessonCheckInfo localTeacher;
	List<StudyMethod> localListStudy = null;
	
	
	private Button mEdit;
	private Button mComplete;
	private Button mSetting;
	private boolean mEditStatus;
	private ProgressDialog pd;
	Resources res;

	private int mSelectedSite;
	private int mSelectedGrade;
	private int pSelectedGrade;
	private int pSelectedChild;
	private String checkurl = "";

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		//界面初始化
		super.onCreate(savedInstanceState);
		Window win = getWindow();
		win.setBackgroundDrawableResource(R.drawable.background);
		win.requestFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.main);
		win.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.logo);
		//测试网络
		mNetworkAvailability = isNetworkAvailable();
		//获取系统资源
		res = getResources();
		//初始化数据库
		mDatabase = new EastStudyDatabase(this);
		//初始化按钮
		mEdit = (Button) this.findViewById(R.id.btn_editor);
		mComplete = (Button) this.findViewById(R.id.btn_complete);
		mSetting = (Button) this.findViewById(R.id.btn_setting);
		//设置按钮监听
		mSetting.setOnClickListener(this);
		mComplete.setOnClickListener(this);
		mEdit.setOnClickListener(this);
		mEditStatus = false;//状态位，用于显示完成还是编辑
		initVersionInfo(uli);
		int nLoginRet = login();
		init(nLoginRet);

	}

	private void init(int nLoginRet)
	{
		if (nLoginRet == 0)//登录失败，非法用户
		{
			this.showDialog(LOGIN_OUT_DIALOG_ID);//退出
			bLoginFail = true;
		} else if (nLoginRet != 3)//12
		{

			mDownloadInfo = new DownloadInfo(res, MAX_THREAD_COUNT);
			mOtherFunctionsInfo = new OtherFunctionsInfo(res);
			courseLessonAdapter = new MyCourseLessonAdapter(this, null);

			mCourseHoriRoot = findViewById(R.id.my_course_hori);

			mCourseLessonList = (ListView) mCourseHoriRoot.findViewById(R.id.course_list_lesson);
			mCourseLessonList.setAdapter(courseLessonAdapter);
			loadSharedPreferences();

			mListView = this.getExpandableListView();

			mListView.setOnGroupExpandListener(new OnGroupExpandListener()
			{

				@Override
				public void onGroupExpand(int groupPosition)
				{
					// TODO Auto-generated method stub
					for (int i = 0; i < mListView.getCount(); i++)
					{
						if (groupPosition != i)
						{
							mListView.collapseGroup(i);
						}
					}
				}

			});
			// mListView.setGroupIndicator(this.getResources().getDrawable(R.drawable.icon));
			// vd = new VideoDec(this);

			// if (mNetworkAvailability)
			new UpgradeTask(mNetworkAvailability).execute("");

			// mTelphoneIMEI = getTelphoneIMEI();
			// mWifiMacAddress = getLocalMacAddress();
			registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
			continueDown();
			DeleteApk();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			ShowExitdialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void DeleteApk()
	{
		File file = new File(APK_PATH);
		if (file.exists())
		{
			File[] ff = file.listFiles();
			if (ff.length > 0)
			{
				for (File f : ff)
				{
					f.delete();
				}
			}
		}
		File directory = new File(EastStudy.DOWNLOAD_PATH);
		if (!directory.exists())
		{
			directory.mkdir();
		}
		File nomedia = new File(EastStudy.DOWNLOAD_PATH + File.separator + ".nomedia");
		if (!nomedia.exists())
		{
			try
			{
				nomedia.createNewFile();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void continueDown()
	{
		if (isNetworkAvailable())
		{
			Cursor ecs = this.getContentResolver().query(Downloads.Execute.CONTENT_URI, Downloads.DOWNLOADING_COLUMNS, null, null, null);

			if (ecs.moveToFirst())
			{
				do
				{
					if (ecs.getString(2).startsWith("http://"))
					{
						mDownloadInfo.mDownloadManager.execute(ecs.getLong(0), ecs.getInt(1));
					} else
					{
						this.getContentResolver().delete(Downloads.Execute.CONTENT_URI, " lid=" + ecs.getLong(0) + " and isv=" + ecs.getInt(1), null);
					}
				} while (ecs.moveToNext());

			}
			ecs.close();
		}
	}

	public int login()
	{
		int bRet = 1;

		if (!mNetworkAvailability)//网络不存在返回2
			return 2;
		try
		{

			String macno;
			macno = getLocalMacAddress();//获取mac地址
			if (macno != null)
				mWifiMacAddress = macno.replace(":", "");//格式化

			Map<String, String> params = new HashMap<String, String>();
			params.put("tq_mac", mWifiMacAddress);
			params.put("tq_ver", APP_ID + "");
			params.put("tq_sid", SID + "");

			String json = new String(MyConnector.postFromHttpClient(LOGIN_URL, params, ENCODE), ENCODE);
			jis = JsonUtiles.InitLoginInfos(json);
			if (jis != null && jis.isSuccess())
			{
				final VerInfo verinfos = jis.get_Version();
				UserLogInfo uinfo = jis.get_User();
				uinfo.set_checkurl(jis.get_checkURL());
				uid = uinfo.get_usrID();
				randSerialNo = uinfo.get_randNum();
				if (uli.get_usrID() < 1)
				{
					uli.set_usrID(uid);
				} else
				{
					if (uli.get_usrID() != uid)
					{
						finish();
					}
				}
				if (uinfo.get_randNum() != null)
				{
					uli.set_randNum(uinfo.get_randNum());
				}
				if (uinfo != null)
				{
					UpdateUserTable(uinfo);
				}
				if (APP_ID < verinfos.get_UpdateNum())//更新版本
				{
					bRet = 3;
					AlertDialog.Builder pBar = new AlertDialog.Builder(MainActivity.this);
					pBar.setTitle(R.string.upgrade_title);
					pBar.setMessage(R.string.upgrade_mesg);
					if (!verinfos.is_UpdateMast() && (verinfos.get_UpdateNum() - APP_ID) < 2)
					{
						pBar.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
								// DgktActivity.this.finish();
								init(1);
							}
						});
					}

					pBar.setPositiveButton(this.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							bLoginFail = true;
							// UpdateSoft( vi.get_ver().get_UpdateUrl());
							UpdateSoft(verinfos.get_UpdateUrl());
						}
					});
					AlertDialog adl = pBar.create();
					adl.setCanceledOnTouchOutside(false);
					adl.show();

					adl.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
					adl.setOnKeyListener(new OnKeyListener()
					{

						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
						{
							// TODO Auto-generated method stub
							// 仅屏蔽back键
							if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)
							{
								return true;
							}
							return false;
						}
					});
				}
			} else
				bRet = 0;

		} catch (Exception e)
		{
			bRet = 2;
			Log.e(TAG, "Can not upgrade tables1", e);
		}
		return bRet;
	}

	private void UpdateUserTable(UserLogInfo uinfo)
	{
		// TODO Auto-generated method stub
		SQLiteDatabase db = mDatabase.getWritableDatabase();

		try
		{
			ContentValues values = new ContentValues();
			db.beginTransaction();
			values.put(UserInfo.COLUMN_ID, uinfo.get_usrID());
			values.put(UserInfo.COLUMN_NAME, uinfo.get_randNum());
			values.put(UserInfo.COLUMN_URL, uinfo.get_checkurl());
			db.replace(UserInfo.TABLE_NAME, null, values);
			db.setTransactionSuccessful();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			db.endTransaction();
		}

	}

	private void loadSharedPreferences()
	{
		mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);

		mOtherFunctionsInfo.loadSharedPreferences();

		setOrientation();
	}

	private void setOrientation()
	{
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	private void saveSharedPreferences()
	{
		Editor editor = mSharedPreferences.edit();
		mOtherFunctionsInfo.saveSharedPreferences(editor);

		editor.commit();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		if (!bLoginFail)
		{
			unregisterReceiver(mNetworkReceiver);
			saveSharedPreferences();
		}
		if (mDownloadInfo != null)
		{
			mDownloadInfo.clear(5000);
		}
		if (mDatabase != null)
		{
			mDatabase.close();
		}
	}

	private class OnDeleteListener implements DialogInterface.OnClickListener
	{
		private long id;
		private String filename;

		public void setParam(long id, String filename)
		{
			this.id = id;
			this.filename = filename;
		}

		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			dialog.dismiss();
			// 删除已或正在下载时，下载按钮处理
			mDownloadInfo.mDownloadManager.cancel(id);

			Log.i(TAG, "id = " + id);
			ContentResolver resolver = getContentResolver();
			resolver.delete(Downloads.Execute.CONTENT_URI, Downloads.LID + '=' + id, null);
			resolver.delete(Downloads.Success.CONTENT_URI, Downloads.LID + '=' + id, null);

			onStateChanged(id, EastStudy.STATUS_PENDING);
			// mMyCourseInfo.init(mDatabase.query(Course.TABLE_NAME,
			// Course.COLUMNS, EastStudy.MYCOURSE_WHERE, null));
			String fdir = filename.substring(0, filename.lastIndexOf("."));
			deleteDir(fdir);
			try
			{
				File file = new File(fdir + ".amt");
				// System.out.println("delete filename:"+filename);
				if (file.exists())
				{
					file.delete();
				}
			} catch (Exception ex)
			{
				System.out.println("delete filename:" + fdir + ".amt");
				ex.printStackTrace();
			}

		}
	}

	/**
	 * 删除一个目录,包括其所有子目录及文件
	 * 
	 * @param path
	 *            要删除的路径
	 * */
	private boolean deleteDir(String path)
	{
		boolean success = true;
		File file = new File(path);
		if (file.exists())
		{
			File[] list = file.listFiles();
			if (list != null)
			{
				int len = list.length;
				for (int i = 0; i < len; ++i)
				{
					if (list[i].isDirectory())
					{
						deleteDir(list[i].getPath());
					} else
					{
						boolean ret = list[i].delete();
						if (!ret)
						{
							success = false;
						}
					}
				}
			}
		} else
		{
			success = false;
		}
		if (success)
		{
			file.delete();
		} else
		{
		}
		return success;
	}

	private final OnDeleteListener mDeleteListener = new OnDeleteListener();
	

	private void deleteLesson(long id, String filename)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.title_delete_confirm);
		builder.setMessage(R.string.message_delete_confirm);
		final Resources res = getResources();

		mDeleteListener.setParam(id, filename);
		builder.setPositiveButton(res.getString(android.R.string.ok), mDeleteListener);

		builder.setNegativeButton(res.getString(android.R.string.cancel), new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		}).create().show();
	}

	private void playingLesson(long _lid)
	{

		// new DecryptTask().execute(uri);
		Intent intent = new Intent(this, StudyWebView.class);

		intent.putExtra(EastStudy.PLAYING_URI, _lid);
		// intent.putExtra(EastStudy.UserInfo.COLUMN_NAME, uli);
		intent.putExtra(EastStudy.StudyMethodDetail.GID, this.current_gid);
		intent.putExtra(EastStudy.Subject.COLUMN_ID, SID);
		startActivity(intent);

	}

	private class MyCourseLessonAdapter extends CursorAdapter implements LessonQuery, OnClickListener
	{
		public MyCourseLessonAdapter(Context context, Cursor cursor)
		{
			super(context, cursor, true);
		}

		@Override
		public View getView(int position, View view, ViewGroup parent)
		{
			final Cursor cursor = (Cursor) getItem(position);
			if (view == null)
			{
				view = newView(MainActivity.this, cursor, parent);
			}

			bindNewView(view, position, cursor);
			return view;
		}

		private void bindNewView(View view, int position, Cursor cursor)
		{
			Button download = (Button) view.findViewById(R.id.hori_lesson_download);//下载按钮
			download.setTag(position);
			download.setOnClickListener(this);

			Button playLesson = (Button) view.findViewById(R.id.hori_lesson_play);//播放按钮
			playLesson.setTag(position);
			playLesson.setOnClickListener(this);
			// long id = cursor.getLong(Lesson.COLUMN_ID_INDEX);
			ProgressBar progressbar = (ProgressBar) view.findViewById(R.id.download_progress);//进度条
			progressbar.setTag(position);
			progressbar.setVisibility(View.GONE);
			Button downloading = (Button) view.findViewById(R.id.hori_lesson_downloading);//下载中按钮
			downloading.setTag(position);
			downloading.setVisibility(View.GONE);

			View deleteLession = view.findViewById(R.id.hori_lesson_delete);//删除按钮
			deleteLession.setTag(position);
			deleteLession.setOnClickListener(this);
			deleteLession.setVisibility(View.GONE);

			switch (cursor.getInt(Lesson.COLUMN_STATUS_INDEX))
			{
			case EastStudy.STATUS_PAUSED://下载中
				playLesson.setVisibility(View.GONE);
				if (mEditStatus)
				{
					deleteLession.setVisibility(View.VISIBLE);
				} else
				{
					download.setVisibility(View.VISIBLE);
				}
				Cursor cc = getContentResolver().query(Downloads.Execute.CONTENT_URI, Downloads.DOWNLOAD_MANAGER_COLUMNS, Downloads.LID + '=' + cursor.getLong(Lesson.COLUMN_ID_INDEX) + " AND isv =0", null, null);

				if (cc.moveToFirst())
				{
					int tsize = cc.getInt(Downloads.DOWNLOAD_MANAGER_COLUMN_TOTAL_SIZE_INDEX);
					int dsize = cc.getInt(Downloads.DOWNLOAD_MANAGER_COLUMN_DOWNLOADED_SIZE_INDEX);

					if (tsize > 0)
					{
						progressbar.setMax(tsize);
						progressbar.setProgress(dsize);
						progressbar.setVisibility(View.VISIBLE);
					}
				}
				cc.close();
				break;
			case EastStudy.STATUS_PENDING:// 初始
				playLesson.setVisibility(View.GONE);
				download.setEnabled(!mEditStatus);
				download.setClickable(!mEditStatus);
				download.setVisibility(View.VISIBLE);

				break;

			case EastStudy.STATUS_FAILED:// 失败
				if (mEditStatus)
				{
					deleteLession.setVisibility(View.VISIBLE);
					playLesson.setVisibility(View.GONE);
					download.setVisibility(View.GONE);
				} else
				{
					playLesson.setVisibility(View.GONE);
					download.setEnabled(true);
					download.setVisibility(View.VISIBLE);
				}
				break;

			case EastStudy.STATUS_RUNNING://下载中
				if (mEditStatus)
				{
					deleteLession.setVisibility(View.VISIBLE);
					playLesson.setVisibility(View.GONE);
					download.setVisibility(View.GONE);
				} else
				{
					playLesson.setVisibility(View.GONE);
					downloading.setVisibility(View.VISIBLE);
					playLesson.setVisibility(View.GONE);
					download.setVisibility(View.GONE);
					Cursor c = getContentResolver().query(Downloads.Execute.CONTENT_URI, Downloads.DOWNLOAD_MANAGER_COLUMNS, Downloads.LID + '=' + cursor.getLong(Lesson.COLUMN_ID_INDEX) + " AND isv =0", null, null);

					if (c.moveToFirst())
					{
						int tsize = c.getInt(Downloads.DOWNLOAD_MANAGER_COLUMN_TOTAL_SIZE_INDEX);
						int dsize = c.getInt(Downloads.DOWNLOAD_MANAGER_COLUMN_DOWNLOADED_SIZE_INDEX);

						if (tsize > 0)
						{
							progressbar.setMax(tsize);
							progressbar.setProgress(dsize);
							progressbar.setVisibility(View.VISIBLE);
						}
					}
					c.close();
				}
				break;

			case EastStudy.STATUS_SUCCESSFUL://可以播放
				if (mEditStatus)
				{
					playLesson.setVisibility(View.GONE);
					download.setEnabled(false);
					download.setVisibility(View.GONE);
					deleteLession.setVisibility(View.VISIBLE);
				} else
				{
					playLesson.setVisibility(View.VISIBLE);//播放按钮
					download.setEnabled(false);
					download.setVisibility(View.GONE);
					deleteLession.setVisibility(View.GONE);
				}
				break;
			}

			((TextView) view.findViewById(R.id.hori_lesson_text)).setText(cursor.getString(Lesson.COLUMN_NAME_INDEX));

		}

		@Override
		public void bindView(View view, Context context, Cursor cursor)
		{

		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			return LayoutInflater.from(context).inflate(R.layout.my_course_lesson_item, null);
		}

		@Override
		public Cursor query(long courseId)
		{
			Log.i(TAG, "courseId = " + courseId);
			// return getContentResolver().query(Downloads.Success.CONTENT_URI,
			// Downloads.DOWNLOAD_COLUMNS, Downloads.COURSE_ID + '=' + courseId,
			// null, null);
			return mDatabase.query(Lesson.TABLE_NAME, Lesson.COLUMNS, EastStudy.buildLessonWhere(courseId), null);
		}

		@Override
		public void onClick(View view)
		{
			final int position = (Integer) view.getTag();
			final Cursor cursor = (Cursor) getItem(position);
			switch (view.getId())
			{
			case R.id.hori_lesson_play:
				String url = cursor.getString(Lesson.COLUMN_URL_INDEX);
				String zip = cursor.getString(Lesson.COLUMN_ZIPNAME_INDEX);
				String turl = GetTurl(cursor.getLong(Lesson.COLUMN_TEACHER_INDEX));
				System.out.println("url = "+url);
				if (url.length() > 10 && turl.length() > 5 && zip.length() > 10)
				{
					/*
					 * playingLesson(cursor.getString(Lesson.COLUMN_URL_INDEX),
					 * cursor
					 * .getString(Lesson.COLUMN_ZIPNAME_INDEX),cursor.getInt
					 * (Lesson.COLUMN_TEACHER_INDEX));
					 */
					if (new File(url).exists() && new File(turl).exists() && new File(zip).exists())
					{
						playingLesson(cursor.getLong(Lesson.COLUMN_ID_INDEX));
					} else
					{
						Toast.makeText(MainActivity.this, R.string.lessonnoexist, Toast.LENGTH_LONG).show();
					}
				}
				break;

			case R.id.hori_lesson_delete:
				Cursor cs = getContentResolver().query(Downloads.Success.CONTENT_URI, Downloads.PLAY_COLUMNS, Downloads.LID + '=' + cursor.getLong(Lesson.COLUMN_ID_INDEX), null, null);

				if (cs.moveToFirst())
				{
					deleteLesson(cursor.getLong(Downloads.DOWNLOAD_COLUMN_ID_INDEX), cs.getString(Downloads.PLAY_COLUMN_FILENAME_INDEX));

				}
				cs.close();
				break;

			case R.id.hori_lesson_download:
				if (isNetworkAvailable())
				{
					if (MAX_THREAD_COUNT == mDownloadInfo.mDownloadManager.getThreadCount())
					{
						Toast.makeText(MainActivity.this, res.getString(R.string.seque), Toast.LENGTH_LONG).show();
					}
					Cursor ecs = getContentResolver().query(Downloads.Execute.CONTENT_URI, Downloads.DOWNLOADING_COLUMNS, Downloads.LID + '=' + cursor.getLong(Lesson.COLUMN_ID_INDEX), null, null);

					if (ecs.moveToFirst())
					{
						do
						{
							mDownloadInfo.mDownloadManager.execute(ecs.getLong(0), ecs.getInt(1));
						} while (ecs.moveToNext());

					} else
					{
						boolean bRet = GetActualUrl(cursor.getInt(Lesson.COLUMN_ID_INDEX), cursor.getInt(Lesson.COLUMN_COURSE_ID_INDEX), cursor.getString(Lesson.COLUMN_URL_INDEX));
						if (bRet)
						{
							mDownloadParam.set(view, cursor.getLong(Lesson.COLUMN_ID_INDEX), mzLessonUrl, mvLessonUrl, mpLessonUrl, cursor.getString(Lesson.COLUMN_NAME_INDEX), cursor.getInt(Lesson.COLUMN_TYPE_INDEX), cursor.getInt(Lesson.COLUMN_SORT_ORDER_INDEX), cursor.getLong(Lesson.COLUMN_COURSE_ID_INDEX), cursor.getInt(Lesson.COLUMN_TEACHER_INDEX), localTeacher.get_tver());
							mDownloadInfo.download();

						} else
						{
							// 处理登陆失败
							final Resources res = MainActivity.this.getResources();
							AlertDialog.Builder lgbuilder = new AlertDialog.Builder(MainActivity.this);
							lgbuilder.setTitle(R.string.ERROR_TIP);
							lgbuilder.setMessage("连接课件服务器失败！请确认网络已连接并重启本程序后重试！");
							// lgbuilder.setMessage(mUserLicense+"Mac:"+mWifiMacAddress);

							lgbuilder.setPositiveButton(res.getString(android.R.string.ok), new DialogInterface.OnClickListener()
							{
								@Override
								public void onClick(DialogInterface dialog, int which)
								{
									dialog.dismiss();
								}
							}).create().show();
						}
					}
					ecs.close();

				} else
				{
					showDialog(NETWORK_DIALOG_ID, null);
				}
				break;
			}
		}
	}

	private String GetTurl(long tid)
	{
		String str;
		Cursor cur = mDatabase.rawQuery("select " + EastStudy.Teacher.URL + " from " + EastStudy.Teacher.TABLE_NAME + " where " + EastStudy.Teacher.ID + " =" + tid, null);
		if (cur.moveToFirst())
		{
			str = cur.getString(Teacher.ID_INDEX);

		} else
			str = null;
		cur.close();
		return str;
	}

	private void initVersionInfo(UserLogInfo lui)
	{
		Cursor cs = mDatabase.rawQuery("select * from " + Version.TABLE_NAME, null);
		if (cs.getCount() > 1)
		{
			do
			{
				if (cs.getString(Version.COLUMN_VERSIONNAME_INDEX).equalsIgnoreCase("mver"))
				{
					lui.set_cver(cs.getInt(Version.COLUMN_NUMBER_INDEX));
				} else
				{
					lui.set_lver(cs.getInt(Version.COLUMN_NUMBER_INDEX));
				}
			} while (cs.moveToNext());
		}
		cs.close();
	}

	private class UpgradeTask extends AsyncTask<String, Long, Cursor[]>
	{
		private ProgressDialog mProgressDialog;
		private final boolean mNetworkAvailable;
		private boolean bGetRet = true;
		private String ErrorMsg = "";

		public UpgradeTask(boolean networkAvailable)
		{
			mNetworkAvailable = networkAvailable;
		}

		@Override
		protected void onPostExecute(Cursor[] cursors)
		{
			if (mNetworkAvailable)
			{
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
			if (bGetRet && cursors[0] != null && cursors[0].getCount() > 0)
			{
				try
				{

					// cursors[0].getColumnIndexOrThrow(Grade.COLUMN_ID);
					mAdapter = new ExpandListViewAdapter(cursors[0], MainActivity.this, R.layout.main_test_group, R.layout.main_test_child, new String[]
					{ Grade.COLUMN_NAME },// Name for group layouts
							new int[]
							{ R.id.btn_group }, new String[]
							{ Version.COLUMN_VERSIONNAME },// Name for child
															// layouts
							new int[]
							{ R.id.childText });
					mListView.setAdapter(mAdapter);
					mListView.setOnChildClickListener(mAdapter.onclicklister);
					mListView.expandGroup(pSelectedGrade);
					((CursorAdapter) mCourseLessonList.getAdapter()).changeCursor(mAdapter.query(mAdapter.getChildId(pSelectedGrade, pSelectedChild)));
				} catch (Exception ex)
				{
					// System.out.println(ex.getMessage());
					ex.printStackTrace();
				}
			} else
			{
				// 处理失败信息
				final Resources res = MainActivity.this.getResources();
				AlertDialog.Builder lgbuilder = new AlertDialog.Builder(MainActivity.this);
				lgbuilder.setTitle(R.string.ERROR_TIP);
				lgbuilder.setMessage(ErrorMsg);

				lgbuilder.setPositiveButton(res.getString(android.R.string.ok), new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				}).create().show();
			}
		}

		@Override
		protected void onPreExecute()
		{
			if (mNetworkAvailable)
			{
				Resources res = getResources();
				mProgressDialog = ProgressDialog.show(MainActivity.this, res.getString(R.string.upgrade_title), res.getString(R.string.upgrade_message), true, false);
			}
		}

		@Override
		protected Cursor[] doInBackground(String... params)
		{
			if (mNetworkAvailable && jis != null)
			{
				try
				{
					Map<String, String> params_CourseRights = new HashMap<String, String>();
					params_CourseRights.put("mx_uid", uli.get_usrID() + "");
					params_CourseRights.put("mx_rand", uli.get_randNum());

					LoginXmlFileInfo lxfile = jis.get_FirstxmlFile();

					if (uli.get_cver() < lxfile.get_cver())
					{
						String ss = lxfile.get_cFile();
						String allcourseList = new String(MyConnector.getByte(ss), ENCODE);
						mycourseright = JsonUtiles.Get_GradeSubjectInfos(allcourseList);
						if (mycourseright != null)
						{
							updateTable(mycourseright);
							updateUriTable();
						} else
						{
							bGetRet = false;

							ErrorMsg = "课程数据加载失败！1";
						}
					}
					if (uli.get_lver() < lxfile.get_lver())
					{
						String allLessonList = new String(MyConnector.getByte(lxfile.get_lFile()), ENCODE);
						mylessonright = JsonUtiles.GetLessonInfo(allLessonList);
						if (mylessonright != null)
						{
							updateLessonTable(mylessonright);
						} else
						{
							bGetRet = false;

							ErrorMsg = "课件数据加载失败！2";
						}
					}

				} catch (Exception e)
				{
					Log.e(TAG, "Can not upgrade tables2", e);
				}
			}
			Cursor[] cursors = new Cursor[4];
			if (bGetRet)
			{
				cursors[0] = queryCourse(true);
			}
			return cursors;
		}

		private Cursor queryCourse(boolean flags)
		{
			/*
			 * String tableName = Course.TABLE_NAME; String where = null;
			 * 
			 * if (flags) { tableName = Grade.TABLE_NAME; //where =
			 * EastStudy.MYCOURSE_WHERE; where = null; }
			 */
			// return mDatabase.query(tableName, Grade.COLUMNS, where, null);
			if (mSelectedGrade > 0)
			{
				return managedQuery(Downloads.ExpandableList.CONTENT_URI, BaseColumns.COLUMNS, BaseColumns.COLUMN_ID + "=" + mSelectedGrade, null, BaseColumns.COLUMN_ID + " asc ");
			} else
			{
				return managedQuery(Downloads.ExpandableList.CONTENT_URI, BaseColumns.COLUMNS, null, null, BaseColumns.COLUMN_ID + " asc ");
			}
		}

		private void updateUriTable()
		{
			// String sqls =
			// "select "+Join.TABLE_NAME+".*,"+Course.TABLE_NAME+".name from " +
			// Join.TABLE_NAME +" left join "+Course.TABLE_NAME
			// +" on "+Course.TABLE_NAME+"."+Course.COLUMN_ID+"="+Join.TABLE_NAME+"."+Join.COLUMN_VERSION_ID+"";

			Cursor csg = mDatabase.query(Grade.TABLE_NAME, Grade.COLUMNS, null, null);
			// SQLiteDatabase db = mDatabase.getWritableDatabase();
			ContentResolver resolver = getContentResolver();
			ContentValues values = new ContentValues();
			if (csg.moveToFirst())
			{
				// resolver.
				// db.beginTransaction();
				do
				{
					long cid = csg.getLong(BaseColumns.COLUMN_ID_INDEX);
					// Uri newUri =
					// ContentUris.withAppendedId(Downloads.ExpandableList.CONTENT_URI,
					// cid);
					Uri newUri = Downloads.ExpandableList.CONTENT_URI;
					values.put(BaseColumns.COLUMN_ID, cid + "");
					values.put(BaseColumns.COLUMN_NAME, csg.getString(BaseColumns.COLUMN_NAME_INDEX));
					values.put(BaseColumns.COLUMN_SORT_ORDER, csg.getString(BaseColumns.COLUMN_SORT_ORDER_INDEX));
					// db.replace(Join.TABLE_NAME, null, values);
					Cursor cursor = resolver.query(newUri, BaseColumns.COLUMNS, BaseColumns.COLUMN_ID + " = " + csg.getString(BaseColumns.COLUMN_ID_INDEX), null, null);
					if (cursor.moveToFirst())
					{
						resolver.update(newUri, values, BaseColumns.COLUMN_ID + " = " + csg.getString(BaseColumns.COLUMN_ID_INDEX), null);
					} else
					{
						resolver.insert(newUri, values);
					}
					cursor.close();
				} while (csg.moveToNext());
				// db.setTransactionSuccessful();
			}
			csg.close();
			values.clear();

			// int ii = cs.getCount();
			Cursor cs = mDatabase.rawQuery("select " + Join.COLUMN_COURSE_ID + "," + Join.COLUMN_GRADE_ID + "," + Join.COLUMN_VERSION_ID + "," + Join.COLUMN_TITLE + " from " + Join.TABLE_NAME, null);
			if (cs.moveToFirst())
			{
				// resolver.
				// db.beginTransaction();
				do
				{
					long cid = cs.getLong(BaseColumns.COLUMN_ID_INDEX);
					// Uri newUri =
					// ContentUris.withAppendedId(Downloads.ExpandableList.CONTENT_URI,
					// cid);
					Uri newUri = Downloads.ExpandableList.CONTENT_ITEM_URI;
					values.put(BaseColumns.COLUMN_ID, cid + "");
					values.put(BaseColumns.COLUMN_NAME, cs.getString(3));
					values.put(BaseColumns.COLUMN_SORT_ORDER, cs.getString(1));
					// db.replace(Join.TABLE_NAME, null, values);
					Cursor cursor = resolver.query(newUri, BaseColumns.COLUMNS, BaseColumns.COLUMN_ID + " = " + cs.getString(BaseColumns.COLUMN_ID_INDEX), null, null);
					if (cursor.moveToFirst())
					{
						resolver.update(newUri, values, BaseColumns.COLUMN_ID + " = " + cs.getString(BaseColumns.COLUMN_ID_INDEX), null);
					} else
					{
						resolver.insert(newUri, values);
					}
					cursor.close();
				} while (cs.moveToNext());
				// db.setTransactionSuccessful();
			}
			cs.close();

		}

		/*
		 * private String prepareUrl() { Cursor cursor =
		 * mDatabase.rawQuery(Upgrade.UPGRADE_STATEMENT, null); long ids[] = new
		 * long[cursor.getCount()]; for (int i = 0; cursor.moveToNext(); ++i) {
		 * ids[i] = cursor.getLong(0); }
		 * 
		 * cursor.close(); //这里多增加两个请求参数 String str =
		 * String.format(Upgrade.UPGRADE_URL_FORMAT, mUserLicense,
		 * mWifiMacAddress,randSerialNo, ids[0], ids[1], ids[2], ids[3], ids[4],
		 * ids[5]); return str; }
		 */

		private void updateTable(GradeSubjectInfos allcourseinfo)
		{
			SQLiteDatabase db = mDatabase.getWritableDatabase();
			try
			{
				ContentValues values = new ContentValues();
				List<BaseInfo> bi_g = allcourseinfo.get_Grade();
				List<BaseInfo> bi_lesson = allcourseinfo.getVersion();
				List<CourseJoin> bi_coursejoin = allcourseinfo.getCourse();
				db.beginTransaction();
				for (int i = 0; i < bi_g.size(); i++)
				{
					BaseInfo acinfo = bi_g.get(i);
					values.put(Course.COLUMN_ID, acinfo.get_ID());
					values.put(Course.COLUMN_NAME, acinfo.get_Title());
					values.put(Course.COLUMN_SORT_ORDER, acinfo.get_ID());
					db.replace(Grade.TABLE_NAME, null, values);
				}
				values.clear();
				for (int i = 0; i < bi_lesson.size(); i++)
				{
					BaseInfo acinfo = bi_lesson.get(i);
					values.put(Course.COLUMN_ID, acinfo.get_ID());
					values.put(Course.COLUMN_NAME, acinfo.get_Title());
					db.replace(Course.TABLE_NAME, null, values);
				}
				values.clear();
				for (int i = 0; i < bi_coursejoin.size(); i++)
				{
					CourseJoin acinfo = bi_coursejoin.get(i);
					values.put(Join.COLUMN_COURSE_ID, acinfo.get_ID());
					values.put(Join.COLUMN_GRADE_ID, acinfo.get_GID());
					values.put(Join.COLUMN_VERSION_ID, acinfo.get_VID());
					values.put(Join.COLUMN_TITLE, acinfo.get_Title());
					db.replace(Join.TABLE_NAME, null, values);
				}
				db.setTransactionSuccessful();
			} finally
			{
				db.endTransaction();
			}
		}

		private void updateLessonTable(List<MyLessonInfo> mylessonright)
		{
			SQLiteDatabase db = mDatabase.getWritableDatabase();

			try
			{
				ContentValues values = new ContentValues();
				db.beginTransaction();
				for (int i = 0; i < mylessonright.size(); i++)
				{
					MyLessonInfo mli = mylessonright.get(i);
					values.put(BaseColumns.COLUMN_ID, mli.get_id());
					values.put(BaseColumns.COLUMN_NAME, mli.get_title());
					values.put(BaseColumns.COLUMN_SORT_ORDER, mli.get_id());

					values.put(Lesson.COLUMN_TYPE, 0);
					values.put(Lesson.COLUMN_COURSE_ID, mli.get_CID());

					Cursor cursor = db.rawQuery("SELECT " + BaseColumns.COLUMN_ID + "," + Lesson.COLUMN_STATUS + "," + Lesson.COLUMN_URL + "," + Lesson.COLUMN_ZIPNAME + " FROM " + Lesson.TABLE_NAME + " WHERE " + BaseColumns.COLUMN_ID + '=' + mli.get_id(), null);
					if (cursor.moveToFirst())
					{
						values.put(Lesson.COLUMN_STATUS, cursor.getInt(1));
						values.put(Lesson.COLUMN_URL, cursor.getString(2));
						values.put(Lesson.COLUMN_ZIPNAME, cursor.getString(3));
					} else
					{
						// 处理URL地址转换功能
						values.put(Lesson.COLUMN_URL, "");
						values.put(Lesson.COLUMN_STATUS, EastStudy.STATUS_PENDING);
						values.put(Lesson.COLUMN_ZIPNAME, "");
					}
					cursor.close();
					values.put(Lesson.COLUMN_TID, mli.get_TID());
					values.put(Lesson.COLUMN_CS, mli.is_CS() ? 1 : 0);
					values.put(Lesson.COLUMN_ZS, mli.is_ZS() ? 1 : 0);
					values.put(Lesson.COLUMN_KS, mli.is_KS() ? 1 : 0);
					values.put(Lesson.COLUMN_TS, mli.is_TS() ? 1 : 0);
					db.replace(Lesson.TABLE_NAME, null, values);
				}
				db.setTransactionSuccessful();
			} catch (Exception e)
			{
				e.printStackTrace();
			} finally
			{
				db.endTransaction();
			}
		}
	}

	public final void disableDownButton(View view)
	{
		Button download = (Button) view.findViewById(R.id.hori_lesson_download);

		download.setEnabled(false);
	}

	private boolean GetActualUrl(int lid, int cid, String LUrl)
	{
		boolean bRet = true;
		InputStream stream = null;
		HttpURLConnection conn = null;

		try
		{
			// 电信-1,网通-2取值上要加一
			int aid = mSelectedSite + 1;
			Map<String, String> params_LessonCheck = new HashMap<String, String>();
			params_LessonCheck.put("tq_uid", uli.get_usrID() + "");
			params_LessonCheck.put("tq_rand", uli.get_randNum());
			params_LessonCheck.put("tq_gid", current_gid + "");
			params_LessonCheck.put("tq_sid", SID + "");
			params_LessonCheck.put("tq_cid", cid + "");
			params_LessonCheck.put("tq_lid", lid + "");
			params_LessonCheck.put("tq_azid", aid + "");
			System.out.println(uli.get_usrID() + "," + uli.get_randNum() + "," + current_gid + "," + SID + "," + cid + "," + lid + "," + aid + ",");
			String lessonUrl;
			lessonUrl = new String(MyConnector.postFromHttpClient(jis.get_checkURL(), params_LessonCheck, ENCODE), ENCODE);

			System.out.println("lessonUrl ="+lessonUrl);

			localTeacher = JsonUtiles.GetRightsLesson(lessonUrl);
			if (localTeacher.isSuccess())
			{
				mzLessonUrl = localTeacher.get_zURL();
				mvLessonUrl = localTeacher.get_vURL();
				if (localTeacher.get_tver() > 0)
				{
					String tstr = CheckPrefaceInfo(localTeacher.get_tid(), localTeacher.get_tver());
					// CheckPrefaceInfo(mylci.get_tid(),mylci.get_tver()).equals(object)
					mpLessonUrl = tstr.equals("0") ? localTeacher.get_turl() : null;
				}
				localListStudy = GetStudyMethodFiles(localTeacher.get_studyMethod(), localTeacher.get_BURL());
				//writeUser(localListStudy);
				
				//localListStudy = readObject();
				if (localListStudy.size() > 0)
				{
					new LoadStudyMethodDetailTask().execute();
				}
			}

		} catch (Exception e)
		{
			Log.e(TAG, "Can not upgrade tables3", e);
			mzLessonUrl = "连接课件服务器失败！请确认网络已连接并重启本程序后重试！";
			bRet = false;
		} finally
		{
			if (conn != null)
			{
				conn.disconnect();
			}

			try
			{
				if (stream != null)
				{
					stream.close();
				}
			} catch (Exception e)
			{
			}
		}

		return bRet;
	}

	private List<StudyMethod> GetStudyMethodFiles(List<StudyMethod> liststudymethod, String burl)
	{
		List<StudyMethod> lsm = new ArrayList<StudyMethod>();
		System.out.println("GetStudyMethodFiles");
		try
		{
			for (StudyMethod sm : liststudymethod)
			{
				// sm.get_nid()
				if (StudyMethodFilesNeedUpdate(sm, burl))
					lsm.add(sm);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return lsm;
	}

	private boolean StudyMethodFilesNeedUpdate(StudyMethod liststudymethod, String burl)
	{
		Cursor cs = mDatabase.rawQuery("select ver from " + StudyMethodInfo.TABLE_NAME + " where _id=" + liststudymethod.get_nid(), null);
		if (cs.moveToFirst())
		{
			return cs.getInt(0) < liststudymethod.get_ver() ? true : false;
		} else
		{
			ContentValues values = new ContentValues();
			values.put(StudyMethodInfo.ID, liststudymethod.get_nid());
			values.put(StudyMethodInfo.GID, liststudymethod.get_gid());
			values.put(StudyMethodInfo.VER, liststudymethod.get_ver());
			values.put(StudyMethodInfo.BASEURL, burl);
			values.put(StudyMethodInfo.URL, liststudymethod.get_xmlFile());
			mDatabase.getWritableDatabase().replace(StudyMethodInfo.TABLE_NAME, null, values);

		}
		cs.close();
		return true;
	}

	private String CheckPrefaceInfo(int tid, int newver)
	{
		Cursor cs = mDatabase.rawQuery("select ver,vurl from " + Teacher.TABLE_NAME + " where _id=" + tid, null);
		if (cs.moveToFirst())
		{
			if (!new File(cs.getString(1)).exists())
			{
				return "0";
			} else
			{
				return cs.getInt(0) < newver ? "0" : cs.getString(1);
			}
		}
		cs.close();
		return "0";
	}

	private static interface LessonQuery
	{
		Cursor query(long courseId);
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog, Bundle args)
	{
		switch (id)
		{
		case DOWNLOAD_DIALOG_ID:
			final String downloadUrl = mDownloadParam.lessonName;
			System.out.println("onPrepareDialog[downloadUrl] = "+downloadUrl);
			((TextView) dialog.findViewById(R.id.download_confirm_title)).setText(String.format(getResources().getString(R.string.download_message), downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1)));
			((Checkable) dialog.findViewById(R.id.download_confirm_check)).setChecked(!mOtherFunctionsInfo.mSettings[2]);
			break;
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id, Bundle args)
	{
		final Resources res = MainActivity.this.getResources();
		switch (id)
		{
		case DOWNLOAD_DIALOG_ID:
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(R.string.download_title);
			builder.setView(LayoutInflater.from(MainActivity.this).inflate(R.layout.download_confirm, null));
			builder.setPositiveButton(res.getString(android.R.string.ok), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Dialog dlg = (Dialog) dialog;
					mOtherFunctionsInfo.mSettings[2] = !((Checkable) dlg.findViewById(R.id.download_confirm_check)).isChecked();
					dialog.dismiss();

					// 下载二次点击控制
					disableDownButton(mDownloadParam.itemView);
					onStateChanged(mDownloadParam.id, EastStudy.STATUS_RUNNING);

					downloads();
				}
			});

			return builder.setNegativeButton(res.getString(android.R.string.cancel), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			}).create();

		case NETWORK_DIALOG_ID:
			AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(MainActivity.this);
			dlgBuilder.setTitle(R.string.network_title);
			dlgBuilder.setMessage(R.string.network_message);
			return dlgBuilder.setPositiveButton(res.getString(android.R.string.ok), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			}).create();

		case SITE_SELECT_DIALOG_ID:
			AlertDialog.Builder siteDlg = new AlertDialog.Builder(MainActivity.this);
			siteDlg.setTitle(R.string.site_title);
			siteDlg.setSingleChoiceItems(R.array.setting_site, mSelectedSite, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					mSelectedSite = which;
					dialog.dismiss();
				}
			});

			return siteDlg.setNegativeButton(res.getString(android.R.string.cancel), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			}).create();

		case LOGIN_OUT_DIALOG_ID:
			// 处理登陆失败
			AlertDialog.Builder lgbuilder = new AlertDialog.Builder(MainActivity.this);
			lgbuilder.setTitle(R.string.ERROR_TIP);
			lgbuilder.setMessage(R.string.login_error);
			// lgbuilder.setMessage(mUserLicense+"Mac:"+mWifiMacAddress);

			return lgbuilder.setPositiveButton(res.getString(android.R.string.ok), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					// MainActivity.this.removeDialog(LOGIN_OUT_DIALOG_ID);
					MainActivity.this.finish();
				}
			}).create();
		case ConstUtiles.GRADE_SELECT_DIALOG_ID:
			AlertDialog.Builder gradeDlg = new AlertDialog.Builder(MainActivity.this);
			gradeDlg.setTitle(R.string.setting);
			Cursor cs = mDatabase.query(Grade.TABLE_NAME, Grade.COLUMNS, null, null);
			GradeViewAdapter gva = new GradeViewAdapter(MainActivity.this, cs, R.layout.grade_text_item, 0);
			View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.setting_functions, null);
			final GridView gv = (GridView) v.findViewById(R.id.gradeview);
			final Button wt = (Button) v.findViewById(R.id.wt);
			final Button dx = (Button) v.findViewById(R.id.dx);
			final Button dao = (Button) v.findViewById(R.id.dao);
			dao.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					v.setEnabled(false);
					dao.setPressed(true);
					 daoru = true;
				}

			});
			wt.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					mSelectedSite = 0;
					v.setEnabled(false);
					dx.setEnabled(true);
				}

			});
			dx.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					mSelectedSite = 1;
					v.setEnabled(false);
					wt.setEnabled(true);
				}

			});
			gv.setNumColumns(3);
			gv.setGravity(Gravity.CENTER);
			gv.setVerticalSpacing(5);
			gv.setHorizontalSpacing(5);
			gv.setSelector(R.drawable.bei);
			gv.setSelection(pSelectedGrade);
			// gv.setPadding(20, 10, 10, 1);
			gv.setBackgroundColor(R.color.white);
			gv.setAdapter(gva);
			gv.setOnItemClickListener(gva);

			gradeDlg.setView(v);
			// gradeDlg.setMessage(R.string.network_message);
			return gradeDlg.setPositiveButton(res.getString(android.R.string.ok), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					if (mSelectedGrade > 0)
					{
						mAdapter.changeCursor(managedQuery(Downloads.ExpandableList.CONTENT_URI, BaseColumns.COLUMNS, BaseColumns.COLUMN_ID + "=" + mSelectedGrade, null, null));
						mAdapter.notifyDataSetChanged();
						mListView.expandGroup(0);
						((CursorAdapter) mCourseLessonList.getAdapter()).changeCursor(mAdapter.query(mAdapter.getChildId(0, 0)));
					}
					if (daoru==true)
					{
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, ListFile.class); // 参数一为当前Package的context，t当前Activity的context就是this，其他Package可能用到createPackageContex()参数二为你要打开的Activity的类名
						startActivity(intent);
					}

				}
			}).create();

		}

		return null;
	}

	class GradeViewAdapter extends GridViewCursorAdapter implements OnItemClickListener
	{

		public GradeViewAdapter(Context context, Cursor cursor, int layoutResId, int bgResId)
		{
			super(context, cursor, layoutResId, bgResId);
		}

		// LayoutInflater inflater;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			Cursor cursor = (Cursor) getItem(position);
			mSelectedGrade = cursor.getInt(Grade.COLUMN_ID_INDEX);
			pSelectedGrade = 0;
			view.setSelected(true);
		}
	}

	

	private class DownloadInfo
	{
		private final DownloadManager mDownloadManager;

		public DownloadInfo(Resources res, int maxThreads)
		{
			mDownloadManager = new DownloadManager(MainActivity.this, maxThreads, mDatabase);
			mDownloadManager.setOnDownloadStatusListener(MainActivity.this);

		}

		public final void download()
		{
			final Resources res = MainActivity.this.getResources();
			if (mOtherFunctionsInfo.mSettings[1] && mDatabase.existsDownloaded(mDownloadParam.id))
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle(R.string.title_redownload_confirm);
				builder.setMessage(R.string.message_redownload_confirm);

				builder.setPositiveButton(res.getString(android.R.string.ok), new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
						// Button download =
						// (Button)mDownloadParam.itemView.findViewById(R.id.hori_lesson_play);
						// mDownloadInfo.mDownloadManager.setProgressList(download);

						downloads();
					}
				});

				builder.setNegativeButton(res.getString(android.R.string.cancel), new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				}).create().show();
			} else
			{
				if (mOtherFunctionsInfo.mSettings[2])
				{
					showDialog(DOWNLOAD_DIALOG_ID, null);
				} else
				{
					// 下载二次点击控制
					disableDownButton(mDownloadParam.itemView);
					onStateChanged(mDownloadParam.id, EastStudy.STATUS_RUNNING);
					// Button download =
					// (Button)mDownloadParam.itemView.findViewById(R.id.hori_lesson_play);
					// mDownloadInfo.mDownloadManager.setProgressList(download);

					downloads();
				}
			}
		}

		public final void clear(int timeout)
		{
			mDownloadManager.shutdown(timeout);
			// ((CursorAdapter)mDownloadedList.getAdapter()).changeCursor(null);
			// ((CursorAdapter)mDownloadingList.getAdapter()).changeCursor(null);
		}
	}

	private void downloads()
	{
		mDownloadInfo.mDownloadManager.execute(mDownloadParam.id, mDownloadParam.downloadzUrl, mDownloadParam.lessonName, mDownloadParam.lessonType, mDownloadParam.sortOrder, mDownloadParam.courseId, Downloads.DOWNLOADS_TYPE[Downloads.DOWNLOADS_TYPE_ZIP_INDEX], 0, 0);
		if (mDownloadParam.downloadpUrl != null && mDownloadParam.downloadpUrl.length() > 10)
		{
			mDownloadInfo.mDownloadManager.execute(mDownloadParam.id, mDownloadParam.downloadpUrl, mDownloadParam.lessonName, mDownloadParam.lessonType, mDownloadParam.sortOrder, mDownloadParam.courseId, Downloads.DOWNLOADS_TYPE[Downloads.DOWNLOADS_TYPE_MP4_INDEX], mDownloadParam.tid, mDownloadParam.tver);
		}

		mDownloadInfo.mDownloadManager.execute(mDownloadParam.id, mDownloadParam.downloadvUrl, mDownloadParam.lessonName, mDownloadParam.lessonType, mDownloadParam.sortOrder, mDownloadParam.courseId, Downloads.DOWNLOADS_TYPE[Downloads.DOWNLOADS_TYPE_VIDEO_INDEX], 0, 0);
	}

	private class OtherFunctionsInfo
	{
		private final boolean[] mSettings;

		public OtherFunctionsInfo(Resources res)
		{
			final String[] settings = res.getStringArray(R.array.settings_functions);
			mSettings = new boolean[settings.length - 1];

		}

		public void loadSharedPreferences()
		{
			mSettings[0] = mSharedPreferences.getBoolean(SAVE_PATH, true);
			// mSettings[0] = mSharedPreferences.getBoolean(AUTO_ORIENTATION,
			// true);
			mSettings[1] = mSharedPreferences.getBoolean(SAVE_DOWNLOAD, false);
			mSettings[2] = mSharedPreferences.getBoolean(DOWNLOAD_CONFIRM, true);
			mSelectedSite = mSharedPreferences.getInt(SELECTED_SITE, 0);
			mSelectedGrade = mSharedPreferences.getInt(ConstUtiles.SELECTED_GRADE, 0);
			pSelectedGrade = mSharedPreferences.getInt(ConstUtiles.SELECTED_GP, 0);
			pSelectedChild = mSharedPreferences.getInt(ConstUtiles.SELECTED_CP, 0);
			current_gid = mSharedPreferences.getLong(CBGID, 0);
			checkurl = mSharedPreferences.getString(ConstUtiles.CHECKURL, "");
			uid = mSharedPreferences.getInt(ConstUtiles.USRID, 0);
			randSerialNo = mSharedPreferences.getString(ConstUtiles.RANDUM, "");
		}

		public void saveSharedPreferences(Editor editor)
		{
			editor.putBoolean(SAVE_PATH, mSettings[0]);
			// editor.putBoolean(AUTO_ORIENTATION, mSettings[0]);
			editor.putBoolean(SAVE_DOWNLOAD, mSettings[1]);
			editor.putBoolean(DOWNLOAD_CONFIRM, mSettings[2]);
			editor.putInt(SELECTED_SITE, mSelectedSite);
			editor.putInt(ConstUtiles.SELECTED_GRADE, mSelectedGrade);
			editor.putInt(ConstUtiles.SELECTED_GP, pSelectedGrade);
			editor.putInt(ConstUtiles.SELECTED_CP, pSelectedChild);
			editor.putString(ConstUtiles.CHECKURL, checkurl);
			editor.putInt(ConstUtiles.USRID, uid);
			editor.putString(ConstUtiles.RANDUM, randSerialNo);
			editor.putLong(CBGID, current_gid);
		}
	}

	private static class DownloadParam
	{
		long id;
		int lessonType;
		int sortOrder;
		long courseId;
		String downloadzUrl;
		String downloadvUrl;
		String downloadpUrl;
		String lessonName;
		View itemView;

		int tid;
		int tver;

		public void set(View view, long id, String downloadzUrl, String downloadvUrl, String downloadpUrl, String lessonName, int lessonType, int sortOrder, long courseId, int tid, int tver)
		{
			this.id = id;
			this.downloadzUrl = downloadzUrl;
			this.downloadvUrl = downloadvUrl;
			this.lessonName = lessonName;
			this.lessonType = lessonType;
			this.sortOrder = sortOrder;
			this.courseId = courseId;
			this.itemView = view;
			this.downloadpUrl = downloadpUrl;
			this.tid = tid;
			this.tver = tver;
		}
	}

	/**
	 * 显示退出窗口
	 */
	protected void ShowExitdialog()
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		if (mDownloadInfo.mDownloadManager.isTaskRunning())
		{
			builder.setMessage("有任务正在下载，确定要退出吗?");
		} else
		{
			builder.setMessage("确定要退出吗?");
		}
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("提示");

		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if (mDownloadInfo != null)
				{
					mDownloadInfo.clear(5000);
				}
				dialog.dismiss();

				MainActivity.this.finish();
			}

		});

		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{

				dialog.dismiss();

			}

		});

		builder.create().show();

	}

	/**
	 * 获取wifi mac地址
	 * 
	 * @return wifi mac地址(xx:xx:xx:xx:xx)
	 */
	public String getLocalMacAddress()
	{
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 获取IMEI地址
	 * 
	 * @return IMEI地址(xx:xx:xx:xx:xx)
	 */
	public String getTelphoneIMEI()
	{
		TelephonyManager telMager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return telMager.getDeviceId();
	}

	/**
	 * 是否有网络
	 * 
	 * @return
	 */
	private boolean isNetworkAvailable()
	{
		final ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		// final NetworkInfo wifi =
		// manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		final NetworkInfo[] info = manager.getAllNetworkInfo();
		for (int i = 0; i < info.length; ++i)
		{
			if (info[i].isConnected())
			{
				return true;
			}
		}

		return false;
	}

	private final BroadcastReceiver mNetworkReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			final String action = intent.getAction();
			if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
			{
				Log.v("ConnectivityReceiver", "onReceive() called with " + intent);
				return;
			}
			if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action))
			{
				mNetworkAvailability = !intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
			}
		}
	};

	/*
	 * update block start
	 */
	private void UpdateSoft(final String url)
	{
		// pBar.show();
		pd = ProgressDialog.show(MainActivity.this, res.getString(R.string.upgrade_title), res.getString(R.string.upgrade_soft), true, false);
		File apkpath = new File(APK_PATH);
		if (!apkpath.exists())
		{
			apkpath.mkdir();
		}

		new Thread()
		{

			@Override
			public void run()
			{
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				String fileName = url.substring(url.lastIndexOf("/") + 1);
				HttpResponse response;
				try
				{
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					// long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null)
					{
						File file = new File(APK_PATH, fileName);
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						while ((ch = is.read(buf)) != -1)
						{
							fileOutputStream.write(buf, 0, ch);
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null)
					{
						fileOutputStream.close();
					}
					installApk(APK_PATH + File.separator + fileName);
				} catch (ClientProtocolException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}

		}.start();
	}

	void installApk(final String fname)
	{
		pd.dismiss();
		pd = null;
		Looper.prepare();
		new Handler().post(new Runnable()
		{

			@Override
			public void run()
			{
				update(fname);
			}

		});
		Looper.loop();
	}

	void update(String fname)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		try
		{
			intent.setDataAndType(Uri.fromFile(new File(fname)), "application/vnd.android.package-archive");
			startActivityForResult(intent, 0);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		MainActivity.this.finish();
	}

	@Override
	public void onStateChanged(long id, int status)
	{
		ContentValues values = new ContentValues();
		if (status == EastStudy.STATUS_NOSPACE)
		{
			values.put(EastStudy.Lesson.COLUMN_STATUS, EastStudy.STATUS_PENDING);
		} else
		{
			values.put(EastStudy.Lesson.COLUMN_STATUS, status);
		}

		mDatabase.getWritableDatabase().update(EastStudy.Lesson.TABLE_NAME, values, BaseColumns.COLUMN_ID + '=' + id, null);

		CursorAdapter madapter = (CursorAdapter) mCourseLessonList.getAdapter();
		Cursor mcursor = madapter.getCursor();
		if (mcursor != null)
		{
			mcursor.requery();
			madapter.notifyDataSetChanged();
		}
		if (status == EastStudy.STATUS_NOSPACE)
		{
			// Toast.makeText(this, "网络中断或空间不足！请检查后重试！", Toast.LENGTH_LONG);
			AlertDialog.Builder pBar = new AlertDialog.Builder(MainActivity.this);
			pBar.setTitle("提示");
			pBar.setMessage("网络中断或空间不足！请检查后重试！");
			pBar.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					// DgktActivity.this.finish();
				}
			});

			pBar.setPositiveButton(this.getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
			AlertDialog adl = pBar.create();
			adl.setCanceledOnTouchOutside(false);
			adl.show();

			adl.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
			adl.setOnKeyListener(new OnKeyListener()
			{

				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
				{
					// TODO Auto-generated method stub
					// 仅屏蔽back键
					if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME)
					{
						return true;
					}
					return false;
				}
			});
		}
		if (status == EastStudy.STATUS_SUCCESSFUL)
		{
			if (mOtherFunctionsInfo.mSettings[1])
			{
				mDatabase.insertDownloaded(id);
			}
		}
	}

	@SuppressLint("SdCardPath")
	class LoadStudyMethodDetailTask extends AsyncTask<String, Long, List<StudyMethodDetail>>
	{
		// private View _resultView;
		String _baseurl;

		LoadStudyMethodDetailTask()
		{
			_baseurl = localTeacher.get_BURL();
		}

		@Override
		protected void onPostExecute(List<StudyMethodDetail> result)
		{

			// ((ImageView)_resultView).setImageURI(result);
			// _resultView.setBackgroundDrawable(new
			// BitmapDrawable(result.getPath()));
			
			//listStudy2 =result;
			
//			writeStudy(result);
//			
//			listStudy2 = readStudy("/mnt/sdcard/test2.xml");
//				System.out.println("OK");
//				System.out.println(listStudy2.size());
			if (result.size() > 0)
			{
				for (StudyMethodDetail smd : result)
				{
					UpdateSMDetail(smd);
				}
			}
		}

		@Override
		protected List<StudyMethodDetail> doInBackground(String... params)
		{
			// TODO Auto-generated method stub

			// return Uri.fromFile(imgfile);
			// localTeacher
			List<StudyMethodDetail> lsmd = new ArrayList<StudyMethodDetail>();

			try
			{
				for (StudyMethod sm :localListStudy)
				{
					if (sm.get_nid() > 0)
					{
						String rl = _baseurl + sm.get_xmlFile();
						String methodDetail = new String(MyConnector.getByte(rl), ENCODE);
						StudyMethodDetail gsmd = JsonUtiles.GetStudyMethodDetailInfos(methodDetail);
						if (gsmd.isSuccess())
						{
							lsmd.add(gsmd);
							updateSMI(sm);
						}
					}
				}

			} catch (Exception e)
			{
				e.printStackTrace();
				return lsmd;
			}
			
			return lsmd;
		}

	}

	/**
	 * 插入学法同步详细信息
	 * 
	 * @param StudyMethodDetail
	 *            _sm
	 * 
	 */
	public static  void UpdateSMDetail(StudyMethodDetail _sm)
	{
		SQLiteDatabase db = mDatabase.getWritableDatabase();
		ContentValues values = new ContentValues();
		String tableName = EastStudy.StudyMethodDetail.TABLE_NAME;
		// String where = StudyMethodInfo.ID + '=' + _sm.get_sid();

		values.put(EastStudy.StudyMethodDetail.ID, _sm.get_NID());
		values.put(EastStudy.StudyMethodDetail.TITLE, _sm.get_NT());
		values.put(EastStudy.StudyMethodDetail.CONTENT, _sm.get_NContend());
		values.put(EastStudy.StudyMethodDetail.BASEURL, _sm.get_Baseurl());
		values.put(EastStudy.StudyMethodDetail.HASPIC, _sm.get_PIC().get_state());
		values.put(EastStudy.StudyMethodDetail.GID, current_gid);

		db.replace(tableName, null, values);
		if (_sm.get_PIC().get_state() > 0)
		{
			UpdateSMDetailPic(_sm.get_PIC(), _sm.get_NID(), _sm.get_Baseurl());
		}
	}

	/**
	 * 插入学法同步图片信息
	 * 
	 * @param StudyMethodDetail
	 *            _sm
	 * 
	 */
	private static  void UpdateSMDetailPic(SMPic _sm, int _id, String _burl)
	{
		SQLiteDatabase db = mDatabase.getWritableDatabase();
		ContentValues values = new ContentValues();
		String tableName = EastStudy.StudyMethodDetailPic.TABLE_NAME;
		// String where = StudyMethodInfo.ID + '=' + _sm.get_sid();
		for (String smp : _sm.get_url())
		{
			values.put(EastStudy.StudyMethodDetailPic.ID, _id);
			values.put(EastStudy.StudyMethodDetailPic.URL, smp);
			values.put(EastStudy.StudyMethodDetailPic.BASEURL, _burl);

			db.replace(tableName, null, values);
		}
	}

	/**
	 * 更新版本信息
	 * 
	 * @param StudyMethod
	 *            _sm
	 * @return 
	 */
	public   void updateSMI(StudyMethod _sm)
	{
		SQLiteDatabase db = mDatabase.getWritableDatabase();
		ContentValues values = new ContentValues();
		String tableName = EastStudy.StudyMethodInfo.TABLE_NAME;
		String where = StudyMethodInfo.ID + '=' + _sm.get_nid();

		values.put(StudyMethodInfo.VER, _sm.get_ver());

		try
		{
			db.update(tableName, values, where, null);
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	

	/*
	 * update block end
	 */
	public class ExpandListViewAdapter extends SimpleCursorTreeAdapter implements OnChildClickListener
	{

		private static final String EMPTY_STRING = "";
		public OnChildClickListener onclicklister;
		Context mContext;
		private Cursor mCursor;

		public ExpandListViewAdapter(Cursor cursor, Context context, int groupLayout, int childLayout, String[] groupFrom, int[] groupTo, String[] childrenFrom, int[] childrenTo)
		{
			super(context, cursor, groupLayout, groupFrom, groupTo, childLayout, childrenFrom, childrenTo);
			onclicklister = this;
			mContext = context;
			mCursor = cursor;
		}

		public final String getItemText(int position)
		{
			String text = EMPTY_STRING;
			if (mCursor != null && mCursor.moveToPosition(position))
			{
				text = mCursor.getString(BaseColumns.COLUMN_NAME_INDEX);
			}

			return text;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
		{
			// View view =
			// LayoutInflater.from(mContext).inflate(R.layout.main_test_group,
			// null);
			View view = super.getGroupView(groupPosition, isExpanded, convertView, parent);
			// ImageView
			// mgroupimage=(ImageView)view.findViewById(R.id.groupimage);
			TextView mgroupimage = (TextView) view.findViewById(R.id.btn_group);
			if (!isExpanded)
			{
				// mgroupimage.setBackgroundResource(R.drawable.rightarrow);
				mgroupimage.setBackgroundResource(R.drawable.handle_normal);
			} else
			{
				// mgroupimage.setBackgroundResource(R.drawable.downarrow);
				mgroupimage.setBackgroundResource(R.drawable.handle_pressed);
			}
			mgroupimage.setPadding(40, 5, 5, 5);
			// TextView tv = (TextView) view.findViewById(R.id.groupname);
			// mgroupimage.setText(getItemText(groupPosition));

			// return super.getGroupView(groupPosition, isExpanded, convertView,
			// parent);
			return view;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			View view = super.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
			TextView mgroupimage = (TextView) view.findViewById(R.id.childText);
			if (pSelectedChild == childPosition && pSelectedGrade == groupPosition)
			{
				mgroupimage.setTextColor(R.color.leftchildbg);
			} else
			{
				mgroupimage.setTextColor(R.color.black);
			}
			return view;
		}

		@Override
		protected Cursor getChildrenCursor(Cursor groupCursor)
		{
			// Given the group, we return a cursor for all the children within
			// that group

			// Return a cursor that points to this contact's phone numbers
			/*
			 * Uri.Builder builder =
			 * Downloads.ExpandableList.CONTENT_URI.buildUpon();
			 * ContentUris.appendId(builder,
			 * groupCursor.getLong(Grade.COLUMN_ID_INDEX));
			 * //ContentUris.appendId(builder,
			 * groupCursor.getLong(mGroupIdColumnIndex));
			 * 
			 * //builder.appendEncodedPath(Grade.TABLE_NAME); Uri myTreeUri =
			 * builder.build();
			 */

			// The returned Cursor MUST be managed by us, so we use Activity's
			// helper
			// functionality to manage it for us.
			// return managedQuery(phoneNumbersUri, mPhoneNumberProjection,
			// null, null, null);
			return managedQuery(Downloads.ExpandableList.CONTENT_ITEM_URI, BaseColumns.COLUMNS, EastStudy.buildContentVersionWhere(groupCursor.getLong(Grade.COLUMN_ID_INDEX)), null, null);

		}

		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
		{
			// TODO Auto-generated method stub
			// Cursor cs = getChild(groupPosition, childPosition);

			current_gid = this.getGroupId(groupPosition);

			if (pSelectedChild != childPosition || pSelectedGrade != groupPosition)
			{
				// this.g
				pSelectedGrade = groupPosition;
				pSelectedChild = childPosition;
				((CursorAdapter) mCourseLessonList.getAdapter()).changeCursor(query(id));
				parent.invalidateViews();
				((TextView) v.findViewById(R.id.childText)).setTextColor(R.color.leftorange);

			}
			return true;
		}

		public Cursor query(long courseId)
		{
			Log.i(TAG, "courseId = " + courseId);
			// return getContentResolver().query(Downloads.Success.CONTENT_URI,
			// Downloads.DOWNLOAD_COLUMNS, Downloads.COURSE_ID + '=' + courseId,
			// null, null);
			return mDatabase.query(Lesson.TABLE_NAME, Lesson.COLUMNS, EastStudy.buildLessonWhere(courseId), null);
		}

	}
	/*
	 * 写object到文件
	 */
	public  void writeUser(List<StudyMethod> localListStudy){
		//file 为写入的文件
		ObjectOutputStream oos;
			try {
				oos = new ObjectOutputStream(new FileOutputStream("/mnt/sdcard/test.xml"));
				oos.writeObject(localListStudy);
				oos.flush();
				oos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	/*
	 * 写object到文件
	 */
	public  void writeStudy(List<StudyMethodDetail> result){
		//file 为写入的文件
		ObjectOutputStream oost;
			try {
				oost = new ObjectOutputStream(new FileOutputStream("/mnt/sdcard/test2.xml"));
				oost.writeObject(result);
				oost.flush();
				oost.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	
	/*
	 *读object
	 */
	public  List<StudyMethodDetail> readStudy(String path){
		List<StudyMethodDetail> liststudy_temp = null;
		  try {
			  FileInputStream  fis=new  FileInputStream(path);  
			  ObjectInputStream Oin = new ObjectInputStream(fis);
		   liststudy_temp = (List<StudyMethodDetail>) ( Oin.readObject());
		   Oin.close();
		  } catch (FileNotFoundException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  } catch (ClassNotFoundException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
		 
		return liststudy_temp;
		  
		 }
	
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		switch (v.getId())
		{
		case R.id.btn_editor:
			mEditStatus = true;
			mEdit.setVisibility(View.GONE);
			mComplete.setVisibility(View.VISIBLE);
			onStateChanged(0, 0);
			break;
		case R.id.btn_complete:
			mEditStatus = false;
			mEdit.setVisibility(View.VISIBLE);
			mComplete.setVisibility(View.GONE);
			onStateChanged(0, 0);
			break;
		case R.id.btn_setting:
			this.showDialog(ConstUtiles.GRADE_SELECT_DIALOG_ID);
			break;
		}
	}

}