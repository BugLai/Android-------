package com.tqxktbyw.study;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.tqxktbyw.study.EastStudy.Lesson;
import com.tqxktbyw.study.EastStudy.Teacher;
import com.tqxktbyw.study.database.EastStudyDatabase;
import com.tqxktbyw.study.provider.Downloads;


public class DownloadManager {
	private static final String TAG = "DownloadManager";
    private static final int INVALID_ID  = -1;
    private static final int BUFFER_SIZE = 1024;
    private static final int MAX_UPDATE_TIME = 500;

	public static final String[] COLUMNS = {
		Downloads.DOWNLOAD_URL,
		Downloads.FILENAME,
		Downloads.TOTAL_SIZE,
		Downloads.DOWNLOADED_SIZE
	};

	public static final int COLUMN_DOWNLOAD_URL_INDEX    = 0;
	public static final int COLUMN_FILENAME_INDEX  	  = 1;
	public static final int COLUMN_TOTAL_SIZE_INDEX 	  = 2;
	public static final int COLUMN_DOWNLOADED_SIZE_INDEX = 3;

    private final ThreadPool mThreadPool;
    private final ContentResolver mResolver;
    //private int mIsv;// 0 视频 1 zip 2 mp4
    private OnDownloadStatusListener mOnDownloadStatusListener;
    private EastStudyDatabase mDatabase;

	public DownloadManager(Context context, int maxThreadCount,EastStudyDatabase _mDatabase) {
        mResolver = context.getContentResolver();
        mThreadPool = new ThreadPool(maxThreadCount);
        //mIsv = 0;
        mDatabase = _mDatabase;
    }
	//public boolean putbutton

    public interface OnDownloadStatusListener {
        void onStateChanged(long id, int status);
    }
    public int getThreadCount(){
    	return mThreadPool.getThreadCount();
    }

    public boolean execute(long id,int isv) {
        if (id == INVALID_ID) {
            throw new IllegalArgumentException();
        }

        Cursor cursor = mResolver.query(Downloads.Execute.CONTENT_URI, COLUMNS, Downloads.LID + " = " + id+ " AND isv=" + isv, null, null);
        boolean successful = cursor.moveToFirst();
        if (successful) {
            Task task = new Task(id,isv);
            task.filename  = cursor.getString(COLUMN_FILENAME_INDEX);
            task.totalSize = cursor.getLong(COLUMN_TOTAL_SIZE_INDEX);
            task.downloadUrl = cursor.getString(COLUMN_DOWNLOAD_URL_INDEX);
            task.downloadedSize = cursor.getLong(COLUMN_DOWNLOADED_SIZE_INDEX);

            ContentValues values = new ContentValues();
            values.put(Downloads.STATUS, EastStudy.STATUS_PENDING);
            mResolver.update(Downloads.Execute.CONTENT_URI, values, Downloads.LID + " = " + task.lid+ " AND isv=" + isv, null);

            mThreadPool.execute(task);
        }

        cursor.close();
        return successful;
    }

    public void execute(long id, String downloadUrl, String lessonName, int lessonType, int sortOrder, long courseId,int isv,int tid,int tver) {
    	//mIsv = isv;
    	Cursor cursor = mResolver.query(Downloads.Execute.CONTENT_URI, COLUMNS, Downloads.LID + " = " + id + " AND isv=" + isv, null, null);
    	if (cursor.getCount()>0){
    		cursor.close();
    		return;
    	}
    	//this.progressList.put(id, (Button)btn.findViewById(R.id.hori_lesson_download));
    	ContentValues values = new ContentValues();
        values.put(Downloads.LID, id);
        values.put(Downloads.COURSE_ID, courseId);
        values.put(Downloads.SORT_ORDER, sortOrder);
        values.put(Downloads.DOWNLOAD_URL, downloadUrl);
        values.put(Downloads.LESSION_NAME, lessonName);
        values.put(Downloads.LESSION_TYPE, lessonType);
        values.put(Downloads.ISV, isv);

        Task task = new Task(id,downloadUrl,isv,tid,tver);
        //task.id = Integer.parseInt(mResolver.insert(Downloads.Execute.CONTENT_URI, values).getPathSegments().get(2));
        //String ivis = mResolver.insert(Downloads.Execute.CONTENT_URI, values).getPathSegments().get(2);
        mResolver.insert(Downloads.Execute.CONTENT_URI, values);
        //List<String>  ls = mResolver.insert(Downloads.Execute.CONTENT_URI, values).getPathSegments();
        /*for(String s:ls){
        }*/
        //task.lid = Integer.parseInt(ivis.substring(0,ivis.indexOf("_")));
        mThreadPool.execute(task);
        cursor.close();
    }

    public void shutdown(int timeout) {
        updateStatusPaused(mThreadPool.shutdown(timeout));
    }

