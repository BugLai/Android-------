package com.tqxktbyw.study.packer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tqxktbyw.study.EastStudy;
import com.tqxktbyw.study.MainActivity;
import com.tqxktbyw.study.R;
import com.tqxktbyw.study.ZipUtils;
import com.tqxktbyw.study.EastStudy.StudyMethodInfo;
import com.tqxktbyw.study.database.EastStudyDatabase;
import com.tqxktbyw.study.entity.StudyMethod;
import com.tqxktbyw.study.entity.StudyMethodDetail;

public class ListFile extends Activity
{
	private static final String PREFERENCES_NAME = "fileInfo"; // xml表名
	private SharedPreferences mSharedPreferences;
	private EastStudyDatabase mDatabase;
	
	private String mSDdespath = "/mnt/external_sd/tqxktbyw/";
	
	public native int depack(String despath, String srcpath);// 本地方法（ 解密）

	private List<String> mFileName = null; // 文件名列表
	private List<String> mFilePaths = null; // 文件路径列表

	private List<FileInfo> lxmlList = new ArrayList<FileInfo>();
	private List<Filexml> filesXmlList = new ArrayList<Filexml>(); // 保存xml的person节点
	private MyXmlUtil xmlPull = new MyXmlUtil(this);
	List<StudyMethod> listStudy;
	List<StudyMethodDetail> listStudy_new;

	private String mRootPath = "/"; // root路径
	private String mExternalPath = "/mnt/external_sd"; // TF卡路径
	private String mSDCard = Environment.getExternalStorageDirectory().toString(); // SD卡路径
	public static String mCurrentFilePath = ""; // 当前目录路径信息

	private long exitTime = 0;
	private int listsize;
	private Thread thread;
	private TextView mPath;
	private AlertDialog alertDialog = null; // 全局
	private int infoFromC; // 解压状态标志位
	private ProgressDialog pdDialog;

	TestEXCard testEXCard = new TestEXCard(); // 对SD和TF卡的操作工具类
	ListView list;
	FileAdapter adapter;
	private boolean onTag = true; // 按键标志位
	private boolean EX_sdTag = true;

