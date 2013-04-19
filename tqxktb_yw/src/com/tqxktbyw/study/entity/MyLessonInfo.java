package com.tqxktbyw.study.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class MyLessonInfo {
	
	private int _id;
	private String _title;
	private int _pid;
	private boolean _CS;
	private boolean _ZS;
	private boolean _KS;
	private boolean _TS;
	private int _TID;
	private int _Lver;
	public int get_Lver() {
		return _Lver;
	}
	public void set_Lver(int _Lver) {
		this._Lver = _Lver;
	}
	public void set_Lver(String _lver) {
		this._Lver = Common.String2Int(_lver);
	}
	public int get_TID() {
		return _TID;
	}
	public void set_TID(int _TID) {
		this._TID = _TID;
	}
	public void set_TID(String _TID) {
		this._TID = Common.String2Int(_TID);
	}
	public boolean is_CS() {
		return _CS;
	}
	public void set_CS(boolean _CS) {
		this._CS = _CS;
	}
	public void set_CS(int _CS) {
		this._CS = _CS == 1;
	}
	public void set_CS(String _CS) {
		this._CS = Common.String2Boolean(_CS);
	}
	public boolean is_ZS() {
		return _ZS;
	}
	public void set_ZS(boolean _ZS) {
		this._ZS = _ZS;
	}
	public void set_ZS(int _CS) {
		this._ZS = _CS == 1;
	}
	public void set_ZS(String _CS) {
		this._ZS = Common.String2Boolean(_CS);
	}
	public boolean is_KS() {
		return _KS;
	}
	public void set_KS(boolean _KS) {
		this._KS = _KS;
	}
	public void set_KS(int _CS) {
		this._KS = _CS == 1;
	}
	public void set_KS(String _CS) {
		this._KS = Common.String2Boolean(_CS);
	}
	public boolean is_TS() {
		return _TS;
	}
	public void set_TS(boolean _TS) {
		this._TS = _TS;
	}public void set_TS(int _CS) {
		this._TS = _CS == 1;
	}
	public void set_TS(String _CS) {
		this._TS = Common.String2Boolean(_CS);
	}
	public int get_id() {
		return _id;
	}
	public void set_ID(int _id) {
		this._id = _id;
	}
	public void set_ID(String _id) {
		this._id = Common.String2Int(_id);
	}
	public String get_title() {
		return _title;
	}
	public void set_Title(String _title) {
		this._title = _title;
	}
	public int get_CID() {
		return _pid;
	}
	public void set_CID(int _pid) {
		this._pid = _pid;
	}
	public void set_CID(String _pid) {
		this._pid = Common.String2Int(_pid);
	}
	/*
	 * mini constructure
	 */
	public MyLessonInfo() {
	}
	public static List<MyLessonInfo> GetListLessonInfo(JSONArray jsonarray) {
		List<MyLessonInfo> lm =new ArrayList<MyLessonInfo>();
		List<Object> lo =Common.set_grade(jsonarray, MyLessonInfo.class, ConstUtiles.MYLESSONINFO_COLUMNS);
		for(Object o:lo){
			lm.add((MyLessonInfo)o);
		}
		return lm;
	}
	public static List<MyLessonInfo> GetListLessonInfo(String jsonarray) {
		try {
			JSONArray jary =new JSONArray(jsonarray);
			return GetListLessonInfo(jary);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