    public void remove(long id,int isv) {
    	
        if (!cancel(id)) {
            Object[] tasks = mThreadPool.getTasks();
            for (int i = 0; i < tasks.length; ++i) {
                Task task = (Task)tasks[i];
                if (task.lid == id) {
                    mThreadPool.remove(task);
                    ContentValues values = new ContentValues();
                    values.put(Downloads.STATUS, EastStudy.STATUS_PAUSED);
                    mResolver.update(Downloads.Execute.CONTENT_URI, values, Downloads.LID + " = " + id+ " AND isv=" + isv, null);
                    break;
                }
            }
        }
    }

    public boolean cancel(long id) {
        for (int i = mThreadPool.getThreadCount() - 1; i >= 0; --i) {
            Task task = (Task)mThreadPool.getExecutingTask(i);
            if (task != null && task.lid == id) {
                task.cancel();
                return true;
            }
        }

        return false;
    }

    public void cancelAll() {
        List<Runnable> taskList = new ArrayList<Runnable>();
        mThreadPool.removeAll(taskList);
        updateStatusPaused(taskList);

        for (int i = mThreadPool.getThreadCount() - 1; i >= 0; --i) {
            Task task = (Task)mThreadPool.getExecutingTask(i);
            if (task != null) {
                task.cancel();
            }
        }
    }

    public boolean isShutdown() {
    	return mThreadPool.isShutdown();
    }

    public boolean isTaskRunning() {
        return mThreadPool.isTaskRunning();
    }

    public void updateStatusPaused(List<Runnable> taskList) {
        int count = taskList.size();
        if (count > 0) {
            ContentValues values = new ContentValues();
            values.put(Downloads.STATUS, EastStudy.STATUS_PAUSED);

            StringBuilder builder = new StringBuilder();
            builder.append(Downloads._ID).append(" IN(");
            for (int i = 0; i < count; ++i) {
                builder.append(((Task)taskList.get(i)).id + ',');
            }

            taskList.clear();
            builder.replace(builder.length() - 1, builder.length(), ")");
            mResolver.update(Downloads.Execute.CONTENT_URI, values, builder.toString(), null);
            mDatabase.getWritableDatabase().update(Lesson.TABLE_NAME,
    				values, builder.toString(), null);
        }
    }

    public void setOnDownloadStatusListener(OnDownloadStatusListener listener) {
        mOnDownloadStatusListener = listener;
    }

