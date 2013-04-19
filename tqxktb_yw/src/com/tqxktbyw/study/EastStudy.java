package com.tqxktbyw.study;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.os.Environment;
import android.os.StatFs;

public final class EastStudy {
    public static final String PLAYING_URI  = "playingUri";

    // Connection timeout
    public static final int CONNECTION_TIMEOUT = 5000;

    // Status
    public  static final int STATUS_NOSPACE   = 10;
    public static final int STATUS_FAILED     = 0;
    public static final int STATUS_PAUSED     = 1;
    public static final int STATUS_PENDING    = 2;
    public static final int STATUS_RUNNING    = 3;
    public static final int STATUS_SUCCESSFUL = 4;

    // Lesson Type
    public static final int LESSON_TYPE_MP4  = 0;
    public static final int LESSON_TYPE_HTML = 1;

    public interface BaseColumns {
        /**
         * The unique ID for a row.
         * <P>Type: INTEGER (long)</P>
         */
        public static final String COLUMN_ID = "_id";

        /**
         * The name of the table
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_NAME = "name";

        /**
         * The sort order of the table
         * <P>Type: INTEGER</P>
         */
        public static final String COLUMN_SORT_ORDER = "sort_order";

        /**
         * The default sort order for table
         */
        public static final String DEFAULT_SORT_ORDER = COLUMN_SORT_ORDER + " ASC";

        /**
         * The default query columns
         */
        public static final String COLUMNS[] = { COLUMN_ID, COLUMN_NAME,COLUMN_SORT_ORDER };

        public static final int COLUMN_ID_INDEX   = 0;
        public static final int COLUMN_NAME_INDEX = 1;
        public static final int COLUMN_SORT_ORDER_INDEX = 1;
    }
    public interface Grade extends BaseColumns {
        /**
         * The table name
         */
        public static final String TABLE_NAME = "grade";

    }
    public interface Notes {
    	public static final String TABLENAME = "noteTable";
    	
    	public static final String COLUMN_ID = "_id";
    	public static final String BACK_ID = "back_id";
    	public static final String TITLE = "title";// 标题
    	public static final String CONTENT = "content";// 问题内容
    	public static final String LTIME = "ltime";// 提问时间
    	public static final String BTIME = "btime";// 回答时间
    	public static final String ANSWERS = "answers";// 答案
    	public static final String FLAG = "flag";// 回答状态
    	public static final String CHOICE = "choice";// 是否精华
    	public static final String SUBJECT = "subject";// 学科
    	public static final String VERSION = "version";// 版本
    	public static final String GRADE = "grade";// 年级
    	public static final String SUMMIT_FLAG = "summit_flag";// 是否把答案插入
    	
    	public static final String COLUMNS[] = { COLUMN_ID, TITLE,BACK_ID,
    		CONTENT,LTIME,BTIME,ANSWERS,FLAG,CHOICE,SUBJECT,VERSION,GRADE,SUMMIT_FLAG};

        public static final int COLUMN_ID_INDEX   = 0;
        public static final int COLUMN_TITLE_INDEX = 1;
        public static final int COLUMN_BACK_ID_INDEX = 2;
        public static final int COLUMN_CONTENT_INDEX = 3;
        public static final int COLUMN_LTIME_INDEX = 4;
        public static final int COLUMN_BTIME_INDEX = 5;
        public static final int COLUMN_ANSWERS_INDEX = 6;
        public static final int COLUMN_FLAG_INDEX = 7;
        public static final int COLUMN_CHOICE_INDEX = 8;
        public static final int COLUMN_SUBJECT_INDEX = 9;
        public static final int COLUMN_VERSION_INDEX = 10;
        public static final int COLUMN_GRADE_INDEX = 11;
        public static final int COLUMN_SUMMIT_FLAG_INDEX = 12;

    }
    public interface Subject extends BaseColumns {
        /**
         * The table name
         */
        public static final String TABLE_NAME = "subject";
		public static final int COLUMN_GRADE_ID_INDEX = 2;
    }

    public interface Version {
        /**
         * The table name
         */
        public static final String TABLE_NAME = "version";
        /**
         * The name of the version
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_VERSIONNAME = "name";

        /**
         * The number of the version
         * <P>Type: INTEGER</P>
         */
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMNS[] = { COLUMN_VERSIONNAME, COLUMN_NUMBER };

        public static final int COLUMN_VERSIONNAME_INDEX         = 0;
        public static final int COLUMN_NUMBER_INDEX       = 1;
    }
    public interface Teacher {
        /**
         * The table name
         */
        public static final String TABLE_NAME = "teacher";
        /**
         * The name of the version
         * <P>Type: TEXT</P>
         */
        public static final String ID = "_id";
        public static final String VER = "ver";
        public static final String URL = "vurl";
        
        
        public static final int ID_INDEX = 0;
        public static final int VER_INDEX = 1;
        public static final int URL_INDEX = 2;

    }
    public interface StudyMethodInfo {
        /**
         * The table name
         */
        public static final String TABLE_NAME = "smi";
        /**
         * The name of the version
         * <P>Type: TEXT</P>
         */
        public static final String ID = "_id";
        public static final String GID = "gid";
        public static final String VER = "ver";
        public static final String BASEURL = "baseurl";
        public static final String URL = "url";
        

