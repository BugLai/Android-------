package com.tqxktbyw.study;

//import com.mywebview.R;
//import com.mywebview.MainActivity.MyWebChromeClient;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tqxktbyw.study.EastStudy.BaseColumns;
import com.tqxktbyw.study.EastStudy.Lesson;
import com.tqxktbyw.study.EastStudy.Teacher;
import com.tqxktbyw.study.database.EastStudyDatabase;
import com.tqxktbyw.study.entity.MyConnector;
import com.tqxktbyw.study.entity.TransInfo;
import com.tqxktbyw.study.provider.Downloads;
import com.tqxktbyw.videoplayer.VideoView;

public class StudyWebView extends Activity  implements OnClickListener, OnErrorListener, OnPreparedListener,OnCompletionListener {
	private static final int BTN_REMEMBER_INDEX =0;
	private static final int BTN_TEST_INDEX =1;
	private static final int BTN_ZKJX_INDEX =2;
	private static final int BTN_GKJX_INDEX =3;
	private static final int BTN_KWTZ_INDEX =4;
	private static final int BTN_XFLY_INDEX =5;
	private static final int BTN_TBTL_INDEX =6;
	private static final int BTN_ASK_INDEX =7;
	//private static final SimpleDateFormat sdf = new SimpleDateFormat(
		//	"yyyy-MM-dd hh:mm:ss");
	private static final String ENCODE = "GBK";
	public static String[] FILES_COLUMNS = {
		"index.htm","zxcs.htm","zkjx.htm",
		"gkjx.htm","kwtz.htm","xfly.htm","tbtl.htm"
	};
	
	private ImageButton mRemember;
	private ImageButton mReduce;
	private ImageButton mAsk;
	private ImageButton mTest;
	private ImageButton mZkjx;
	private ImageButton mGkjx;
	private ImageButton mKwtz;
	private ImageButton mXfly;
	private ImageButton mTbtl;
	private VideoDec vd;
	//private String indexUri;
	public String RANDOM = "";
	private int AskUID=0;
	private WebView mWebView;
	private ListView mListView;
	private int mCurrentButton;
	private TransInfo mTi;
	private String mWeburl;
	//private int mShowType;//0 webview, 1 video,2 Writing
	private String askLoginURL="http://pad2.tqpad.com/login/login_student_question.php";
	private String askSaveUrl = "http://pad2.tqpad.com/padaskcheck/lession_ask.php";
	//Video 
	private VideoView mVideoLesson;
    private MediaController mMediaController;
    private int stopposition = 10000;
    private MyStudyMethodAdapter msma;
    private int prefacePosition;
    private EastStudyDatabase mDatabase;
    private long currentGid;
    private int currentSid;
    private long mLessonID;
    String uri;
    String decFileName;
    //答start
	private GridView bottomMenuGrid;
	String content;
	String noteId;
	EditText title;
	EditText edit;
	RelativeLayout relativelayout;
	public int pic_count = 0;
	String[] bottom_menu_itemName = { "提交","取消", "插图" };

	int[] bottom_menu_itemSource = { R.drawable.menu_sub, 
			R.drawable.menu_quxiao, R.drawable.menu_insert };
	public StringBuffer sb = new StringBuffer();
	public String edit_value = "";
	public String endlist;
	public String now_value;
	private static final int SELECT_IMAGE = 0x123;
	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果
	public static final int WRITTING = 4;// 手写
	static String path = EastStudy.STORAGE_PATH + "/tqdy";
	static String pathvalue = EastStudy.STORAGE_PATH + "/";
	public int width;
	public int height;
	ImageView imageView = null;
	private TextView vtv;
	public Intent intent;
	public Bundle bud;
	public boolean firstPlay;
	private ProgressDialog pd;
	//private Myapplication app;
	private int leftnum;
	private long vtotalsize;

	public static SharedPreferences mSharedPreferences;
//	private int mSelectedPos = 0;
//	private long mSelectedId = 1;

