package com.tqxktbyw.study.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class LessonCheckInfo extends MyBase{
	private String _vURL;
	private String _zURL;
	private String _BURL;
	private int _tid;
	private int _tver;
	private String _turl;
	private List<StudyMethod> _studyMethod;
	
	public int get_tid() {
		return _tid;
	}
	public void set_tid(int _tid) {
		this._tid = _tid;
	}
	public void set_tid(String _tid) {
		this._tid = Common.String2Int(_tid);
	}
	public int get_tver() {
		return _tver;
	}
	public void set_tver(int _tver) {
		this._tver = _tver;
	}
	public void set_tver(String _tver) {
		this._tver = Common.String2Int(_tver);
	}
	public String get_turl() {
		return _turl;
	}
	public void set_turl(String _turl) {
		this._turl = _turl;
	}
	public String get_BURL() {
		return _BURL;
	}
	public void set_BURL(String _BURL) {
		this._BURL = _BURL;
	}

	public String get_vURL() {
		return _vURL;
	}
	public void set_vURL(String _vURL) {
		this._vURL = _vURL;
	}
	public String get_zURL() {
		return _zURL;
	}
	public void set_zURL(String _zURL) {
		this._zURL = _zURL;
	}
	public List<StudyMethod> get_studyMethod() {
		return _studyMethod;
	}
	public  void set_studyMethod(List<Object> _studyMethod) {
		List<StudyMethod> sigs = new ArrayList<StudyMethod>();
		for(Object o:_studyMethod){
			sigs.add((StudyMethod)o);
		}
		this._studyMethod = sigs;
	}
	public void set_studyMethod(JSONArray jsonarray) {
		
		this.set_studyMethod(Common.set_grade(jsonarray, StudyMethod.class, ConstUtiles.STUDYMETHOD_COLUMNS));
	}
	public void set_studyMethod(String jsonarray) {
		try {
			JSONArray jary =new JSONArray(jsonarray);
			this.set_studyMethod(jary);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this._studyMethod = null;
		}
	}
	public LessonCheckInfo() {
	}

}