        public static final int ID_INDEX = 0;
        public static final int GID_INDEX = 1;
        public static final int VER_INDEX = 2;
        public static final int BASEURL_INDEX = 3;
        public static final int URL_INDEX = 4;

    }
    public interface StudyMethodDetail {
        /**
         * The table name
         *
         */
        public static final String TABLE_NAME = "smd";
        /**
         * The name of the version
         * <P>Type: TEXT</P>
         */
        public static final String ID = "_id";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String BASEURL = "baseurl";
        public static final String HASPIC = "haspic";
        public static final String GID = "gid";
        

        public static final int ID_INDEX = 0;
        public static final int TITLE_INDEX = 1;
        public static final int CONTENT_INDEX = 2;
        public static final int BASEURL_INDEX = 3;
        public static final int HASPIC_INDEX = 4;
        public static final int GID_INDEX = 5;
        public static final String LIST_COLUMNS[] = {
        	ID, TITLE
        };
        public static final String SORT_COLUMNS[] = {
        	ID, TITLE
        };

    }
    public interface StudyMethodDetailPic {
        /**
         * The table name
         */
        public static final String TABLE_NAME = "pic";
        /**
         * The name of the version
         * <P>Type: TEXT</P>
         */
        public static final String ID = "_id";
        public static final String URL = "url";
        public static final String BASEURL = "baseurl";
        

        public static final int ID_INDEX = 0;
        public static final int URL_INDEX = 1;
        public static final int BASEURL_INDEX = 2;

    }

    public interface Join {
        /**
         * The table name
         */
        public static final String TABLE_NAME = "join_table";

        /**
         * The grade id of the table
         * <P>Type: INTEGER (long)</P>
         */
        public static final String COLUMN_COURSE_ID = "course_id";

        /**
         * The subject id of the table
         * <P>Type: INTEGER (long)</P>
         */
        public static final String COLUMN_GRADE_ID = "gid";

        /**
         * The version id of the table
         * <P>Type: INTEGER (long)</P>
         */
        public static final String COLUMN_VERSION_ID = "vid";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMNS[] = {
        	COLUMN_COURSE_ID, COLUMN_GRADE_ID, COLUMN_VERSION_ID, COLUMN_TITLE
        };
    }

    public interface Course extends BaseColumns {
        /**
         * The table name
         */
        public static final String TABLE_NAME = "course";
        public static final String COLUMN_PID = "pid";
        
        public static final String COLUMNS[] = {
            COLUMN_ID, COLUMN_NAME, COLUMN_SORT_ORDER, COLUMN_PID
        };

        public static final int COLUMN_ID_INDEX         = 0;
        public static final int COLUMN_NAME_INDEX       = 1;
        public static final int COLUMN_SORT_ORDER_INDEX = 2;
        public static final int COLUMN_PID_INDEX       	= 3;
    }

    public interface Lesson extends BaseColumns {
        /**
         * The table name
         */
        public static final String TABLE_NAME = "lesson";

        /**
         * The url of the table
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_URL = "url";

        /**
         * The type of the table
         * <P>Type: INTEGER</P>
         */
        public static final String COLUMN_TYPE = "type";

        /**
         * The status of the table
         * <P>Type: INTEGER</P>
         */
        public static final String COLUMN_STATUS = "status";

        /**
         * The course id of the table
         * <P>Type: INTEGER (long)</P>
         */
        public static final String COLUMN_COURSE_ID = "course_id";
        public static final String COLUMN_ZIPNAME = "zipname";
        public static final String COLUMN_TID = "tid";
        //,cs INTEGER,zs INTEGER,ks INTEGER,ts INTEGER
        public static final String COLUMN_CS = "cs";
        public static final String COLUMN_ZS = "zs";
        public static final String COLUMN_KS = "ks";
        public static final String COLUMN_TS = "ts";
        

        /**
         * The default query columns
         */
        public static final String COLUMNS[] = {
            COLUMN_ID, COLUMN_NAME, COLUMN_URL, COLUMN_TYPE,
            COLUMN_STATUS, COLUMN_SORT_ORDER, COLUMN_COURSE_ID,COLUMN_ZIPNAME,COLUMN_TID,
            COLUMN_CS,COLUMN_ZS,COLUMN_KS,COLUMN_TS
        };

        public static final int COLUMN_ID_INDEX         = 0;
        public static final int COLUMN_NAME_INDEX       = 1;
        public static final int COLUMN_URL_INDEX        = 2;
        public static final int COLUMN_TYPE_INDEX       = 3;
        public static final int COLUMN_STATUS_INDEX     = 4;
        public static final int COLUMN_SORT_ORDER_INDEX = 5;
        public static final int COLUMN_COURSE_ID_INDEX  = 6;
        public static final int COLUMN_ZIPNAME_INDEX  = 7;
        public static final int COLUMN_TEACHER_INDEX  = 8;
        public static final int COLUMN_CS_INDEX  = 9;
        public static final int COLUMN_ZS_INDEX  = 10;
        public static final int COLUMN_KS_INDEX  = 11;
        public static final int COLUMN_TS_INDEX  = 12;
    }

