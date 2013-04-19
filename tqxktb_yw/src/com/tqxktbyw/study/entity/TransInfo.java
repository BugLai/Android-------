package com.tqxktbyw.study.entity;

import java.io.Serializable;
import java.util.List;

public class TransInfo  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 
	 intent.putExtra(EastStudy.PLAYING_URI, cur.getString(Lesson.COLUMN_URL_INDEX));
		intent.putExtra(EastStudy.UserInfo.COLUMN_NAME, uli);
		intent.putExtra(EastStudy.Lesson.COLUMN_ZIPNAME, cur.getString(Lesson.COLUMN_ZIPNAME_INDEX));
		intent.putExtra(EastStudy.Teacher.URL, CheckPrefaceInfo(cur.getInt(Lesson.COLUMN_TEACHER_INDEX),0));
	 */
	private String _vUrl;
	private String _zUrl;
	private String _tUrl;
	private String _Title;
	public String get_Title() {
		return _Title;
	}
	public void set_Title(String _Title) {
		this._Title = _Title;
	}
	private long _tid;
	private boolean _CS;
	private boolean _ZS;
	private boolean _KS;
	private boolean _TS;
	private List<StudyMethod> _studyMethod;
	public String get_vUrl() {
		return _vUrl;
	}
	public void set_vUrl(String _vUrl) {
		this._vUrl = _vUrl;
	}
	public String get_zUrl() {
		return _zUrl;
	}
	public void set_zUrl(String _zUrl) {
		this._zUrl = _zUrl;
	}
	public String get_tUrl() {
		return _tUrl;
	}
	public void set_tUrl(String _tUrl) {
		this._tUrl = _tUrl;
	}
	public long get_tid() {
		return _tid;
	}
	public void set_tid(long _tid) {
		this._tid = _tid;
	}
	public boolean is_CS() {
		return _CS;
	}
	public void set_CS(boolean _CS) {
		this._CS = _CS;
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
	public void set_ZS(String _ZS) {
		this._ZS = Common.String2Boolean(_ZS);
	}
	public boolean is_KS() {
		return _KS;
	}
	public void set_KS(boolean _KS) {
		this._KS = _KS;
	}
	public void set_KS(String _KS) {
		this._KS = Common.String2Boolean(_KS);
	}
	public boolean is_TS() {
		return _TS;
	}
	public void set_TS(boolean _TS) {
		this._TS = _TS;
	}
	public void set_TS(String _TS) {
		this._TS = Common.String2Boolean(_TS);
	}
	public List<StudyMethod> get_studyMethod() {
		return _studyMethod;
	}
	public void set_studyMethod(List<StudyMethod> _studyMethod) {
		this._studyMethod = _studyMethod;
	}
	

}
