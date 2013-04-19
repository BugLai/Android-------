package com.tqxktbyw.study.provider;

import com.tqxktbyw.study.EastStudy;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

public class DownloadProvider extends ContentProvider {
	private static final String DATABASE_NAME = "tqxktbywdownloads8.db";
	private static final String EMPTY_STRING = "";
    private static final int DATABASE_VERSION = 1;

	private static final String DOWNLOAD_TABLE = "downloads";
    static final String SUCCESS_TABLE  = "success";
    static final String EXECUTE_TABLE  = "execute";
    static final String EXPANDABLE_TABLE  = "expandablelist";
    static final String GRADE_TABLE = "grade";

    private static final int DOWNLOAD 	   = 100;
    private static final int DOWNLOAD_ITEM = 101;
    private static final int SUCCESS 	   = 200;
    private static final int SUCCESS_ITEM  = 201;
    private static final int EXECUTE	   = 300;
    private static final int EXECUTE_ITEM  = 301;
    private static final int DOWNLOAD_MOVE = 400;
    private static final int EXPANDABLE	   = 500;
    private static final int EXPANDABLE_ITEM  = 501;

    private static final UriMatcher mUriMatcher;
    private DownloadDatabase mDatabase;

	@Override
	public boolean onCreate() {
		mDatabase = new DownloadDatabase(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		switch (mUriMatcher.match(uri)) {
		case DOWNLOAD:
			return Downloads.CONTENT_TYPE;

		case DOWNLOAD_ITEM:
			return Downloads.CONTENT_ITEM_TYPE;

		case SUCCESS:
			return Downloads.Success.CONTENT_TYPE;

		case SUCCESS_ITEM:
			return Downloads.Success.CONTENT_ITEM_TYPE;

		case EXECUTE:
			return Downloads.Execute.CONTENT_TYPE;

		case EXECUTE_ITEM:
			return Downloads.Execute.CONTENT_ITEM_TYPE;

		case EXPANDABLE:
			return Downloads.ExpandableList.CONTENT_TYPE;

		case EXPANDABLE_ITEM:
			return Downloads.ExpandableList.CONTENT_ITEM_TYPE;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		QueryInfo info = new QueryInfo(uri, selection);
		getQueryInfo(info);

		if (TextUtils.isEmpty(sortOrder)) {
			sortOrder = Downloads.DEFAULT_SORT_ORDER;
		}

		Cursor cursor = mDatabase.getReadableDatabase().query(info.table, projection, info.selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		QueryInfo info = new QueryInfo(uri, selection);
		getQueryInfo(info);

		if (info.table.equals(DOWNLOAD_TABLE)) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

        int count = mDatabase.getWritableDatabase().delete(info.table, info.selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		int code = mUriMatcher.match(uri);
		if (code != SUCCESS && code != EXECUTE && code !=EXPANDABLE && code != EXPANDABLE_ITEM) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		if (code!=EXPANDABLE && code != EXPANDABLE_ITEM && !initialValues.containsKey(Downloads.DOWNLOAD_URL)) {
			throw new IllegalArgumentException("Empty Download URL");
		}

		String table = EXECUTE_TABLE;
		int status = EastStudy.STATUS_PENDING;
		ContentValues values = new ContentValues(initialValues);
		if (code == SUCCESS) {
			table  = SUCCESS_TABLE;
			status = EastStudy.STATUS_SUCCESSFUL;
		}
		if (code == EXPANDABLE) {
			table  = GRADE_TABLE;
		}else if(code == EXPANDABLE_ITEM){
			table  = EXPANDABLE_TABLE;
		}else{
			values.put(Downloads.STATUS, status);
			if (!values.containsKey(Downloads.LESSION_NAME)) {
				values.put(Downloads.LESSION_NAME, EMPTY_STRING);
			}

		    if (!values.containsKey(Downloads.LESSION_TYPE)) {
		        values.put(Downloads.LESSION_TYPE, EastStudy.LESSON_TYPE_MP4);
		    }

		    if (!values.containsKey(Downloads.FILENAME)) {
		        values.put(Downloads.FILENAME, EMPTY_STRING);
		    }

			if (!values.containsKey(Downloads.TOTAL_SIZE)) {
				values.put(Downloads.TOTAL_SIZE, 0);
			}

			if (!values.containsKey(Downloads.DOWNLOADED_SIZE)) {
				values.put(Downloads.DOWNLOADED_SIZE, 0);
			}

			if (!values.containsKey(Downloads.SORT_ORDER)) {
				values.put(Downloads.SORT_ORDER, 0);
			}

	        if (!values.containsKey(Downloads.COURSE_ID)) {
	            values.put(Downloads.COURSE_ID, 0);
	        }
		}

		long rowId = mDatabase.getWritableDatabase().insert(table, null, values);
		
		if (rowId > 0) {
			//Uri newUri = ContentUris.withAppendedId(uri, rowId);
			Uri newUri;
			if (!values.containsKey(Downloads.ISV)) {
				newUri = ContentUris.withAppendedId(uri, rowId);
	        }else{
	        	int ivss = values.getAsInteger(Downloads.ISV);
				newUri = Uri.withAppendedPath(uri, rowId + "_" + ivss);
	        	//newUri = ContentUris.withAppendedId(uri, rowId);
	        }
			
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}

		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int count = 2, code = mUriMatcher.match(uri);
		if (code == DOWNLOAD_MOVE) {
			move(values);
		} else {
			QueryInfo info = new QueryInfo(uri, selection);
			getQueryInfo(info);

			if (info.table.equals(DOWNLOAD_TABLE)) {
				throw new IllegalArgumentException("Unknown URI " + uri);
			}

			count = mDatabase.getWritableDatabase().update(info.table, values, info.selection, selectionArgs);
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return count;
	}

	private void move(ContentValues values) {
		SQLiteDatabase db = mDatabase.getWritableDatabase();
		try {
		    String id = values.getAsString(Downloads._ID);
		    String sourceTable = values.getAsString(Downloads.SOURCE_TABLE);

            db.beginTransaction();
            System.out.println(String.format(SQL_INSERT_STATEMENT, values.getAsString(Downloads.DEST_TABLE),
		            values.getAsString(Downloads.STATUS), values.getAsString(Downloads.DOWNLOADED_SIZE),
		            sourceTable, id,values.getAsString(Downloads.ISV))+"yy?");
		    db.execSQL(String.format(SQL_INSERT_STATEMENT, values.getAsString(Downloads.DEST_TABLE),
		            values.getAsString(Downloads.STATUS), values.getAsString(Downloads.DOWNLOADED_SIZE),
		            sourceTable, id,values.getAsString(Downloads.ISV)));
			db.execSQL("DELETE FROM " + sourceTable + " WHERE lid=" + id + " AND isv="+ values.getAsString(Downloads.ISV) +";");
			db.setTransactionSuccessful();
		}catch(Exception ex){
			ex.printStackTrace();
		} finally {
			db.endTransaction();
		}

		ContentResolver resolver = getContext().getContentResolver();
		resolver.notifyChange(Downloads.Execute.CONTENT_URI, null);
		resolver.notifyChange(Downloads.Success.CONTENT_URI, null);
	}

	private void getQueryInfo(QueryInfo info) {
		String id = null;
        switch (mUriMatcher.match(info.uri)) {
        case DOWNLOAD:
        	info.table = DOWNLOAD_TABLE;
        	break;

        case SUCCESS:
        	info.table = SUCCESS_TABLE;
        	break;

        case EXECUTE:
        	info.table = EXECUTE_TABLE;
            break;

        case EXPANDABLE:
        	info.table = GRADE_TABLE;
            break;

        case EXPANDABLE_ITEM:
            //id = info.uri.getPathSegments().get(2);
            info.table = EXPANDABLE_TABLE;
            //info.selection = Downloads._ID + "=" + id + (!TextUtils.isEmpty(info.selection) ? " AND (" + info.selection + ')' : "");
        	break;

        case DOWNLOAD_ITEM:
            id = info.uri.getPathSegments().get(1);
            info.table = DOWNLOAD_TABLE;
            info.selection = Downloads._ID + "=" + id + (!TextUtils.isEmpty(info.selection) ? " AND (" + info.selection + ')' : "");
            break;

        case SUCCESS_ITEM:
            id = info.uri.getPathSegments().get(2);
            info.table = SUCCESS_TABLE;
            info.selection = Downloads._ID + "=" + id + (!TextUtils.isEmpty(info.selection) ? " AND (" + info.selection + ')' : "");
        	break;

        case EXECUTE_ITEM:
            id = info.uri.getPathSegments().get(2);
            info.table = EXECUTE_TABLE;
            info.selection = Downloads._ID + "=" + id + (!TextUtils.isEmpty(info.selection) ? " AND (" + info.selection + ')' : "");
        	break;

        default:
        	if(info.uri.getPathSegments().get(1).equalsIgnoreCase(EXPANDABLE_TABLE)){
        		//id = info.uri.getPathSegments().get(3);
                info.table = EXPANDABLE_TABLE;
                //info.selection = Downloads._ID + "=" + id + (!TextUtils.isEmpty(info.selection) ? " AND (" + info.selection + ')' : "");
            	break;
        	}else{
        		throw new IllegalArgumentException(mUriMatcher.match(info.uri) +"Unknown URI " + info.uri);
        	}
            
        }
	}

	private static class QueryInfo {
		public Uri uri;
		public String table;
		public String selection;

		public QueryInfo(Uri uri, String selection) {
			this.uri = uri;
			this.selection = selection;
		}
	}

	private static class DownloadDatabase extends SQLiteOpenHelper {
		public DownloadDatabase(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
    		db.execSQL(CREATE_SUCCESS_TABLE);
    		db.execSQL(CREATE_EXECUTE_TABLE);
    		db.execSQL(CREATE_DOWNLOAD_TALBE);
    		db.execSQL(CREATE_EXPANDABLE_TABLE);
    		db.execSQL(CREATE_GRADE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP VIEW IF EXISTS " + DOWNLOAD_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SUCCESS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + EXECUTE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + EXPANDABLE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + GRADE_TABLE);
            onCreate(db);
		}
	}

	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(Downloads.AUTHORITY, DOWNLOAD_TABLE, DOWNLOAD);
		mUriMatcher.addURI(Downloads.AUTHORITY, DOWNLOAD_TABLE + "/#", DOWNLOAD_ITEM);
		mUriMatcher.addURI(Downloads.AUTHORITY, DOWNLOAD_TABLE + "/" + SUCCESS_TABLE, SUCCESS);
		mUriMatcher.addURI(Downloads.AUTHORITY, DOWNLOAD_TABLE + "/" + SUCCESS_TABLE + "/#", SUCCESS_ITEM);
		mUriMatcher.addURI(Downloads.AUTHORITY, DOWNLOAD_TABLE + "/" + EXECUTE_TABLE, EXECUTE);
		mUriMatcher.addURI(Downloads.AUTHORITY, DOWNLOAD_TABLE + "/" + EXECUTE_TABLE + "/#", EXECUTE_ITEM);
		mUriMatcher.addURI(Downloads.AUTHORITY, DOWNLOAD_TABLE + "/" + GRADE_TABLE, EXPANDABLE);
		mUriMatcher.addURI(Downloads.AUTHORITY, DOWNLOAD_TABLE + "/" + EXPANDABLE_TABLE,EXPANDABLE_ITEM);
		mUriMatcher.addURI(Downloads.AUTHORITY, DOWNLOAD_TABLE + "/move", DOWNLOAD_MOVE);
	}

	private static final String CREATE_EXPANDABLE_TABLE = "CREATE TABLE expandablelist (_id INTEGER PRIMARY KEY,"+
		    "name TEXT,sort_order INTEGER);";
	private static final String CREATE_GRADE_TABLE = "CREATE TABLE grade (_id INTEGER PRIMARY KEY,"+
		    "name TEXT,sort_order INTEGER);";
	private static final String CREATE_SUCCESS_TABLE = "CREATE TABLE success (_id INTEGER,lid INTEGER,"+
	    "download_url TEXT,lession_name TEXT,lession_type INTEGER,filename TEXT,status INTEGER,total_size INTEGER,"+
	    "downloaded_size INTEGER,sort_order INTEGER,course_id INTEGER,isv INTEGER);";

	private static final String CREATE_EXECUTE_TABLE = "CREATE TABLE execute (_id INTEGER,lid INTEGER,"+
	    "download_url TEXT,lession_name TEXT,lession_type INTEGER,filename TEXT,status INTEGER,total_size INTEGER,"+
	    "downloaded_size INTEGER,sort_order INTEGER,course_id INTEGER,isv INTEGER);";

	private static final String CREATE_DOWNLOAD_TALBE = "CREATE VIEW downloads AS SELECT * FROM success UNION SELECT * "+
	    "FROM execute;";

	private static final String SQL_INSERT_STATEMENT = "INSERT INTO %s (_id,lid,download_url,lession_name,lession_type,"+
	    "filename,status,total_size,downloaded_size,sort_order,course_id,isv) SELECT _id,lid,download_url,lession_name,lession_type,"+
	    "filename,%s,total_size,%s,sort_order,course_id,isv FROM %s WHERE lid=%s AND isv=%s;";
}