	private static final String ASKLEFTNUM = "ASKLEFTNUM";
	private String mWifiMacAddress = "";
    //答end
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window win = getWindow();
		win.setBackgroundDrawableResource(R.drawable.background);
		//win.
		win.requestFeature(Window.FEATURE_LEFT_ICON);
		win.requestFeature(Window.FEATURE_RIGHT_ICON);
		
		
		win.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.logo);
		win.setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.go_back);
		
		Intent intent  = getIntent();
		mTi=new TransInfo();
		firstPlay = true;
        mLessonID = intent.getLongExtra(EastStudy.PLAYING_URI,0L);
        currentGid = intent.getLongExtra(EastStudy.StudyMethodDetail.GID,0L);
        currentSid = intent.getIntExtra(EastStudy.Subject.COLUMN_ID,0);
        if(currentGid<7){
			setContentView(R.layout.lesson_player_x);
			//middle_layout.get
		}else{
			setContentView(R.layout.lesson_player);
		}
        loadSharedPreferences();
        mMediaController = new MediaController(this);
        mVideoLesson = (VideoView)findViewById(R.id.lesson_player);
        mVideoLesson.setMediaController(mMediaController);
        //mShowType = 0;
        mDatabase=new EastStudyDatabase(this);
        getLessonInfo();
        vd = new VideoDec(this);
        String macno;
		 macno = getLocalMacAddress();
		 if (macno!=null)
		 mWifiMacAddress= macno.replace(":", "");
		init();
		initAsk();
        if(isNetworkAvailable()){
        	new UpgradeTask(isNetworkAvailable()).execute("");
        }
	}
	/**
	 * 获取wifi mac地址
	 * 
	 * @return wifi mac地址(xx:xx:xx:xx:xx)
	 */
	public String getLocalMacAddress() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}
	/**
	 * 是否有网络
	 * 
	 * @return
	 */
	private boolean isNetworkAvailable() {
		final ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		final NetworkInfo[] info = manager.getAllNetworkInfo();
		for (int i = 0; i < info.length; ++i) {
			if (info[i].isConnected()) {
				return true;
			}
		}

		return false;
	}

	private class UpgradeTask extends AsyncTask<String, Long, Cursor[]> {
		private ProgressDialog mProgressDialog;
		private final boolean mNetworkAvailable;
		private boolean bGetRet = true;
		private String ErrorMsg = "";

		public UpgradeTask(boolean networkAvailable) {
			mNetworkAvailable = networkAvailable;
		}

		@Override
		protected void onPostExecute(Cursor[] cursors) {
			if (mNetworkAvailable) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
			if (bGetRet) {
				
			} else {
				// 处理失败信息
				final Resources res = StudyWebView.this.getResources();
				AlertDialog.Builder lgbuilder = new AlertDialog.Builder(
						StudyWebView.this);
				lgbuilder.setTitle(R.string.ERROR_TIP);
				lgbuilder.setMessage(ErrorMsg);

				lgbuilder
						.setPositiveButton(res.getString(android.R.string.ok),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).create().show();
			}
		}

		@Override
		protected void onPreExecute() {
			if (mNetworkAvailable) {
				Resources res = getResources();
				mProgressDialog = ProgressDialog.show(StudyWebView.this,
						res.getString(R.string.upgrade_title),
						res.getString(R.string.upgrade_message), true, false);
			}
		}

		@Override
		protected Cursor[] doInBackground(String... params) {
			if (mNetworkAvailable) {

				Map<String, String> paramsLogin = new HashMap<String, String>();

				paramsLogin.put("tq_mac", mWifiMacAddress);// mac地址
				paramsLogin.put("tq_ver", "1");// 版本号
				String json1;
				try {
					json1 = new String(MyConnector.postFromHttpClient(askLoginURL, paramsLogin,
							ENCODE), ENCODE);
					JSONObject jo = new JSONObject(json1);

					if (jo.getString("success").equals("1")) {

						JSONArray userArray = jo.getJSONArray("user");
						JSONObject random = userArray.getJSONObject(0);
						RANDOM = random.getString("randNum");

						AskUID = random.getInt("usrID");
						leftnum = random.getInt("qNum");
						saveSharedPreferences();

					} else {

						RANDOM = null;

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			Cursor[] cursors = new Cursor[4];
			if (bGetRet) {
				cursors[0] = queryCourse(true);
			}
			return cursors;
		}

		private Cursor queryCourse(boolean flags) {
			return managedQuery(Downloads.ExpandableList.CONTENT_URI,BaseColumns.COLUMNS, null, null,null);
		}
		
	}
	private void initAsk() {
		relativelayout = (RelativeLayout) this.findViewById(R.id.m_ask);
		title = (EditText) findViewById(R.id.title);
		edit = (EditText) findViewById(R.id.content);
		loadBottomMenu();
		
	}
	public void save(String list) {
		String spic_path = "";
		String context = edit.getText().toString();
		String title_value = title.getText().toString();
		
		String json;

		try {
			String[] edit_Array = context.split("@@@");

			for (int i = 0; i < edit_Array.length; i++) {
				if (edit_Array[i].lastIndexOf("###") != -1) {
					String[] pic_Array = edit_Array[i].split("###");
					for (int j = 0; j < pic_Array.length; j++) {
						if (j == 0) {
							// Uri spic_path = Uri.parse(pic_Array[0] );
							spic_path = pic_Array[0];

						}
						if (j == 1) {
							if (!pic_Array[1].equals("")) {
								sb.append(pic_Array[1]);
							}
						}
					}
					width = edit.getWidth();
					height = edit.getHeight();
				} else {
					sb.append(edit_Array[i]);
				}
			}
			if (edit.getText().toString() != "" && !title_value.equals("")
					&& !context.equals("")) {
				// 要上传的文件路径，理论上可以传输任何文件，实际使用时根据需要处理
				if (!isNetworkAvailable()) {

					Toast.makeText(StudyWebView.this, R.string.network_message,
							Toast.LENGTH_LONG).show();

				} else {

					json = new String(
							post(askSaveUrl, spic_path, title_value, context,
									currentGid+"", currentSid+""));
					JSONObject jo = new JSONObject(json);
					System.out.println("question.json:"+json);

					if (jo.getString("success").equals("1")) {

						leftnum = jo.getInt("Qnum");
						saveSharedPreferences();

						Toast.makeText(StudyWebView.this, R.string.questionsucces,
								Toast.LENGTH_LONG).show();
						edit.setText("");
						title.setText("");
						pic_count = 0;
						((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(StudyWebView.this
								.getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
						onClick(this.findViewById(R.id.remember));
					}else{
						if(jo.getInt("Qnum")==0){
							Toast.makeText(StudyWebView.this, R.string.numless,
									Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(StudyWebView.this, R.string.questionfaile,
									Toast.LENGTH_LONG).show();
						}
					}

				}

			} else {
				Toast.makeText(StudyWebView.this, R.string.titleempty,
						Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;
	}
	private void loadBottomMenu() {

		if (bottomMenuGrid == null) {
			bottomMenuGrid = (GridView) findViewById(R.id.gv_buttom_menu);
			bottomMenuGrid.setBackgroundResource(R.drawable.biaotibeijing);// 设置背景
			bottomMenuGrid.setNumColumns(3);// 设置每行列数
			bottomMenuGrid.setGravity(Gravity.CENTER);// 位置居中
			bottomMenuGrid.setVerticalSpacing(1);// 垂直间隔
			bottomMenuGrid.setHorizontalSpacing(15);// 水平间隔
			bottomMenuGrid.setAdapter(getMenuAdapter(bottom_menu_itemName,
					bottom_menu_itemSource));// 设置菜单Adapter
			/** 监听底部菜单选项 **/
			bottomMenuGrid.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					content = edit.getText().toString();

					switch (arg2) {
					case 0: {
						save(content);
						break;
					}

				
					case 1: {
						finish();
						break;

					}
					case 2: {// 插图
						if (pic_count == 1) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									StudyWebView.this);
							builder.setTitle("警告"); // 设置标题、
							builder.setMessage("只能插入一张图片，继续会删除第一张"); // 设置对话框内容
							builder.setPositiveButton("确认",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											AlertDialog.Builder log = new AlertDialog.Builder(
													StudyWebView.this);
											log.setTitle("请选择：");
											log.setItems(
													R.array.insertpic,
													new DialogInterface.OnClickListener() {
														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {

															String[] arr = getResources()
																	.getStringArray(
																			R.array.insertpic);
															if (arr[which]
																	.equals("从图库中插入")) {
																Intent getImage = new Intent(
																		Intent.ACTION_PICK,
																		MediaStore.Images.Media.INTERNAL_CONTENT_URI);
																startActivityForResult(
																		getImage,
																		SELECT_IMAGE);

															} else if (arr[which]
																	.equals("现拍")) {

																Intent intent = new Intent(
																		MediaStore.ACTION_IMAGE_CAPTURE);
																intent.putExtra(
																		MediaStore.EXTRA_OUTPUT,
																		Uri.fromFile(new File(EastStudy.STORAGE_PATH,
																				"temp.jpg")));
																startActivityForResult(
																		intent,
																		PHOTOHRAPH);
															}
														}
													});
											log.setNegativeButton(
													android.R.string.cancel,
													new DialogInterface.OnClickListener() {

														@Override
														public void onClick(
																DialogInterface dialog,
																int which) {
															dialog.dismiss();
														}
													});
											log.show();

										}
									});
							builder.setNegativeButton(android.R.string.cancel,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											 dialog.dismiss();
										}
									});
							builder.create().show();

						} else {

							AlertDialog.Builder log = new AlertDialog.Builder(
									StudyWebView.this);
							log.setTitle("请选择：");
							log.setItems(R.array.insertpic,
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											String[] arr = getResources()
													.getStringArray(
															R.array.insertpic);
											if (arr[which].equals("从图库中插入")) {
												Intent getImage = new Intent(
														Intent.ACTION_PICK,
														MediaStore.Images.Media.INTERNAL_CONTENT_URI);
												startActivityForResult(
														getImage, SELECT_IMAGE);

											} else if (arr[which].equals("现拍")) {

												Intent intent = new Intent(
														MediaStore.ACTION_IMAGE_CAPTURE);
												intent.putExtra(
														MediaStore.EXTRA_OUTPUT,
														Uri.fromFile(new File(EastStudy.STORAGE_PATH,
																"temp.jpg")));
												startActivityForResult(intent,
														PHOTOHRAPH);
											}
										}
									});
							log.setNegativeButton(android.R.string.cancel,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									});
							log.show();

						}

						
						break;
					}
					}
				}
			});

		}

	}
	private void getLessonInfo(){
		Cursor cur = mDatabase.query(EastStudy.Lesson.TABLE_NAME, 
        		EastStudy.Lesson.COLUMNS, EastStudy.Lesson.COLUMN_ID + "=" + mLessonID, null);
		
		if(cur.moveToFirst()){
			
			mTi.set_CS(cur.getString(Lesson.COLUMN_CS_INDEX));
			mTi.set_KS(cur.getString(Lesson.COLUMN_KS_INDEX));
			mTi.set_ZS(cur.getString(Lesson.COLUMN_ZS_INDEX));
			mTi.set_TS(cur.getString(Lesson.COLUMN_TS_INDEX));
			mTi.set_CS(cur.getString(Lesson.COLUMN_CS_INDEX));
			mTi.set_tid(cur.getLong(Lesson.COLUMN_TEACHER_INDEX));
			//mTi.set_tUrl(cur.getString(Lesson.COLUMN_TEACHER_INDEX));
			mTi.set_vUrl(cur.getString(Lesson.COLUMN_URL_INDEX));
			System.out.println("VUL==="+cur.getString(Lesson.COLUMN_URL_INDEX));
			mTi.set_zUrl(cur.getString(Lesson.COLUMN_ZIPNAME_INDEX));
			mTi.set_Title(cur.getString(Lesson.COLUMN_NAME_INDEX));
			System.out.println(cur.getString(Lesson.COLUMN_NAME_INDEX)+":22");
			uri = mTi.get_vUrl();
			mWeburl = "file://"+ mTi.get_zUrl() + "/";
			vtotalsize = getVtotalsize(mLessonID);
		}else
			mTi =null;
		cur.close();
		if(mTi!=null){
			String turls = GetTurl(mTi.get_tid());
			if(turls != null){
				mTi.set_tUrl(turls);
				mVideoLesson.setVideoURI(Uri.parse(mTi.get_tUrl()));
			}else{
				firstPlay = false;
			}
		}
	}
	private String GetTurl(long tid){
		String str;
		Cursor cur = mDatabase.rawQuery("select " +EastStudy.Teacher.URL
				+ " from " + EastStudy.Teacher.TABLE_NAME + " where "
        		+ EastStudy.Teacher.ID + " =" + tid, null);
		if(cur.moveToFirst()){
			str = "file://" + cur.getString(Teacher.ID_INDEX);
			
		}else
			str = null;
		cur.close();
		return str;
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void init() {
		if(mTi!=null){
			mListView = (ListView) this.findViewById(R.id.lesson_list);
	        Cursor cs = mDatabase.rawQuery("select "+EastStudy.StudyMethodDetail.ID+","+
	        		EastStudy.StudyMethodDetail.TITLE + " from "+ EastStudy.StudyMethodDetail.TABLE_NAME +
	        		" where gid=" + currentGid+" or gid=0", null);
	        msma = new MyStudyMethodAdapter(this,cs);
	        mListView.setAdapter(msma);
	        mListView.setOnItemClickListener(msma);

	        mRemember = (ImageButton) this.findViewById(R.id.remember);
	        mReduce = (ImageButton) this.findViewById(R.id.reduce);
	        mTest = (ImageButton) this.findViewById(R.id.test);
	        mAsk = (ImageButton) this.findViewById(R.id.ask);
	        mZkjx = (ImageButton) this.findViewById(R.id.zkjx);
	        mGkjx = (ImageButton) this.findViewById(R.id.gkjx);
	        mKwtz = (ImageButton) this.findViewById(R.id.kwtz);
	        mXfly = (ImageButton) this.findViewById(R.id.xfly);
	        mTbtl = (ImageButton) this.findViewById(R.id.tbtl);
	        vtv=(TextView) this.findViewById(R.id.vtitle);
	        vtv.setText(mTi.get_Title());
	        //mRemember.
	        
	        mRemember.setOnClickListener(this);
	        mReduce.setOnClickListener(this);
	        mTest.setOnClickListener(this);
	        mAsk.setOnClickListener(this);
	        mZkjx.setOnClickListener(this);
	        mGkjx.setOnClickListener(this);
	        mKwtz.setOnClickListener(this);
	        mXfly.setOnClickListener(this);
	        mTbtl.setOnClickListener(this);
	        
			mWebView = (WebView) findViewById(R.id.my_web_view);
			
			if(!mTi.is_CS()){
				mTest.setVisibility(View.GONE);
			}
			if(!mTi.is_KS()){
				mKwtz.setVisibility(View.GONE);
			}
			if(!mTi.is_TS()){
				mTbtl.setVisibility(View.GONE);
			}
			mZkjx.setVisibility(View.GONE);
			mGkjx.setVisibility(View.GONE);
			if(mTi.is_ZS()){
				if(currentGid > 6 && currentGid <11){
					mZkjx.setVisibility(View.VISIBLE);
				}else if(currentGid>10 && currentGid<14){
					mGkjx.setVisibility(View.VISIBLE);
				}
			}

	        //mWebView.setInitialScale(1);
	        WebSettings webSettings = mWebView.getSettings();
	        webSettings.setSavePassword(false);
	        webSettings.setSaveFormData(false);
	        webSettings.setJavaScriptEnabled(true);
	        webSettings.setPluginsEnabled(true);
	        webSettings.setBuiltInZoomControls(false);
	        
	        mWebView.setWebViewClient(new MyWebViewClient());  
	        mWebView.setWebChromeClient(new MyWebChromeClient());
	        mCurrentButton = R.id.remember;
	        mRemember.setSelected(true);
	        
	        if(mWeburl!=""){
	        	mWebView.loadUrl(mWeburl+FILES_COLUMNS[0]);
	        }
		}else{
			
		}

        
		
	}
protected void ShowExitdialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(StudyWebView.this);
		
		builder.setMessage("课件信息已经删除，请重新下载后学习?");
		
		builder.setTitle("提示");

		builder.setPositiveButton(android.R.string.ok,
				new android.content.DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						StudyWebView.this.finish();
					}

				});

		builder.create().show();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	       /*if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {       

	           mWebView.goBack();       
	           
	           return true;       

	       }*/
		if (keyCode == KeyEvent.KEYCODE_BACK ||KeyEvent.KEYCODE_HOME == keyCode )
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(StudyWebView.this);
	
			builder.setMessage(R.string.view_exit_msg);
	
			builder.setTitle(R.string.title_dialog_alert);
	
			builder.setPositiveButton(android.R.string.ok,
					new android.content.DialogInterface.OnClickListener() {
	
						@Override
						public void onClick(DialogInterface dialog, int which) {
	
							dialog.dismiss();
							 
							mWebView.clearCache(true);
							mWebView.destroy();
							StudyWebView.this.finish();
						}
	
					});
	
			builder.setNegativeButton(android.R.string.cancel,
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
	 
	  /** 
     * Provides a hook for calling "alert" from javascript. Useful for 
     * debugging your javascript. 
     */  
    final class MyWebViewClient extends WebViewClient {  
    	   @Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {       

               view.loadUrl(url);       

               return true;       

           }

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}  
    	   
    } 
    
    final class MyWebChromeClient extends WebChromeClient {  
        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) 
        {
            new AlertDialog.Builder(StudyWebView.this)
                .setTitle(R.string.title_dialog_alert)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,
                        new AlertDialog.OnClickListener() 
                        {
                            @Override
							public void onClick(DialogInterface dialog, int which) 
                            {
                                result.confirm();
                            }
                        })
                .setCancelable(false)
                .create()
                .show();
            
            return true;
        }

		@Override
		public boolean onCreateWindow(WebView view, boolean dialog,
				boolean userGesture, Message resultMsg) {
			return super.onCreateWindow(view, dialog, userGesture, resultMsg);
		};
    }
    private void switchButton(View v,int vid,int index){
    	if(mCurrentButton != vid){
    		this.findViewById(mCurrentButton).setSelected(false);
			v.setSelected(true);
			mCurrentButton = vid;
			if(pd!=null){
				pd.dismiss();
				pd=null;
			}
			
    		switch(vid){
    		case R.id.ask:
    			//showt =SHOWAPP;
    			relativelayout.setVisibility(View.VISIBLE);
    			mListView.setVisibility(View.GONE);
				mWebView.setVisibility(View.GONE);
				mWebView.clearAnimation();
				mVideoLesson.setVisibility(View.GONE);
				vtv.setVisibility(View.GONE);
    			break;
    		case R.id.xfly:
    			//showt =SHOWAPP;
    			mListView.setVisibility(View.VISIBLE);
				mWebView.setVisibility(View.GONE);
				mWebView.clearAnimation();
				mVideoLesson.setVisibility(View.GONE);
    			relativelayout.setVisibility(View.GONE);
				vtv.setVisibility(View.GONE);
    			break;
    		case R.id.reduce:
    			mListView.setVisibility(View.GONE);
				mWebView.setVisibility(View.GONE);
				mWebView.clearAnimation();
				mVideoLesson.setVisibility(View.VISIBLE);
    			relativelayout.setVisibility(View.GONE);
				vtv.setVisibility(View.VISIBLE);
    			break;
    		default:
    			String urls=mWeburl + FILES_COLUMNS[index];
    			System.out.println("urls:"+urls);
    			//mWebView.clearCache(true);
    			mWebView.loadUrl(urls);
    			if(exist(urls.substring(7))){
    				mWebView.loadUrl(urls);
    			}else if(exist(urls.substring(7)+"l")){
    				mWebView.loadUrl(urls + "l");
    			}else{
    				Toast.makeText(StudyWebView.this, R.string.lessonnoexist, Toast.LENGTH_LONG).show();
    			}
    			mListView.setVisibility(View.GONE);
    			mVideoLesson.setVisibility(View.GONE);
    			mWebView.setVisibility(View.VISIBLE);
    			relativelayout.setVisibility(View.GONE);
				vtv.setVisibility(View.GONE);
    		}
		}
    }
    private boolean exist(String p){
    	File f=new File(p);
    	return f.exists();
    }

	@Override
	public void onClick(View v) {
		int clickid = v.getId();
			
			switch (clickid) {
			case R.id.remember:
				switchButton(v,clickid,BTN_REMEMBER_INDEX);
				break;
			case R.id.kwtz:
				switchButton(v,clickid,BTN_KWTZ_INDEX);
				break;
			case R.id.gkjx:
				switchButton(v,clickid,BTN_GKJX_INDEX);
				break;
			case R.id.xfly:
				switchButton(v,clickid,BTN_XFLY_INDEX);
				break;
			case R.id.tbtl:
				switchButton(v,clickid,BTN_TBTL_INDEX);
				break;
			case R.id.test:
				//mWebView.loadUrl(Utiles.TEST);
				switchButton(v,clickid,BTN_TEST_INDEX);
				break;
			case R.id.zkjx:
				switchButton(v,clickid,BTN_ZKJX_INDEX);
				break;
			case R.id.reduce:
				switchButton(v,clickid,BTN_XFLY_INDEX);
				//mVideoLesson.seekTo(mVideoLesson.getCurrentPosition());
				if(vtotalsize< EastStudy.getStorageFreeSpace()+1024000){
					mVideoLesson.start();
					if(firstPlay){
						new Thread(){

							@Override
							public void run() {
								while(prefacePosition <= stopposition){
									prefacePosition = mVideoLesson.getCurrentPosition();
								}
								mVideoLesson.pause();
									
								if(mCurrentButton ==R.id.reduce && new File(mTi.get_vUrl()).exists()){
									Looper.prepare();
									pd = ProgressDialog.show(StudyWebView.this,
											"提示",
											"课件数据加载中，请稍候……", true, false);
									Looper.loop();
								}
							}
				        	
				        }.start();
				    	// TODO	
				        	playingLesson(mTi.get_vUrl());
				        
					}
					}else{
			        	pd.dismiss();
			        	Toast.makeText(this, "空间不足！请检查后重试！", Toast.LENGTH_SHORT);
			        	switchButton(mRemember,R.id.remember,BTN_REMEMBER_INDEX);
			        }
			        /*private long getVtotalsize(long lid){
		ContentResolver mResolver = this.getContentResolver();
		Cursor cursor = mResolver.query(Downloads.Success.CONTENT_URI, Downloads.DOWNLOAD_COLUMNS, Downloads.LID + " = " + lid + " AND isv=0", null, null);
		if (cursor.getCount()>0){
			long tt = cursor.getLong(2);
			cursor.close();
			return tt;
		}
		return 0l;
		//vtotalsize = getVtotalsize(mLessonID);
	}*/
				break;
			case R.id.ask:
				findViewById(R.id.ask).setClickable(!(leftnum <=0));
				if(leftnum>0){
					if(isNetworkAvailable()){
						switchButton(v,clickid,BTN_ASK_INDEX);
					}else{
						Toast.makeText(StudyWebView.this, R.string.network_message,
								Toast.LENGTH_LONG).show();
						switchButton(mRemember,R.id.remember,BTN_REMEMBER_INDEX);
					}
				}
				else{
					Toast.makeText(StudyWebView.this, R.string.numless,
							Toast.LENGTH_LONG).show();
					switchButton(mRemember,R.id.remember,BTN_REMEMBER_INDEX);
				}
				break;
			
			}
	}
	private long getVtotalsize(long lid){
		ContentResolver mResolver = this.getContentResolver();
		Cursor cursor = mResolver.query(Downloads.Success.CONTENT_URI, Downloads.DOWNLOAD_COLUMNS, Downloads.LID + " = " + lid + " AND isv=0", null, null);
		if (cursor.moveToFirst()){
			long tt = cursor.getLong(2);
			cursor.close();
			return tt;
		}
		return 0l;
		//vtotalsize = getVtotalsize(mLessonID);
	}
	@Override
    protected void onPause() {
        super.onPause();
        mVideoLesson.pause();
    }
	@Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoLesson.stopPlayback();
        if(mDatabase!=null){
        	mDatabase.close();
        }
		if(decFileName!=null && decFileName.length()>5)
		{
			File file = new File(decFileName);
	        file.delete();
		}
		saveSharedPreferences();
    }
	private void playingLesson(String uri) {

		new DecryptTask().execute(uri);
	}
	/* 上传文件至Server，uploadUrl：接收文件的处理页面 */

	
	public String post(String actionUrl, String FileName,
			String title_value, String contentall, String grade_id,
			String subject_id)

	throws IOException {

		StringBuilder sb2 = new StringBuilder();
		String BOUNDARY = java.util.UUID.randomUUID().toString();

		String PREFIX = "--", LINEND = "\r\n";

		String MULTIPART_FROM_DATA = "multipart/form-data";

		URL uri = new URL(actionUrl);

		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

		conn.setReadTimeout(20 * 1000);

		// 缓存的最长时间

		conn.setDoInput(true);// 允许输入

		conn.setDoOutput(true);// 允许输出

		conn.setUseCaches(false); // 不允许使用缓存

		conn.setRequestMethod("POST");

		conn.setRequestProperty("connection", "keep-alive");

		conn.setRequestProperty("Charsert", ENCODE);

		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA

		+ ";boundary=" + BOUNDARY);

		StringBuilder textEntity = new StringBuilder();
		Map<String, String> params = new HashMap<String, String>();
		
		params.put("tq_ask_grade", grade_id);
		params.put("tq_ask_subject", subject_id);
		params.put("tq_ask_title", title_value);
		params.put("tq_ask_content", contentall);
		params.put("tq_ask_sid", AskUID+"");
		params.put("tq_ask_rand", RANDOM);

		DataOutputStream outStream = new DataOutputStream(

		conn.getOutputStream());

		if (FileName != "") {

			StringBuilder sb1 = new StringBuilder();

			sb1.append(PREFIX);

			sb1.append(BOUNDARY);

			sb1.append(LINEND);

			sb1.append("Content-Disposition: form-data; name=\"file1\"; filename=\""

					+ FileName + "\"" + LINEND);

			sb1.append("Content-Type: application/octet-stream; charset="

			+ ENCODE + LINEND);

			sb1.append(LINEND);

			outStream.write(sb1.toString().getBytes());

			InputStream is = new FileInputStream(FileName);

			byte[] buffer = new byte[1024];

			int len = 0;

			while ((len = is.read(buffer)) != -1) {

				outStream.write(buffer, 0, len);

			}

			is.close();

			outStream.write(LINEND.getBytes());
		}

			for (Map.Entry<String, String> entry : params.entrySet()) {
				// 构造文本类型参数的实体数据
				textEntity.append("--");
				textEntity.append(BOUNDARY);
				textEntity.append("\r\n");
				textEntity.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"\r\n\r\n");
				textEntity.append(URLEncoder.encode(entry.getValue(),"GBK"));
				textEntity.append("\r\n");
			}

			outStream.write(textEntity.toString().getBytes());



		// 请求结束标志

		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();

		outStream.write(end_data);

		outStream.flush();

		// 得到响应码

		int res = conn.getResponseCode();

		InputStream in = null;

		if (res == 200) {

			in = conn.getInputStream();

			int ch;
			while ((ch = in.read()) != -1) {

				sb2.append((char) ch);

			}

		} else {
			return null;
		}
		return sb2.toString();

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == SELECT_IMAGE && data != null) {
			width = 800;//edit.getWidth();
			height = 600;//edit.getHeight();

			Uri path1 = data.getData();
			ContentResolver resolver = getContentResolver();
			Bitmap cameraBitmap;
			try {
				cameraBitmap = MediaStore.Images.Media.getBitmap(resolver,
						path1);
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor actualimagecursor = managedQuery(path1, proj, null,
						null, null);
				int actual_image_column_index = actualimagecursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				actualimagecursor.moveToFirst();
				String img_path = actualimagecursor
						.getString(actual_image_column_index); // 得到真实路径
				// 显得到bitmap图片
				Bitmap newbm = this.resizeBitmap(cameraBitmap, width, height);
				ImageSpan span = new ImageSpan(newbm, ImageSpan.ALIGN_BOTTOM);
				SpannableString spannableString = new SpannableString("@@@"
						+ img_path.toString() + "###");
				spannableString.setSpan(span, 0,
						("@@@" + img_path.toString() + "###").length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				if (pic_count == 0) {
					edit.append(spannableString);
					pic_count = 1;
				} else {
					setNewText();
					edit.append(spannableString);
					pic_count = 1;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		// 拍照
		if (requestCode == PHOTOHRAPH && requestCode != 0) {
			// 设置文件保存路径这里放在跟目录下
			//File picture = new File(EastStudy.STORAGE_PATH
			//		+ "/temp.jpg");
			//startPhotoZoom(Uri.fromFile(picture));
			width = 800;//edit.getWidth()-100;
			height = 600;//edit.getHeight()-100;

			//Uri path1 = data.getData();
			//ContentResolver resolver = getContentResolver();
			Bitmap cameraBitmap;
			try {
				/*cameraBitmap = MediaStore.Images.Media.getBitmap(resolver,
						path1);
				String[] proj = { MediaStore.Images.Media.DATA };
				Cursor actualimagecursor = managedQuery(path1, proj, null,
						null, null);
				int actual_image_column_index = actualimagecursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				actualimagecursor.moveToFirst();*/
				String img_path = EastStudy.STORAGE_PATH + "/temp.jpg"; // 得到真实路径
				cameraBitmap = BitmapFactory.decodeFile(img_path);
				// 显得到bitmap图片
				Bitmap newbm = this.resizeBitmap(cameraBitmap, width, height);
				ImageSpan span = new ImageSpan(newbm, ImageSpan.ALIGN_BOTTOM);
				SpannableString spannableString = new SpannableString("@@@"
						+ img_path.toString() + "###");
				spannableString.setSpan(span, 0,
						("@@@" + img_path.toString() + "###").length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				if (pic_count == 0) {
					edit.append(spannableString);
					pic_count = 1;
				} else {
					setNewText();
					edit.append(spannableString);
					pic_count = 1;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/*// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {
			startPhotoZoom(data.getData());
		}*/
		// 处理结果
		if (requestCode == PHOTORESOULT && requestCode != 0 && data!=null) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				width = 800;//edit.getWidth();
				height = 600;//edit.getHeight();
				Bitmap photo = extras.getParcelable("data");

				Date date = new Date();
				Long timecout = date.getTime();
				String times = String.valueOf(timecout);
				try {
					savePic(photo, times + ".png");
				} catch (IOException e) {
					e.printStackTrace();
				}
				String path1 = path + "/" + times + ".png";

				Bitmap newbm = this.resizeBitmap(photo, width, height);
				ImageSpan span = new ImageSpan(newbm, ImageSpan.ALIGN_BOTTOM);
				SpannableString spannableString = new SpannableString("@@@"
						+ path1.toString() + "###");
				spannableString.setSpan(span, 0,
						("@@@" + path1.toString() + "###").length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

				path1 = "";
				if (pic_count == 0) {
					edit.append(spannableString);
					pic_count = 1;
				} else {
					setNewText();
					edit.append(spannableString);
					pic_count = 1;
				}
			}
		}
		if(resultCode==10){
			super.onActivityResult(requestCode, resultCode, data);	
		}
		
		if (resultCode == 6) {

			Bundle bunde = data.getExtras();
			String file_path = bunde.getString("filepath");
			String imag_name = bunde.getString("filename");
			width = 800;//edit.getWidth();
			height = 600;//edit.getHeight();
			Uri path1 = Uri.parse(file_path + imag_name + ".png");

			Bitmap cameraBitmap = BitmapFactory.decodeFile(file_path
					+ imag_name + ".png");
			Bitmap newbm = this.resizeBitmap(cameraBitmap, width, height);
			ImageSpan span = new ImageSpan(newbm, ImageSpan.ALIGN_BOTTOM);
			SpannableString spannableString = new SpannableString("@@@"
					+ path1.toString() + "###");
			spannableString.setSpan(span, 0,
					("@@@" + path1.toString() + "###").length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			if (pic_count == 0) {
				edit.append(spannableString);
				pic_count = 1;
			} else {
				setNewText();
				edit.append(spannableString);
				pic_count = 1;
			}

		}
		super.onActivityResult(requestCode, resultCode, data);

	}
	/**
	 * 编辑图片
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intentCarema = new Intent("com.android.camera.action.CROP");
		intentCarema.setDataAndType(uri, "image/*");
		intentCarema.putExtra("crop", true);
		intentCarema.putExtra("scale", true);
		//intentCarema.putExtra("noFaceDetection", true);//不需要人脸识别功能
		//intentCarema.putExtra("circleCrop", "");//设定此方法选定区域会是圆形区域
		//aspectX aspectY是宽高比例
//		intentCarema.putExtra("aspectX", 1);
//		intentCarema.putExtra("aspectY",1);
		//outputX outputY	是裁剪图片的宽高
		intentCarema.putExtra("outputX", 200);
		intentCarema.putExtra("outputY", 200);
		intentCarema.putExtra("return-data", true);
		startActivityForResult(intentCarema, PHOTORESOULT);
	}
	/**
	 * @param bitmap
	 * @param maxWidth
	 * @param maxHeight
	 * @return 缩小图片
	 */
	public Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {

		return Bitmap.createScaledBitmap(bitmap, maxWidth, maxHeight, false);
		/*Bitmap bm = null;

		int originWidth = bitmap.getWidth();
		int originHeight = bitmap.getHeight();
		if (originWidth <= maxWidth) {
			bm = bitmap;
		}
		int width = originWidth;
		int height = originHeight;
		// 若图片过宽, 则保持长宽比缩放图片
		if (originWidth > maxWidth) {
			width = maxWidth;

			double i = originWidth * 1.0 / maxWidth;
			height = (int) Math.floor(originHeight / i);
			bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
			bm = bitmap;
		}

		return bm;*/
	}
	// 保存图片
		public void savePic(Bitmap b, String strFileName) throws IOException {

			File f = new File(path);
			if (!f.exists()) {
				try {
					// 按照指定的路径创建文件夹
					f.mkdirs();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			File dir = new File(path + "/.nomedia");

			if (!dir.exists()) {

				try {

					// 在指定的文件夹中创建文件

					dir.createNewFile();

				} catch (Exception e) {

				}

			}
			System.out.print("<-------------------------------------");

			// f.createNewFile();
			File file2 = new File(f , strFileName);
			FileOutputStream fOut = new FileOutputStream(file2);
//			try {
//				fOut = new FileOutputStream(f);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
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

		public void setNewText() {
			String context = edit.getText().toString();
			edit.setText("");
			String[] edit_Array = context.split("@@@");
			for (int i = 0; i < edit_Array.length; i++) {
				if (edit_Array[i].lastIndexOf("###") != -1) {
					String[] pic_Array = edit_Array[i].split("###");
					for (int j = 0; j < pic_Array.length; j++) {
						if (j == 0) {
							Uri spic_path = Uri.parse(pic_Array[0]);
							Bitmap cameraBitmap1 = BitmapFactory
									.decodeFile(spic_path.toString());
							Bitmap newbm = this.resizeBitmap(cameraBitmap1, 800,
									600);//10000000);
							ImageSpan span = new ImageSpan(newbm,
									ImageSpan.ALIGN_BOTTOM);
							SpannableString spannableString = new SpannableString(
									"@@@" + spic_path.toString() + "###");
							spannableString
									.setSpan(span, 0,
											("@@@" + spic_path.toString() + "###")
													.length(),
											Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							edit.append("");
						}
						if (j == 1) {
							if (!pic_Array[1].equals("")) {
								edit.append(pic_Array[1]);

							}

						}

					}
					width = edit.getWidth();
					height = edit.getHeight();
				} else {
					edit.append(edit_Array[i]);
				}

			}

		}
	private void playingDecrpyLesson(String uri) {
		
		if(mCurrentButton == R.id.reduce){
			mVideoLesson.setVisibility(View.INVISIBLE);
			mVideoLesson.clearAnimation();
			mVideoLesson.setVideoURI(Uri.parse(uri));
			mVideoLesson.setVisibility(View.VISIBLE);
			mVideoLesson.start();
			firstPlay=false;
		}
		
	}
	private class DecryptTask extends AsyncTask<String, Long, String> {
		//private ProgressDialog mProgressDialog;
		
		@Override
		protected void onPostExecute(String strUri) {
			//mProgressDialog.dismiss();
			//mProgressDialog = null;
			if(pd!=null){
				pd.dismiss();
				pd=null;
			}
			if (strUri.equals("")) {
				// 处理失败信息
				final Resources res = StudyWebView.this.getResources();
				AlertDialog.Builder lgbuilder = new AlertDialog.Builder(
						StudyWebView.this);
				lgbuilder.setTitle(R.string.app_name);
				lgbuilder.setMessage("该课件已被删除！");

				lgbuilder
						.setPositiveButton(res.getString(android.R.string.ok),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).create().show();
			} else
				playingDecrpyLesson(strUri);
		}

		@Override
		protected String doInBackground(String... prarms) {
			try {
				decFileName = vd.DecryptVideo(prarms[0]);
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (ShortBufferException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				e.printStackTrace();
			}
			return decFileName;
		}

	}

	private class MyStudyMethodAdapter extends CursorAdapter implements
			OnClickListener,OnItemClickListener {
		public MyStudyMethodAdapter(Context context, Cursor cursor) {
			super(context, cursor, true);
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			final Cursor cursor = (Cursor) getItem(position);
			if (view == null) {
				view = newView(StudyWebView.this, cursor, parent);
			}

			bindNewView(view, position, cursor);
			return view;
		}

		private void bindNewView(View view, int position, Cursor cursor) {
			
			((TextView) view.findViewById(R.id.hori_lesson_text))
					.setText(cursor.getString(EastStudy.StudyMethodDetail.TITLE_INDEX));
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {

		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return LayoutInflater.from(context).inflate(
					R.layout.my_study_method_item, null);
		}

		@Override
		public void onClick(View view) {
			final int position = (Integer) view.getTag();
			final Cursor cursor = (Cursor) getItem(position);
			switch (view.getId()) {
			case R.id.hori_lesson_delete:
				long id = cursor.getLong(EastStudy.StudyMethodDetail.ID_INDEX);
				mDatabase.rawQuery("delete from " + EastStudy.StudyMethodDetail.TABLE_NAME +
						" where _id=" + id, null);
				mDatabase.rawQuery("delete from " + EastStudy.StudyMethodDetailPic.TABLE_NAME +
						" where _id=" + id, null);
				
				break;
			}
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Cursor cs = mDatabase.rawQuery("select " + EastStudy.StudyMethodDetail.ID + ","
					+ EastStudy.StudyMethodDetail.TITLE + ","
					+ EastStudy.StudyMethodDetail.CONTENT
					+ " from "+EastStudy.StudyMethodDetail.TABLE_NAME +
					" where _id=" + id,null);
			if(cs.moveToFirst()){
				mWebView.loadDataWithBaseURL(null, cs.getString(EastStudy.StudyMethodDetail.CONTENT_INDEX), "text/html", "utf-8", null);
				mWebView.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
			}
			cs.close();
		}
	}
	@Override
	public void onCompletion(MediaPlayer mp) {
		mVideoLesson.stopPlayback();
		StudyWebView.this.finish();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}
	private void loadSharedPreferences() {
		mSharedPreferences = getSharedPreferences(ASKLEFTNUM,
				Context.MODE_PRIVATE);
		/*mSettings[0] = mSharedPreferences.getBoolean(SAVE_PATH, true);
		// mSettings[0] = mSharedPreferences.getBoolean(AUTO_ORIENTATION,
		// true);
		mSettings[1] = mSharedPreferences.getBoolean(SAVE_DOWNLOAD, false);
		mSettings[2] = mSharedPreferences
				.getBoolean(DOWNLOAD_CONFIRM, true);
		mSelectedSite = mSharedPreferences.getInt(SELECTED_SITE, 0);
		mSelectedGrade = mSharedPreferences.getInt(ConstUtiles.SELECTED_GRADE,
				0);
		pSelectedGrade = mSharedPreferences.getInt(ConstUtiles.SELECTED_GP,
				0);
		pSelectedChild = mSharedPreferences.getInt(ConstUtiles.SELECTED_CP,
				0);*/
		leftnum = mSharedPreferences.getInt(ASKLEFTNUM, 0);
		/*checkurl = mSharedPreferences.getString(ConstUtiles.CHECKURL, "");
		uid = mSharedPreferences.getInt(ConstUtiles.USRID,
				0);
		randSerialNo = mSharedPreferences.getString(ConstUtiles.RANDUM, "");*/

	}


	private void saveSharedPreferences() {
		Editor editor = mSharedPreferences.edit();

		editor.putInt(ASKLEFTNUM, leftnum);
		editor.commit();
	}

}
