package com.tqxktbyw.study.entity;

import java.util.HashMap;

public class VerifyInfo {
	private int _verNum;
	private String _verUpdateUrl;
	private int _verMastUpdate;
	private int _isTQUser;
	private int _usrID;
	private String _randNum;
	private long _serverTime;
	private HashMap<String,String> _address;
	private String _stuCourseURL;
	private String _stuLessonURL;
	private String _adpic;
	private String _adURL;
	public int get_verNum() {
		return _verNum;
	}
	public void set_verNum(int _verNum) {
		this._verNum = _verNum;
	}
	public String get_verUpdateUrl() {
		return _verUpdateUrl;
	}
	public void set_verUpdateUrl(String _verUpdateUrl) {
		this._verUpdateUrl = _verUpdateUrl;
	}
	public int get_verMastUpdate() {
		return _verMastUpdate;
	}
	public void set_verMastUpdate(int _verMastUpdate) {
		this._verMastUpdate = _verMastUpdate;
	}
	public int get_isTQUser() {
		return _isTQUser;
	}
	public void set_isTQUser(int _isTQUser) {
		this._isTQUser = _isTQUser;
	}
	public int get_usrID() {
		return _usrID;
	}
	public void set_usrID(int _usrID) {
		this._usrID = _usrID;
	}
	public String get_randNum() {
		return _randNum;
	}
	public void set_randNum(String _randNum) {
		this._randNum = _randNum;
	}
	public long get_serverTime() {
		return _serverTime;
	}
	public void set_serverTime(long _serverTime) {
		this._serverTime = _serverTime;
	}
	public HashMap<String,String> get_address() {
		return _address;
	}
	public void set_address(HashMap<String,String> _address) {
		this._address = _address;
	}
	public String get_stuCourseURL() {
		return _stuCourseURL;
	}
	public void set_stuCourseURL(String _stuCourseURL) {
		this._stuCourseURL = _stuCourseURL;
	}
	public String get_stuLessonURL() {
		return _stuLessonURL;
	}
	public void set_stuLessonURL(String _stuLessonURL) {
		this._stuLessonURL = _stuLessonURL;
	}
	public String get_adpic() {
		return _adpic;
	}
	public void set_adpic(String _adpic) {
		this._adpic = _adpic;
	}
	public String get_adURL() {
		return _adURL;
	}
	public void set_adURL(String _adURL) {
		this._adURL = _adURL;
	}
	/**
	 * miniStructure
	 */
	public VerifyInfo() {
	}
}
