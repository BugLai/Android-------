package com.tqxktbyw.study.entity;

public class LoginXmlFileInfo {
	
	private int _cver;
	private String _cFile;
	private int _lver;
	private String _lFile;
	public int get_cver() {
		return _cver;
	}
	public void set_cver(int _cver) {
		this._cver = _cver;
	}
	public void set_cver(String _cver) {
		this._cver = Common.String2Int(_cver);
	}
	public String get_cFile() {
		return _cFile;
	}
	public void set_cFile(String _cFile) {
		this._cFile = _cFile;
	}
	public int get_lver() {
		return _lver;
	}
	public void set_lver(int _cver) {
		this._lver = _cver;
	}
	public void set_lver(String _cver) {
		this._lver = Common.String2Int(_cver);
	}
	public String get_lFile() {
		return _lFile;
	}
	public void set_lFile(String _cFile) {
		this._lFile = _cFile;
	}

}
