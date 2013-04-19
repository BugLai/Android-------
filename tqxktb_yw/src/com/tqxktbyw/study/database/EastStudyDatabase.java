package com.tqxktbyw.study.database;

import com.tqxktbyw.study.EastStudy;
import com.tqxktbyw.study.EastStudy.Notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EastStudyDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tqxktbyw.db";
    private static final int DATABASE_VERSION = 1;

    public EastStudyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public final Cursor query(String table, String[] columns, String where, String[] whereArgs) {
        return getReadableDatabase().query(table, columns, where, whereArgs, null, null, EastStudy.BaseColumns.DEFAULT_SORT_ORDER);
    }

    public final Cursor rawQuery(String sql, String[] selectionArgs) {
        return getReadableDatabase().rawQuery(sql, selectionArgs);
    }
    public final boolean rawQuery(String[] sql){
    	SQLiteDatabase db = getReadableDatabase();
    	boolean b = false;
    	db.beginTransaction();//事启事务
		try{
			for(int i=0;i<sql.length;i++){
				db.execSQL(sql[i]);
			}
			db.setTransactionSuccessful();//设置事务标志为成功，当结束事务时就会提交事务
			b = true;
		}finally{
			db.endTransaction();
		}
		return b;
    }

    public final boolean existsDownloaded(long id) {
        Cursor cursor = getReadableDatabase().query(EastStudy.Downloaded.TABLE_NAME, null, EastStudy.Downloaded.COLUMN_ID + '=' + id, null, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();

        return exists;
    }

    public final void insertDownloaded(long id) {
        ContentValues values = new ContentValues();
        values.put(EastStudy.Downloaded.COLUMN_ID, id);

        getWritableDatabase().replace(EastStudy.Downloaded.TABLE_NAME, null, values);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_VERSION_TABLE);
        db.execSQL(CREATE_JOIN_TABLE);
        db.execSQL(CREATE_COURSE_TABLE);
        db.execSQL(CREATE_LESSON_TABLE);
        db.execSQL(CREATE_DOWNLOADED_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_GRADE_TABLE);
        db.execSQL(CREATE_TEACHER_TABLE);
        db.execSQL(CREATE_STUDYMETHEDINFO_TABLE);
        db.execSQL(CREATE_STUDYMETHEDDETAIL_TABLE);
        db.execSQL(CREATE_STUDYMETHEDDETAIL_PIC_TABLE);
        db.execSQL(CREATE_Note_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EastStudy.Grade.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EastStudy.Join.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EastStudy.Lesson.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EastStudy.Course.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EastStudy.Version.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EastStudy.Downloaded.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EastStudy.UserInfo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EastStudy.Teacher.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EastStudy.StudyMethodInfo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EastStudy.StudyMethodDetail.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EastStudy.StudyMethodDetailPic.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EastStudy.Notes.TABLENAME);
        onCreate(db);
    }
    private static final String CREATE_GRADE_TABLE = "CREATE TABLE grade (_id INTEGER PRIMARY KEY,"+
            "name TEXT,sort_order INTEGER);";
    private static final String CREATE_USER_TABLE = "CREATE TABLE u_table (_id INTEGER PRIMARY KEY,"+
            "random TEXT,checkurl TEXT);";
    private static final String CREATE_TEACHER_TABLE = "CREATE TABLE teacher (_id INTEGER PRIMARY KEY,"+
            "ver INTEGER,vurl TEXT);";
    private static final String CREATE_STUDYMETHEDINFO_TABLE = "CREATE TABLE smi (_id INTEGER PRIMARY KEY,"+
            "gid INTEGER,ver INTEGER,baseurl TEXT,url TEXT);";
    private static final String CREATE_STUDYMETHEDDETAIL_TABLE = "CREATE TABLE smd (_id INTEGER PRIMARY KEY,"+
            "title TEXT,content TEXT,baseurl TEXT,haspic INTEGER,gid INTEGER);";
    private static final String CREATE_STUDYMETHEDDETAIL_PIC_TABLE = "CREATE TABLE pic (_id INTEGER,"+
            "url TEXT,baseurl TEXT);";

    private static final String CREATE_VERSION_TABLE = "CREATE TABLE version (name TEXT PRIMARY KEY,"+
        "number INTEGER);";

    private static final String CREATE_JOIN_TABLE = "CREATE TABLE join_table (course_id INTEGER PRIMARY KEY,"+
    "gid INTEGER,vid INTEGER,title TEXT,sort_order INTEGER);";
    
    private static final String CREATE_Note_TABLE =  new StringBuilder("create table ").append(Notes.TABLENAME)
    		.append("(").append(Notes.COLUMN_ID).append(" integer primary key,").append(Notes.TITLE)
    		.append(" text,").append(Notes.BACK_ID).append(" text,")
    		.append(Notes.CONTENT).append(" text,")
    		.append(Notes.LTIME).append(" text,")
    		.append(Notes.BTIME).append(" text,")
    		.append(Notes.ANSWERS).append(" text,")
    		.append(Notes.FLAG).append(" text,")
    		.append(Notes.CHOICE).append(" text,")
    		.append(Notes.GRADE).append(" text,")
    		.append(Notes.SUBJECT).append(" text,")
    		.append(Notes.SUMMIT_FLAG).append(" text")
    		.append(")").toString();

    private static final String CREATE_COURSE_TABLE = "CREATE TABLE course (_id INTEGER PRIMARY KEY,"+
        "name TEXT,sort_order INTEGER,pid INTEGER);";

    private static final String CREATE_LESSON_TABLE = "CREATE TABLE lesson (_id INTEGER PRIMARY KEY,"+
        "name TEXT,sort_order INTEGER,url TEXT,type INTEGER,status INTEGER,course_id INTEGER,zipname TEXT,"+
    		"tid INTEGER,cs INTEGER,zs INTEGER,ks INTEGER,ts INTEGER,lver INTEGER,sver INTEGER);";

    private static final String CREATE_DOWNLOADED_TABLE = "CREATE TABLE downloaded (_id INTEGER PRIMARY KEY);";
}