	/*
	 * 程序入口
	 */

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.wenianliulan);

		pdDialog = new ProgressDialog(this);
		list = (ListView) findViewById(R.id.list);
		mPath = (TextView) findViewById(R.id.list_text);

		/*
		 * 初始化
		 */
		mDatabase = new EastStudyDatabase(this);
		initFileListInfo(mSDCard);
		listjianting();

	}

	/*
	 * 接收消息，更新UI
	 */
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case 0:
					Toast.makeText(ListFile.this, "导入文件错误或不完整，请导入正确的文件！", Toast.LENGTH_LONG).show();
					ViewHolder viewHolder_error = new ViewHolder();
					View arg1_error = (View) msg.obj;
					viewHolder_error.mTV = (TextView) arg1_error.findViewById(R.id.text2_list_childs);
					viewHolder_error.mTV.setText("文件损坏");
					viewHolder_error.mTV.setTextColor(Color.RED);
					break;
				case 1:
					Toast.makeText(ListFile.this, "导入成功，请返回查看！", Toast.LENGTH_LONG).show();
					ViewHolder viewHolder_success = new ViewHolder();
					View arg1_success = (View) msg.obj;
					viewHolder_success.mTV = (TextView) arg1_success.findViewById(R.id.text2_list_childs);
					viewHolder_success.mTV.setText("导入成功");
					viewHolder_success.mTV.setTextColor(Color.GREEN);
					break;
				case 2:
					adapter.notifyDataSetChanged();
					break;
				default:
					break;
			}
		};
	};

	/*
	 * 初始化，遍历文件
	 */
	private void initFileListInfo(String filePath)
	{
		// 加载xml
		xmlPull = new MyXmlUtil(this);// 初始化xml文件
		lxmlList = xmlPull.pullXMLResolve();

		// 文件大小计算工具类
		TestEXCard testEXCard = new TestEXCard();

		// 格式化数字
		DecimalFormat format = new DecimalFormat();
		format.setGroupingSize(3);
		String tf_total = format.format(testEXCard.FileFolder_All_Size(mExternalPath));
		String tf_avaliable = format.format(testEXCard.FileFolder_Free_Size(mExternalPath));
		String total = format.format(testEXCard.FileFolder_All_Size(mSDCard));
		String available = format.format(testEXCard.FileFolder_Free_Size(mSDCard));

		// 当前路径
		mCurrentFilePath = filePath;
		/*
		 * 如果是SD卡路径
		 */
		if (mCurrentFilePath.equals(mSDCard))
		{
			/*
			 * 空间不足提醒
			 */
			if (testEXCard.FileFolder_Free_Size(mSDCard) < 500)
			{
				mPath.setText("  当前位置：" + filePath + "  SD卡可用空间:" + available + " M" + "  剩余空间不足500M，请及时清理!");
				mPath.setTextColor(Color.RED);
			}
			else
			{
				mPath.setText("  当前位置：" + filePath + "       SD卡总空间：" + total + " M" + "       剩余空间：" + available + " M");
			}
		}
		/*
		 * 如果是TF卡路径
		 */
		else if (mCurrentFilePath.equals(mExternalPath))
		{
			/*
			 * 空间不足提醒
			 */
			if (testEXCard.FileFolder_Free_Size(mExternalPath) < 500)
			{
				mPath.setText("  当前位置：" + filePath + "  TF卡可用空间:" + tf_avaliable + " M" + "  剩余空间不足500M，请及时清理!");
				mPath.setTextColor(Color.RED);
			}
			else
			{
				mPath.setText("  当前位置：" + filePath + "       TF卡总空间：" + tf_total + " M" + "       TF卡剩余空间：" + tf_avaliable + " M");
			}
		}
		/*
		 * 如果是其他路径
		 */
		else
		{
			mPath.setText("  当前位置：" + filePath);
		}
		// 创建文件名和路径数组
		mFileName = new ArrayList<String>();
		mFilePaths = new ArrayList<String>();
		File mFile = new File(filePath);
		File[] mFiles = mFile.listFiles();
		/**
		 * 初始化，添加返回功能
		 */
		initAddBackUp(filePath);

		/* 将所有文件信息添加到集合中 */
		for (File mCurrentFile : mFiles)
		{
			/**
			 * 先添加，只显示后缀为tq的文件
			 */
			final File msFile = new File(mCurrentFile.getPath());
			String Extension = "tq";
			if (!msFile.getName().substring(0, 1).equals(".") && (msFile.getPath().substring(msFile.getPath().length() - Extension.length()).equals(Extension) ))
			{
				mFileName.add(mCurrentFile.getName());
				mFilePaths.add(mCurrentFile.getPath());
			}
		}
		for (File mCurrentFile : mFiles)
		{
			/**
			 * 后添加文件夹，如果不要文件夹，就注释掉这里
			 */
			final File msFile = new File(mCurrentFile.getPath());
			if (!msFile.getName().substring(0, 1).equals(".") && ( msFile.isDirectory()))
			{
				mFileName.add(mCurrentFile.getName());
				mFilePaths.add(mCurrentFile.getPath());
			}
		}
		/* 适配数据 */
		adapter = new FileAdapter(ListFile.this, mFileName, mFilePaths);
		list.setAdapter(adapter);
	}

	/**
	 * 添加返回功能，返回TF卡目录，返回上一层
	 */
	private void initAddBackUp(String filePath)
	{

		if (!filePath.equals(mRootPath))
		{
			/* 列表项的第一项设置为返回根目录 */
			/*
			 * 如果当前是TF卡目录，则第一项设置为返回SD卡
			 */
			if (filePath.equals(mExternalPath))
			{
				mFileName.add("BacktoSD");
				mFilePaths.add(mSDCard);
			}
			/*
			 * 如果当前是SD卡目录，则第一项设置为返回TF卡
			 */
			else if (filePath.equals(mSDCard))
			{
				mFileName.add("BacktoEX");
				mFilePaths.add(mExternalPath);
			}

			/* 列表项的第二项设置为返回上一级,sd,tf为顶级目录 */
			if (!filePath.equals(mSDCard) && !filePath.equals(mExternalPath))
			{
				mFileName.add("BacktoUp");
				mFilePaths.add(new File(filePath).getParent());// 回到当前目录的父目录即回到上级
			}

		}

	}

	/**
	 * /数据 适配器 继承 BaseAdapter
	 */
	class FileAdapter extends BaseAdapter
	{

		private Bitmap mFolder; // 文件夹图标
		private Bitmap mTq; // tq文件专属图标
		private Bitmap backImage; // 背景图标
		private Bitmap sanjiaoImage; // 小三角图标
		private Bitmap logo; // 返回TF SD图标
		private Bitmap backup; // 返回 上一层图标
		private Context mContext;
		private List<String> mFileNameList; // 文件名字列表
		private List<String> mFilePathList; // 文件名字对应路径列表

		public FileAdapter(Context context, List<String> fileName, List<String> filePath)
		{
			// 初始化
			mContext = context;
			mFileNameList = fileName;// 文件名字列表
			mFilePathList = filePath;// 文件名字对应路径列表
			mTq = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tqimage);
			mFolder = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.folder);
			backImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.lib_back);
			logo = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tfsdlogo);
			backup = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.back_to_root);
			sanjiaoImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.meta_item_more);
		}

		// 返回总数
		public int getCount()
		{
			return mFileNameList.size();
		}

		// 返回点击项目
		public Object getItem(int position)
		{
			return mFileNameList.get(position);
		}

		// 返回点击位置
		public long getItemId(int position)
		{
			return position;
		}

		// 设置项目
		public View getView(int position, View convertView, ViewGroup viewgroup)
		{
			ViewHolder viewHolder = null;
			String mFileName = mFilePathList.get(position).toString();
			File mFile = new File(mFileName);
			lxmlList = xmlPull.pullXMLResolve();
			/**
			 * 初始化
			 */
			viewHolder = new ViewHolder();
			LayoutInflater mLI = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mLI.inflate(R.layout.listview, null);
			viewHolder.mIV = (ImageView) convertView.findViewById(R.id.image_list_childs);
			viewHolder.mTV = (TextView) convertView.findViewById(R.id.text_list_childs);
			viewHolder.mTV2 = (TextView) convertView.findViewById(R.id.text2_list_childs);
			viewHolder.backImage = (ImageView) convertView.findViewById(R.id.backImage);
			/**
			 * 第一项和第二项功能设置
			 */
			if (mFileNameList.get(position).toString().equals("BacktoUp"))
			{
				viewHolder.mTV2.setVisibility(View.GONE);
				viewHolder.mIV.setImageBitmap(backup);
				viewHolder.backImage.setImageBitmap(backImage);
				viewHolder.mTV.setText("上一层");

				viewHolder.mTV.setTextSize(22);
				viewHolder.mTV.setTextColor(R.color.leftorange);
			}
			else if (mFileNameList.get(position).toString().equals("BacktoSD"))
			{
				viewHolder.backImage.setImageBitmap(backImage);
				viewHolder.mIV.setImageBitmap(logo);//
				viewHolder.mTV.setText("打开内置SD卡");
				viewHolder.mTV.setTextColor(R.color.leftorange);
			}
			else if (mFileNameList.get(position).toString().equals("BacktoEX"))
			{
				viewHolder.backImage.setImageBitmap(backImage);
				/**
				 * 检测TF卡，如果没有就给出提示
				 */
				TestEXCard testEXCard = new TestEXCard();
				// 如果没有检测到SD卡
				if (!testEXCard.isSDCardAvailable())
				{
					EX_sdTag = false;
					viewHolder.mTV.setText("检测不到外置TF卡！");
					viewHolder.mTV.setTextColor(Color.RED);
				}
				else
				{
					EX_sdTag = true;
					viewHolder.mIV.setImageBitmap(logo);//
					viewHolder.mTV.setText("打开外置TF卡");
					viewHolder.mTV.setTextColor(R.color.leftorange);
				}
			}
			else
			{

				if (mFile.isDirectory())// 如果是文件夹,设置文件夹背景
				{
					viewHolder.mIV.setImageBitmap(mFolder);
				}
				else
				{
					viewHolder.mIV.setImageBitmap(mTq);
					// 读xmL
					if (!lxmlList.isEmpty())
					{
						FileInfo fileInfo_set = new FileInfo(mFileNameList.get(position).toString(), mFilePathList.get(position).toString(), 0, 0);
						int status_file = lxmlList.indexOf(fileInfo_set);
						if (status_file != -1)
						{
							if (lxmlList.get(status_file).getFile_Status() == 1)
							{
								viewHolder.mTV2.setText("已经导入");
								viewHolder.mTV2.setTextColor(Color.GREEN);
							}
							else
							{
								viewHolder.mTV2.setText("文件损坏");
								viewHolder.mTV2.setTextColor(Color.RED);
							}
						}
						else
						{
							Log.i("未找到文件", "");
						}
					}
					else
					{
						Log.i("XML文件无数据", "执行时间" + System.currentTimeMillis());
					}
				}

				viewHolder.mIV.setVisibility(View.VISIBLE);
				viewHolder.backImage.setImageBitmap(sanjiaoImage);
				viewHolder.mTV.setText(mFile.getName());
			}
			return convertView;
		}

	}

	/**
	 * c层更新进度
	 */
	public void setprogress(int progress)
	{
		pdDialog.setProgress(progress);
	}

	// 列表监听
	public void listjianting()
	{

		list.setOnItemClickListener(new ListView.OnItemClickListener()
		{

			public void onItemClick(AdapterView<?> arg0, final View arg1, final int arg2, long arg3)
			{
				// TODO Auto-generated method stubrfd
				final File mfile = new File(mFilePaths.get(arg2));
				xmlPull = new MyXmlUtil(ListFile.this);// 初始化xml文件
				lxmlList = xmlPull.pullXMLResolve();
				//final String mSDdespath = EastStudy.DOWNLOAD_PATH + File.separator;// 目的
				final String mSDsrcPath = mfile.getPath();// 源
				if (mfile.canRead())
				{// 如果该文件是可读的，我们进去查看文件
					if (mfile.isDirectory())
					{// 如果是文件夹，则直接进入该文件夹，查看文件目录
						initFileListInfo(mFilePaths.get(arg2));
					}
					else
					{// 如果是文件

						if (onTag == true)// 如果可以点击
						{
							onTag = false;// 避免多次点击，多次响应
							if ((alertDialog != null && alertDialog.isShowing())) // 限制唯一弹框
							{
								return;
							}
							Builder builder = new AlertDialog.Builder(ListFile.this);
							builder.setIcon(android.R.drawable.ic_dialog_alert).setTitle("数据导入操作").setMessage("当前文件： " + mfile.getName()).setPositiveButton("删除文件", new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int which)

								{

									if (!mfile.exists())
									{
										Toast.makeText(ListFile.this, "文件不存在了！", Toast.LENGTH_SHORT).show();
										onTag = true;// 避免多次点击，多次响应
									}
									// 删除二次确认
									else
									{
										AlertDialog.Builder builder2 = new AlertDialog.Builder(ListFile.this);
										builder2.setMessage("确认删除？").setCancelable(false).setPositiveButton("确认", new DialogInterface.OnClickListener()
										{
											public void onClick(DialogInterface dialoginterface, int i)
											{
												// 如果文件不存在，不删除，只弹出TOAST
												if (mfile.exists())
												{
													FileInfo Info1 = new FileInfo(mfile.getName(), mfile.getPath(), 0, 0);
													int indx = lxmlList.indexOf(Info1);

													mFileName.remove(arg2);
													mFilePaths.remove(arg2);
													// 如果xml中有记录
													if (lxmlList.contains(Info1))
													{
														lxmlList.remove(indx);
													}
													mfile.delete();
													xmlPull.pullXMLCreate(lxmlList);
													Message msg = new Message();
													msg.what = 2;
													mHandler.sendMessage(msg);

													Toast.makeText(ListFile.this, "文件已删除！", Toast.LENGTH_SHORT).show();

												}
												onTag = true;// 避免多次点击，多次响应
											}
										})
										// 按键--取消，不操作
												.setNegativeButton("取消", new DialogInterface.OnClickListener()
												{
													public void onClick(DialogInterface dialoginterface, int i)
													{
														onTag = true;// 避免多次点击，多次响应

													}
												}).create().show();
									}
								}
							}).setNegativeButton("导入文件", new DialogInterface.OnClickListener()
							{

								public void onClick(DialogInterface dialog, int which)
								{
									// 设置进度条风格，风格为圆形，旋转的
									pdDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
									// 设置ProgressDialog
									// 标题
									pdDialog.setTitle("数据导入");
									// 设置ProgressDialog
									// 提示信息
									pdDialog.setMessage("正在导入，请稍候..");
									// 设置ProgressDialog
									// 标题图标
									pdDialog.setIcon(android.R.drawable.ic_dialog_alert);
									// 设置ProgressDialog的最大进度
									long size = mfile.length();

									pdDialog.setMax((int) size);
									pdDialog.setProgress(0);
									pdDialog.setCancelable(false);
									
									/**
									 * 如果SD卡不存在，判断空间大小
									 */
									if (EX_sdTag ==false)
									{
									 mSDdespath = "/mnt/sdcard/tqxktbyw/";
									if ((testEXCard.FileFolder_Free_Size(mSDCard) * 1024 * 1024) >= size) // 还有空间吗
									{
										pdDialog.show();
										thread = new Thread()
										{

											@Override
											public void run()
											{

												infoFromC = depack(mSDdespath, mSDsrcPath);
												/*
												 * infofromc
												 * 0
												 * 失败
												 * 1
												 * 成功
												 */
												if (infoFromC == 0)
												{
													FileInfo fileInfo_0 = new FileInfo(mfile.getName(), mSDsrcPath, 0, 0);
													pdDialog.dismiss();
													Message msg = new Message();
													msg.what = 0;
													msg.obj = arg1;
													mHandler.sendMessage(msg);

													if (!lxmlList.contains(fileInfo_0))
													{
														Log.i("添加一条", "错误文件");
														listsize++;
														fileInfo_0.setFile_Name(mfile.getName());
														fileInfo_0.setFile_Path(mSDsrcPath);
														fileInfo_0.setFile_Status(0);
														lxmlList.add(fileInfo_0);
														xmlPull.pullXMLCreate(lxmlList);
													}

												}
												else
												{
													FileInfo fileInfo_1 = new FileInfo(mfile.getName(), mSDsrcPath, 0, 0);
													pdDialog.dismiss();
													Message msg = new Message();
													msg.what = 1;
													msg.obj = arg1;
													mHandler.sendMessage(msg);

													if (!lxmlList.contains(fileInfo_1))
													{
														Log.i("添加一条", "成功文件");
														listsize++;
														fileInfo_1.setFile_Name(mfile.getName());
														fileInfo_1.setFile_Path(mSDsrcPath);
														fileInfo_1.setFile_Status(1);
														lxmlList.add(fileInfo_1);

														/*
														 * 写smi表
														 */
														listStudy = readUser(mSDdespath + "smi.xml");
														try
														{
															for (StudyMethod sm : listStudy)
															{
																if (sm.get_nid() > 0)
																{
																	replaceSMI(sm, "http://pad2.tqpad.com/file_News/Studymethod/");
																}
															}

														}
														catch (Exception e)
														{
															e.printStackTrace();
														}
														/*
														 * 写smi表结束
														 */
														/*
														 * 写smd
														 */
														listStudy_new = readStudy(mSDdespath + "smd.xml");
														if (listStudy_new.size() > 0)
														{
															for (StudyMethodDetail smd : listStudy_new)
															{
																MainActivity.UpdateSMDetail(smd);
															}
														}
														/*
														 * 写smd结束
														 */
														/*
														 * 写lesson
														 */
														filesXmlList = xmlPull.pullXMLFromC(mSDdespath + "lesson.xml");
														String tableName = EastStudy.Lesson.TABLE_NAME;
														String tableName_teacher = EastStudy.Teacher.TABLE_NAME;
														for (int i = 0; i < filesXmlList.size(); i++)
														{
															ContentValues values = new ContentValues();
															ContentValues values_teacher = new ContentValues();

															values_teacher.put("_id", filesXmlList.get(i).getTeacher_id());
															values_teacher.put("ver", filesXmlList.get(i).getTeacher_ver());
															values_teacher.put("vurl", mSDdespath + filesXmlList.get(i).getTeacher_url());

															values.put("status", 4);
															values.put("url", mSDdespath + filesXmlList.get(i).getFile_url());
															values.put("zipname", mSDdespath + filesXmlList.get(i).getFile_zip());

															String[] args_id =
															{ String.valueOf(filesXmlList.get(i).getFile_id()) };
															SQLiteDatabase db = mDatabase.getWritableDatabase();
															db.update(tableName, values, "_id=?", args_id);

															db.replace(tableName_teacher, null, values_teacher);
															String zip_pathString = mSDdespath + filesXmlList.get(i).getFile_zip() + ".zip";
															File file_zipFile = new File(zip_pathString);
															try
															{
																// 解压完再删除
																ZipUtils.decompress(zip_pathString, mSDdespath);
															}
															catch (Exception e)
															{
																e.printStackTrace();
															}
															if (file_zipFile.exists())
															{
																file_zipFile.delete();
															}
														}
														/*
														 * 写lesson借书
														 */
														xmlPull.pullXMLCreate(lxmlList);
													}

												}
											}

										};
										thread.start();
										onTag = true;// 避免多次点击，多次响应
									}
									else
									{
										Toast.makeText(ListFile.this, "空间不足，请删除部分文件后重试！", Toast.LENGTH_LONG).show();
									}
								}
									/**
									 * 如果是TF卡存在，判断TF卡大小
									 */
									else {
										mSDdespath = "/mnt/external_sd/tqxktbyw/";
										
										if ((testEXCard.FileFolder_Free_Size(mSDCard) * 1024 * 1024) >= size) // 还有空间吗
										{
											pdDialog.show();
											thread = new Thread()
											{

												@Override
												public void run()
												{

													infoFromC = depack(mSDdespath, mSDsrcPath);
													/*
													 * infofromc
													 * 0
													 * 失败
													 * 1
													 * 成功
													 */
													if (infoFromC == 0)
													{
														FileInfo fileInfo_0 = new FileInfo(mfile.getName(), mSDsrcPath, 0, 0);
														pdDialog.dismiss();
														Message msg = new Message();
														msg.what = 0;
														msg.obj = arg1;
														mHandler.sendMessage(msg);

														if (!lxmlList.contains(fileInfo_0))
														{
															Log.i("添加一条", "错误文件");
															listsize++;
															fileInfo_0.setFile_Name(mfile.getName());
															fileInfo_0.setFile_Path(mSDsrcPath);
															fileInfo_0.setFile_Status(0);
															lxmlList.add(fileInfo_0);
															xmlPull.pullXMLCreate(lxmlList);
														}

													}
													else
													{
														FileInfo fileInfo_1 = new FileInfo(mfile.getName(), mSDsrcPath, 0, 0);
														pdDialog.dismiss();
														Message msg = new Message();
														msg.what = 1;
														msg.obj = arg1;
														mHandler.sendMessage(msg);

														if (!lxmlList.contains(fileInfo_1))
														{
															Log.i("添加一条", "成功文件");
															listsize++;
															fileInfo_1.setFile_Name(mfile.getName());
															fileInfo_1.setFile_Path(mSDsrcPath);
															fileInfo_1.setFile_Status(1);
															lxmlList.add(fileInfo_1);

															/*
															 * 写smi表
															 */
															listStudy = readUser(mSDdespath + "smi.xml");
															try
															{
																for (StudyMethod sm : listStudy)
																{
																	if (sm.get_nid() > 0)
																	{
																		replaceSMI(sm, "http://pad2.tqpad.com/file_News/Studymethod/");
																	}
																}

															}
															catch (Exception e)
															{
																e.printStackTrace();
															}
															/*
															 * 写smi表结束
															 */
															/*
															 * 写smd
															 */
															listStudy_new = readStudy(mSDdespath + "smd.xml");
															if (listStudy_new.size() > 0)
															{
																for (StudyMethodDetail smd : listStudy_new)
																{
																	MainActivity.UpdateSMDetail(smd);
																}
															}
															/*
															 * 写smd结束
															 */
															/*
															 * 写lesson
															 */
															filesXmlList = xmlPull.pullXMLFromC(mSDdespath + "lesson.xml");
															String tableName = EastStudy.Lesson.TABLE_NAME;
															String tableName_teacher = EastStudy.Teacher.TABLE_NAME;
															for (int i = 0; i < filesXmlList.size(); i++)
															{
																ContentValues values = new ContentValues();
																ContentValues values_teacher = new ContentValues();

																values_teacher.put("_id", filesXmlList.get(i).getTeacher_id());
																values_teacher.put("ver", filesXmlList.get(i).getTeacher_ver());
																values_teacher.put("vurl", mSDdespath + filesXmlList.get(i).getTeacher_url());

																values.put("status", 4);
																values.put("url", mSDdespath + filesXmlList.get(i).getFile_url());
																values.put("zipname", mSDdespath + filesXmlList.get(i).getFile_zip());

																String[] args_id =
																{ String.valueOf(filesXmlList.get(i).getFile_id()) };
																SQLiteDatabase db = mDatabase.getWritableDatabase();
																db.update(tableName, values, "_id=?", args_id);

																db.replace(tableName_teacher, null, values_teacher);
																String zip_pathString = mSDdespath + filesXmlList.get(i).getFile_zip() + ".zip";
																File file_zipFile = new File(zip_pathString);
																try
																{
																	// 解压完再删除
																	ZipUtils.decompress(zip_pathString, mSDdespath);
																}
																catch (Exception e)
																{
																	e.printStackTrace();
																}
																if (file_zipFile.exists())
																{
																	file_zipFile.delete();
																}
															}
															/*
															 * 写lesson借书
															 */
															xmlPull.pullXMLCreate(lxmlList);
														}

													}
												}

											};
											thread.start();
											onTag = true;// 避免多次点击，多次响应
										}
										else
										{
											Toast.makeText(ListFile.this, "TF空间不足，请删除部分文件后重试！", Toast.LENGTH_LONG).show();
										}
										
									}
									
									onTag = true;// 避免多次点击，多次响应
								}

							})

							.setNeutralButton("取消操作", new DialogInterface.OnClickListener()
							{
								public void onClick(DialogInterface dialog, int which)
								{
									onTag = true;// 避免多次点击，多次响应
								}
							}).setCancelable(false);
							alertDialog = builder.create();
							alertDialog.show();

						}
					}
				}
			}
		});

	}

	/*
	 * / 按2次返回退出程序
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if ((System.currentTimeMillis() - exitTime) > 2000)
			{
				Toast.makeText(getApplicationContext(), "再按一次退出导入", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			}
			else
			{
				// Comparator<FileInfo> comp = new
				// Mycomparator();
				// Collections.sort(lxmlList, comp);
				//xmlPull.pullXMLCreate(lxmlList);
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * 读object
	 */
	public List<StudyMethod> readUser(String path)
	{
		try
		{
			ObjectInputStream oin = new ObjectInputStream(new FileInputStream(path));
			listStudy = (List<StudyMethod>) (oin.readObject());
			oin.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listStudy;

	}

	/*
	 * 读object
	 */
	public List<StudyMethodDetail> readStudy(String path)
	{
		try
		{
			ObjectInputStream oins = new ObjectInputStream(new FileInputStream(path));
			listStudy_new = (List<StudyMethodDetail>) oins.readObject();
			oins.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listStudy_new;

	}

	/*
	 * 插入SMI信息
	 */
	public void replaceSMI(StudyMethod _sm, String burl)
	{
		SQLiteDatabase db = mDatabase.getWritableDatabase();
		String tableName = EastStudy.StudyMethodInfo.TABLE_NAME;

		ContentValues values = new ContentValues();
		values.put(StudyMethodInfo.ID, _sm.get_nid());
		values.put(StudyMethodInfo.GID, _sm.get_gid());
		values.put(StudyMethodInfo.VER, _sm.get_ver());
		values.put(StudyMethodInfo.BASEURL, burl);
		values.put(StudyMethodInfo.URL, _sm.get_xmlFile());
		mDatabase.getWritableDatabase().replace(StudyMethodInfo.TABLE_NAME, null, values);

		try
		{
			db.replace(tableName, null, values);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	static
	{
		System.loadLibrary("packer");
	}
}