    private void notifyDownloadStatusChanged(long id, int status) {
        mHandler.sendMessage(mHandler.obtainMessage(0, status, 0, Long.valueOf(id)));
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
			if( msg.obj != null){
				if (mOnDownloadStatusListener != null) {
	                mOnDownloadStatusListener.onStateChanged((Long)msg.obj, msg.arg1);
	            }
			}
        }
    };

    /**
     * class Task
     */
    private class Task implements Runnable {
    	public long id;
        public long lid;
        public long totalSize;
        public long downloadedSize;
        public String filename;
        public String downloadUrl;
        public long tid;
        public long tver;
        int taskIsv;

        private volatile boolean cancel;

        public Task(long id,int _isv) {
        	this.id = id;
            this.lid  = id;
            taskIsv = _isv;
        }

        public Task(long id,String downloadUrl,int _isv,long tid,long tver) {
        	this.id = id;
            this.lid = id;
            this.downloadUrl = downloadUrl;
            this.taskIsv = _isv;
            this.tid = tid;
            this.tver=tver;
        }

		public void cancel() {
            cancel = true;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            try {
                if (TextUtils.isEmpty(filename)) {
                    filename = EastStudy.DOWNLOAD_PATH + File.separator + downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1);
                }
                URL url = new URL(downloadUrl);
                conn = (HttpURLConnection)url.openConnection();
                connect(conn);

                update(EastStudy.STATUS_RUNNING,taskIsv);
                notifyDownloadStatusChanged(lid, EastStudy.STATUS_RUNNING);

                if (totalSize >= EastStudy.getExternalStorageFreeSpace()) {
                    throw new Exception("Insufficient free space to save file");
                }

                download(conn.getInputStream());
            } catch (Exception e) {
                update(EastStudy.STATUS_FAILED,taskIsv);
                if(e.getMessage().equalsIgnoreCase("Insufficient free space to save file")){
                	notifyDownloadStatusChanged(lid, EastStudy.STATUS_NOSPACE);
                }else{
                	notifyDownloadStatusChanged(lid, EastStudy.STATUS_FAILED);
                }
                Log.e(TAG, "Can not download -- " + downloadUrl, e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }

        private void connect(HttpURLConnection conn) throws Exception {
            conn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(EastStudy.CONNECTION_TIMEOUT);
            conn.addRequestProperty("Accept", "*/*");
            conn.addRequestProperty("User-Agent", "EastStudy");
            conn.addRequestProperty("Connection", "keep-alive");

            if (downloadedSize != 0) {
                conn.addRequestProperty("Range", "bytes=" + downloadedSize + '-');
            }

            conn.connect();
            switch (conn.getResponseCode()) {
            case HttpURLConnection.HTTP_OK:
                downloadedSize = 0;
                totalSize = conn.getContentLength();
                break;

            case HttpURLConnection.HTTP_PARTIAL:
                break;

            default:
                throw new ConnectException("Can not connect " + downloadUrl);
            }
        }

        private void download(InputStream is) throws Exception {
            RandomAccessFile file = null;
            BufferedInputStream stream = null;

            try {
                stream = new BufferedInputStream(is);
                file   = new RandomAccessFile(filename, "rw");
                file.seek(downloadedSize);

                int downloadedTime = 0;
                byte[] buffer = new byte[BUFFER_SIZE];

                while (!isCancelled() && !isShutdown()) {
                    int size = stream.read(buffer);
                    if (size == -1) {
                        break;
                    }

                    file.write(buffer, 0, size);
                    downloadedSize += size;
                    ++downloadedTime;
                    if(taskIsv == 0){
                    	if (downloadedTime > MAX_UPDATE_TIME) {
                    		notifyDownloadStatusChanged(lid,EastStudy.STATUS_RUNNING);
                            updatePregress(taskIsv);
                            downloadedTime = 0;
                        }
                    }
                    
                }

                if (downloadedSize < totalSize) {
                	if (isCancelled())
                	{
                		update(EastStudy.STATUS_PAUSED,taskIsv);
                		notifyDownloadStatusChanged(lid, EastStudy.STATUS_PAUSED);
                	}
                	else if (isShutdown())
                	{
                  		update(EastStudy.STATUS_FAILED,taskIsv);
                		notifyDownloadStatusChanged(lid, EastStudy.STATUS_FAILED);
                	}
                } else {
                	String upfilename = filename;
                	if (filename.substring(filename.indexOf(".")+1).equals("zip"))
                	{
	                    try {
	                    	upfilename = filename.substring(0, filename.length()-4);
	            			ZipUtils.decompress(filename, upfilename);
	                        File delfile = new File(filename);
	                        delfile.delete();
	                    	//update(id,dstDic,taskIsv);
	            		} catch (Exception e) {
	            			e.printStackTrace();
	            		}
	                }

                	if(taskIsv == Downloads.DOWNLOADS_TYPE_VIDEO){
	                    notifyDownloadStatusChanged(lid, EastStudy.STATUS_SUCCESSFUL);
	                }
                	update(upfilename,taskIsv);
                    Downloads.moveExecuteToSuccess(mResolver, id, downloadedSize,taskIsv);
                }
            }catch(Exception ex){
            	ex.printStackTrace();
            }finally {
                try {
                    if (file != null) {
                        file.close();
                    }

                    if (stream != null) {
                        stream.close();
                    }
                } catch (Exception e) {
                }
            }
        }

        private boolean isCancelled() {
            return cancel;
        }
        private void update( String dstDic,int isv) {
			// TODO Auto-generated method stub
        	SQLiteDatabase db = mDatabase.getWritableDatabase();
        	ContentValues values = new ContentValues();
        	String tableName = Lesson.TABLE_NAME;
        	String where = Lesson.COLUMN_ID + '=' + lid;
        	switch(isv){
        	case Downloads.DOWNLOADS_TYPE_VIDEO:
        		values.put(Lesson.COLUMN_URL, dstDic);
        		break;
        	case Downloads.DOWNLOADS_TYPE_ZIP:
        		values.put(Lesson.COLUMN_ZIPNAME, dstDic);
        		break;
        	case Downloads.DOWNLOADS_TYPE_MP4:

        		tableName = Teacher.TABLE_NAME;
        		
        			//TODO
        			values.put(Teacher.ID, tid);
        			values.put(Teacher.VER, tver);
        			values.put(Teacher.URL, dstDic);
        			db.replace(tableName, null, values);
        		break;
        	}
        	//values.put(strt, dstDic);
        	if(isv != Downloads.DOWNLOADS_TYPE_MP4){
        		try{
                    db.update(tableName,
            				values, where, null);
                	}catch(Exception ex){
                		ex.printStackTrace();
                	}
        	}
		}

        private void update(int status,int isv) {
        	
            ContentValues values = new ContentValues();
            values.put(Downloads.STATUS, status);
            values.put(Downloads.FILENAME, filename);
            values.put(Downloads.TOTAL_SIZE, totalSize);
            values.put(Downloads.DOWNLOADED_SIZE, downloadedSize);

            mResolver.update(Downloads.Execute.CONTENT_URI, values, Downloads.LID + "=" + lid + " AND isv= " + isv, null);
        }

        private void updatePregress(int isv) {
            ContentValues values = new ContentValues();
            values.put(Downloads.DOWNLOADED_SIZE, downloadedSize);

            mResolver.update(Downloads.Execute.CONTENT_URI, values, Downloads.LID + "=" + lid+ " AND isv=" + isv, null);
        }
    }

	
}