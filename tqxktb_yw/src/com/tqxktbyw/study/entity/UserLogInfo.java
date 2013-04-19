package com.tqxktbyw.study.entity;

import java.io.Serializable;

public class UserLogInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean _isTQUser;
	private int _usrID;
	private String _randNum;
	private String _checkurl;
	private int _cver;
	private int _lver;
	public int get_cver() {
		return _cver;
	}
	public void set_cver(int _cver) {
		this._cver = _cver;
	}

	public void set_cver(String _cver) {
		this._cver = Common.String2Int(_cver);
	}
	public int get_lver() {
		return _lver;
	}
	public void set_lver(int _lver) {
		this._lver = _lver;
	}
	public void set_lver(String _lver) {
		this._lver = Common.String2Int(_lver);
	}
	public String get_checkurl() {
		return _checkurl;
	}
	public void set_checkurl(String _checkurl) {
		this._checkurl = _checkurl;
	}
	public boolean is_isTQUser() {
		return _isTQUser;
	}
	public void set_isTQUser(boolean _isTQUser) {
		this._isTQUser = _isTQUser;
	}
	public  void set_isTQUser(String _isTQUser) {
		this._isTQUser = Common.String2Boolean(_isTQUser);
	}
	public  void set_istquser(String _isTQUser) {
		this._isTQUser = Common.String2Boolean(_isTQUser);
	}
	
	public int get_usrID() {
		return _usrID;
	}
	public void set__id(int _usrID) {
		this._usrID = _usrID;
	}
	public void set__id(String _usrID) {
		this._usrID = Common.String2Int(_usrID);
	}
	public void set_usrID(int _usrID) {
		this._usrID = _usrID;
	}
	public void set_usrID(String _usrID) {
		this._usrID = Common.String2Int(_usrID);
	}
	public String get_randNum() {
		return _randNum;
	}
	public void set_randNum(String _randNum) {
		this._randNum = _randNum;
	}
	public void set_random(String _randNum) {
		this._randNum = _randNum;
	}
	@Override
	public String toString() {
		return this._randNum+":"+this._usrID + ":"+ this._isTQUser;
	}

}