    public interface Downloaded {
        /**
         * The table name
         */
        public static final String TABLE_NAME = "downloaded";

        /**
         * The unique ID for a row.
         * <P>Type: INTEGER (long)</P>
         */
        public static final String COLUMN_ID = "_id";
    }
    public interface UserInfo {
        /**
         * The table name
         */
        public static final String TABLE_NAME = "u_table";

        /**
         * The unique ID for a row.
         * <P>Type: INTEGER (long)</P>
         */
        public static final String COLUMN_ID = "_id";
        
        /**
         * The email of the user
         * <P>Type: TEXT</P>
         */
        public static final String COLUMN_NAME = "random";
        /**
         * The playtime.
         * <P>Type: INTEGER (long)</P>
         */
        public static final String COLUMN_URL = "checkurl";
        public static final String COLUMNS[] = {
            COLUMN_ID, COLUMN_NAME, COLUMN_URL
        };
        public static final int COLUMN_ID_INDEX         = 0;
        public static final int COLUMN_NAME_INDEX       = 1;
        public static final int COLUMN_URL_INDEX        = 2;
    }
    public interface TimeInfo {
        /**
         * The table name
         */
        public static final String TABLE_NAME = "t_table";

        /**
         * The unique ID for a row.
         * <P>Type: INTEGER (long)</P>
         */
        public static final String COLUMN_ID = "_id";
        /**
         * The playtime.
         * <P>Type: INTEGER (long)</P>
         */
        public static final String COLUMN_NAME = "name";
    }

    private static final StringBuilder where = new StringBuilder();
    public static final String BASE_WHERE = BaseColumns.COLUMN_ID + " IN (SELECT DISTINCT ";
    public static final String MYCOURSE_WHERE = BASE_WHERE + Join.COLUMN_COURSE_ID + " FROM " + Join.TABLE_NAME + ");";
    //public static final String ALLCOURSE_WHERE= Course.COLUMN_LEVEL+"="+2;
    
    public static String buildVersionWhere(long gradeId) {
        where.setLength(0);
        return where.append(BASE_WHERE).append(Join.COLUMN_VERSION_ID).append(" FROM ").append(Join.TABLE_NAME)
                    .append(" WHERE ").append(Join.COLUMN_GRADE_ID).append('=').append(gradeId).append(");").toString();
    }
    public static String buildContentVersionWhere(long gradeId) {
        where.setLength(0);
        return where.append(Course.COLUMN_SORT_ORDER).append('=').append(gradeId).toString();
    }

    public static String buildCourseWhere(long courseId) {
        where.setLength(0);
        return where.append(Course.COLUMN_PID).append('=').append(courseId).toString();
    }

    public static String buildLessonWhere(long courseId) {
        where.setLength(0);
        return where.append(Lesson.COLUMN_COURSE_ID).append('=').append(courseId).toString();
    }

	public static String buildParentCourseWhere(int lev, int pid) {
		where.setLength(0);
		return where.append(Course.COLUMN_PID)
				.append(" IN (select ").append(Course.COLUMN_PID).append(" from ")
				.append(Course.TABLE_NAME).append(" where ").append(Course.COLUMN_ID)
				.append("=").append(pid).append(");").toString();
	}

    public static String formatBytes(long size) {
        DecimalFormat format = (DecimalFormat)NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);

        return format.format(size / 1048576.0) + " MB";
    }

    public static final String STORAGE_PATH;
    public static final String DOWNLOAD_PATH;
    private static final String DOWNLOAD_DIRECTOTY = "tqxktbyw";

    public synchronized static final long getExternalStorageFreeSpace() {
        StatFs stat = new StatFs(STORAGE_PATH);
        return (long)stat.getAvailableBlocks() * (long)stat.getBlockSize();
    }
    public synchronized static final long getStorageFreeSpace() {
        StatFs stat = new StatFs("/data");
        return (long)stat.getAvailableBlocks() * (long)stat.getBlockSize();
    }

    static {
    	File sd = new File("/mnt/external_sd");
    	if(sd.exists()&& sd.canWrite()){
    		STORAGE_PATH = "/mnt/external_sd";
    	}else if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            STORAGE_PATH = Environment.getExternalStorageDirectory().getPath();
        } else {
            STORAGE_PATH = "/mnt/sdcard";
        }

        DOWNLOAD_PATH = STORAGE_PATH + File.separator + DOWNLOAD_DIRECTOTY;
        
        File directory = new File(DOWNLOAD_PATH);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File nomedia = new File(DOWNLOAD_PATH + File.separator + ".nomedia");
        if (!nomedia.exists()) {
        	try {
				nomedia.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    private EastStudy() {
    }

}