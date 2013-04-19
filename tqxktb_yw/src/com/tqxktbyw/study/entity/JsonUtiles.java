package com.tqxktbyw.study.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


public class JsonUtiles {
	
	/**
	 * 解析LessonCheck返回信息，获取课件真实路径
	 * 
	 * @param lessoninfo LessonCheck返回字串
	 * @return Object LessonCheckInfo
	  */
	
	public static LessonCheckInfo GetRightsLesson(String lessoninfo){
		LessonCheckInfo ci =new LessonCheckInfo();
		try {
			JSONObject jo = new JSONObject(lessoninfo);
			if(jo.getInt(ConstUtiles.SUCCESS) == 1){
				for(int j=0;j<ConstUtiles.CHECKLESSON_COLUMNS.length;j++){
					String columnname = ConstUtiles.CHECKLESSON_COLUMNS[j];
					ci.getClass().getMethod("set_"+columnname, String.class).invoke(ci, jo.getString(columnname));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ci;
	}
	
	/**
	 * 解析登录信息
	 * @param String jsonlist 登录返回值
	 * @return LoginInfos对象
	 *
	{
		"success":"",	//返回是否正常 0-不正常

		"ver":[{  //版本
			"NewVer":"1.0.1",	//最新版本号
			"UpdateNum":"1",	//最新版本升级批次号
			"UpdateUrl":"#",	//最新版本下载地址
			"UpdateMast":"0"	//是否强制更新：0-否、1-是
		}],

		"user":[{  //用户
			"isTQUser":"0",	//	//是否太奇用户：0-否、1-是
			"usrID":"0",	//用户ID
			"randNum":"0"	//用户登录成功返回的随机码
		}],

		"addressV":[	//视频服务器的地址 Video
			{
				"aid":"1",	//address 记录ID
				"sid":"1",	//站点类型ID，联通-1、电信-2
				"sname":"联通"		//站点名称
			},
			{
				"aid":"2",	//address 记录ID
				"sid":"2",	//站点类型ID，联通-1、电信-2
				"sname":"电信"	//站点名称
			}
		],

		"addressZ":[	//服务器的地址 ZIP
			{
				"aid":"1",	//address 记录ID
				"sid":"1",	//站点类型ID，联通-1、电信-2
				"sname":"联通"		//站点名称
			},
			{
				"aid":"2",	//address 记录ID
				"sid":"2",	//站点类型ID，联通-1、电信-2
				"sname":"电信"	//站点名称
			}
		],

		"xmlFile":[	//课程目录json文件URL 及 课件目录json文件URL
			{
				"cver":"0",		//课程目录最新更新批次号
				"cFile":"#"		//课程目录json文件URL
			},
			{
				"lver":"0",		//课件目录最新更新批次号
				"lFile":"#"		//课件目录json文件URL
			}
		],

		"checkURL":"#"	//课件下载验证地址 需向此URL POST：学员ID、登录随机码、学科ID、课程ID、课件ID、常用站点ID

	}
	*/
	public static LoginInfos InitLoginInfos(String jsonlist) {
		
		LoginInfos ci =new LoginInfos();
		try {
				JSONObject jo = new JSONObject(jsonlist);
				for(int j=0;j<ConstUtiles.LOGIN_COLUMNS.length;j++){
					String columnname = ConstUtiles.LOGIN_COLUMNS[j];
					ci.getClass().getMethod("set_"+columnname, String.class).invoke(ci, jo.getString(columnname));
				}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ci;
	}
	
	/**
	 * 
	 {"success":"1",
	 "ver":[
	 {"NewVer":"1.0.1","UpdateNum":"1","UpdateUrl":"http://pad2.tqpad.com/file/tqpadyw.apk","UpdateMast":"0"}],
	 "user":[
	 {"isTQUser":"1","usrID":"1","randNum":"WBjutjyckq62wJVr"}],
	 "addressV":[
	 {"aid":"1","sid":"1","sname":"联通"},
	 {"aid":"2","sid":"2","sname":"电信"}],
	 "addressZ":[
	 {"aid":"3","sid":"1","sname":"联通"},
	 {"aid":"4","sid":"2","sname":"电信"}],
	 "xmlFile":[
	 {"cver":"19","cFile":"http://pad2.tqpad.com/file/course_Subject_List_1.xml"},
	 {"lver":"112","lFile":"http://pad2.tqpad.com/file/course_Lesson_Subject_List_1.xml"}],
	 "checkURL":"http://pad2.tqpad.com/lessoncheck/lessoncheck_subject.php"}
	 
	 {"success":"1",
"grade":[{"ID":"4","Title":"小学初级"},
{"ID":"5","Title":"小学中级"},
{"ID":"6","Title":"小学高级"},
{"ID":"7","Title":"初中一年级"},
{"ID":"8","Title":"初中二年级"},
{"ID":"9","Title":"初中三年级"},
{"ID":"11","Title":"高中一年级"},
{"ID":"12","Title":"高中二年级"},
{"ID":"13","Title":"高中三年级"}],
"version":[{"ID":"36","Title":"北京版"},
{"ID":"2","Title":"北师大版"},
{"ID":"4","Title":"沪教版"},
{"ID":"34","Title":"沪科版"},
{"ID":"32","Title":"济南版"},
{"ID":"35","Title":"冀教版"},
{"ID":"5","Title":"鲁科版"},
{"ID":"33","Title":"牛津译林版"},
{"ID":"1","Title":"人教实验版"},
{"ID":"31","Title":"人民版"},
{"ID":"29","Title":"苏人版"},
{"ID":"3","Title":"湘教版"},
{"ID":"30","Title":"语文出版社"}],
"course":[{"ID":"1","GID":"7","VID":"1"},
{"ID":"2","GID":"5","VID":"1"},
{"ID":"3","GID":"9","VID":"1"},
{"ID":"6","GID":"7","VID":"2"}]}
*/
	public static GradeSubjectInfos Get_GradeSubjectInfos(String lessoninfo){
		GradeSubjectInfos ci =new GradeSubjectInfos();
		try {
			JSONObject jo = new JSONObject(lessoninfo);
			if(jo.getInt(ConstUtiles.SUCCESS) == 1){
				for(int j=0;j<ConstUtiles.FIRST_GRADE_SUBJECT_COURSE_COLUMNS.length;j++){
					String columnname = ConstUtiles.FIRST_GRADE_SUBJECT_COURSE_COLUMNS[j];
					ci.getClass().getMethod("set_"+columnname, String.class).invoke(ci, jo.getString(columnname));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ci;
	}
/*
{"success":"1",
"lesson":[{"ID":"1","Title":"课件测试第一节","CID":"1","CS":"1","ZS":"0","KS":"1","TS":"0"},
{"ID":"3","Title":"课件测试第2节","CID":"1","CS":"1","ZS":"0","KS":"1","TS":"0"},

{"ID":"20","Title":"初三语文2","CID":"3","CS":"1","ZS":"1","KS":"1","TS":"0"},
{"ID":"4","Title":"初一语文北师大版1","CID":"6","CS":"1","ZS":"1","KS":"1","TS":"0"}]}

"CS":"1",	测试
"ZS":"0",	中高考解析
"KS":"1",	课外拓展
"TS"		同步听力
	 */
	public static List<MyLessonInfo> GetLessonInfo(String lessoninfo){
		List<MyLessonInfo> ci =new ArrayList<MyLessonInfo>();
		try {
			JSONObject jo = new JSONObject(lessoninfo);
			if(jo.getInt(ConstUtiles.SUCCESS) == 1){
				ci = MyLessonInfo.GetListLessonInfo(jo.getString(ConstUtiles.LESSON_NAME));
				return ci;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 学法同步信息解析
	 * @param lessoninfo
	 * @return
	 */
	public static StudyMethodDetail GetStudyMethodDetailInfos(String jsonlist) {
		
		StudyMethodDetail ci =new StudyMethodDetail();
		try {
				JSONObject jo = new JSONObject(jsonlist);
				for(int j=0;j<ConstUtiles.STUDYMETHODDETAIL_COLUMNS.length;j++){
					String columnname = ConstUtiles.STUDYMETHODDETAIL_COLUMNS[j];
					ci.getClass().getMethod("set_"+columnname, String.class).invoke(ci, jo.getString(columnname));
				}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ci;
	}
}
