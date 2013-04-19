package com.tqxktbyw.study.entity;

public class ConstUtiles {

	public static final int GRADE_SELECT_DIALOG_ID = 6;
	public static final String SELECTED_GRADE = "selectedgrade";
	public static final String SELECTED_CP = "selectedchildposition";
	public static final String SELECTED_GP = "selectedgradeposition";
	public static final String SELECTED_SITE = "selectedsite";
	public static final String CHECKURL = "checkurl";
	public static String SUCCESS = "success";
	public static String LOGIN_VERSION = "ver";
	public static String LOGIN_USER = "user";
	public static String LOGIN_ADDRV = "addressV";
	public static String LOGIN_ADDRZ = "addressZ";
	public static String LOGIN_XMLFILE = "xmlFile";
	public static String LOGIN_CHECKURL = "checkURL";
	public static final String LOGIN_COLUMNS[] = {
		SUCCESS,LOGIN_VERSION,LOGIN_USER,LOGIN_ADDRV,LOGIN_ADDRZ,LOGIN_XMLFILE,LOGIN_CHECKURL
    };
	
	public static String NEWVER = "NewVer";
	public static String UPDATENUM = "UpdateNum";
	public static String UPDATEURL = "UpdateUrl";
	public static String UPDATEMAST = "UpdateMast";
	public static String[] VER_COLUMNS = {
		NEWVER,UPDATENUM,UPDATEURL,UPDATEMAST
	};
	public static String ISTQUSER = "isTQUser";
	public static String USRID = "usrID";
	public static String RANDUM = "randNum";
	public static String[] USER_COLUMNS = {
		ISTQUSER,USRID,RANDUM
	};
	public static String ADDR_AID = "aid";
	public static String ADDR_SID = "sid";
	public static String ADDR_SNAME = "sname";
	public static String[] ADDR_COLUMNS = {
		ADDR_AID,ADDR_SID,ADDR_SNAME
	};
	public static String XML_CVER = "cver";
	public static String XML_CFILE = "cFile";
	public static String XML_LVER = "lver";
	public static String XML_LFILE = "lFile";
	public static String[] XML_COLUMNS = {
		XML_CVER,XML_CFILE,XML_LVER,XML_LFILE
	};
	//
	public static final String GRADE_NAME = "grade";
	public static final String COLUMN_ID = "ID";
	public static final String COLUMN_TITLE = "Title";
	public static final String VERSION_NAME = "version";
	public static final String COURSE_NAME = "course";
	public static final String GRADE_ID = "GID";
	public static final String VERSION_ID = "VID";
	public static final String GRADE_COLUMNS[] = {
        COLUMN_ID,COLUMN_TITLE
    };
	public static final String VERSION_COLUMNS[] = {
        COLUMN_ID, COLUMN_TITLE
    };
	public static final String COURSE_COLUMNS[] = {
        COLUMN_ID, COLUMN_TITLE,GRADE_ID, VERSION_ID
    };

	public static final String FIRST_GRADE_SUBJECT_COURSE_COLUMNS[] = {
		SUCCESS,GRADE_NAME, VERSION_NAME, COURSE_NAME
    };

	public static final String CHECKLESSON_VURL = "vURL";
	public static final String CHECKLESSON_ZURL = "zURL";
	public static final String CHECKLESSON_BASEURL = "BURL";
	public static final String CHECKLESSON_STUDY_METHOD = "studyMethod";
	public static final String CHECKLESSON_TID = "tid";
	public static final String CHECKLESSON_TVER = "tver";
	public static final String CHECKLESSON_TURL = "turl";
	public static final String CHECKLESSON_COLUMNS[] = {
		SUCCESS,CHECKLESSON_VURL, CHECKLESSON_ZURL,CHECKLESSON_BASEURL,CHECKLESSON_STUDY_METHOD,
		CHECKLESSON_TID,CHECKLESSON_TVER,CHECKLESSON_TURL
    };
	public static final String STUDYMETHOD_GID = "gid";
	public static final String DNID="nid";
	public static final String STUDYMETHOD_COLUMNS[] = {
		STUDYMETHOD_GID,DNID,LOGIN_VERSION,LOGIN_XMLFILE
    };
	
	public static String LESSON_NAME = "lesson";
	public static String ID = "ID";
	public static String TITLE = "Title";
	public static String CID = "CID";
	public static String CS = "CS";
	public static String ZS = "ZS";
	public static String KS = "KS";
	public static String TS = "TS";
	public static String TID = "TID";
	public static String LVER = "Lver";
	public static String[] MYLESSONINFO_COLUMNS = {
		ID,TITLE,CID,CS,ZS,KS,TS,TID,LVER
	};

	public static final String BASEURL="Baseurl";
	public static final String PIC="PIC";
	
	public static final String NID="NID";
	public static final String NT="NT";
	public static final String NCONTENT="NContend";
	
	public static final String STATE="state";
	public static final String URL="url";
	public static final String IMG="IMG";
	public static String[] STUDYMETHODDETAIL_COLUMNS = {
		SUCCESS,BASEURL,NID,NT,NCONTENT,PIC
	};
	public static String[] STUDYMETHODDETAIL_PIC_COLUMNS = {
		STATE,URL
	};
	public static String[] STUDYMETHODDETAIL_IMG_COLUMNS = {
		IMG
	};
}
