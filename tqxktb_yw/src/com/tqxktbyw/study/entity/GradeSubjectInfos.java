package com.tqxktbyw.study.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class GradeSubjectInfos extends MyBase{
	/**
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
	 * */
	private List<BaseInfo> _grade;
	private List<BaseInfo> _version;
	private List<CourseJoin> _course;

	public  List<BaseInfo> get_Grade() {
		return _grade;
	}
	public  void set_grade(List<Object> grade) {
		List<BaseInfo> sigs = new ArrayList<BaseInfo>();
		for(Object o:grade){
			sigs.add((BaseInfo)o);
		}
		this._grade = sigs;
	}
	public void set_grade(JSONArray jsonarray) {
		this.set_grade(Common.set_grade(jsonarray, BaseInfo.class, ConstUtiles.GRADE_COLUMNS));
	}
	public void set_grade(String jsonarray) {
		try {
			JSONArray jary =new JSONArray(jsonarray);
			this.set_grade(jary);
		} catch (JSONException e) {
			e.printStackTrace();
			this._grade = null;
		}
	}
	public  List<BaseInfo> getVersion() {
		return _version;
	}
	public  void set_version(List<Object> version) {
		List<BaseInfo> sigs = new ArrayList<BaseInfo>();
		for(Object o:version){
			sigs.add((BaseInfo)o);
		}
		this._version = sigs;
	}
	public void set_version(JSONArray jsonarray) {
		
		this.set_version(Common.set_grade(jsonarray, BaseInfo.class, ConstUtiles.VERSION_COLUMNS));
	}
	public void set_version(String jsonarray) {
		try {
			JSONArray jary =new JSONArray(jsonarray);
			this.set_version(jary);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this._version = null;
		}
	}
	public  List<CourseJoin> getCourse() {
		return _course;
	}
	public  void set_course(List<Object> course) {
		List<CourseJoin> sigs = new ArrayList<CourseJoin>();
		for(Object o:course){
			sigs.add((CourseJoin)o);
		}
		this._course = sigs;
	}
	public void set_course(JSONArray jsonarray) {
		
		this.set_course(Common.set_grade(jsonarray, CourseJoin.class, ConstUtiles.COURSE_COLUMNS));
	}
	public void set_course(String jsonarray) {
		try {
			JSONArray jary =new JSONArray(jsonarray);
			this.set_course(jary);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this._course = null;
		}
	}
}
