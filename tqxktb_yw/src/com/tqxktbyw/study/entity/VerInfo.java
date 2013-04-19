package com.tqxktbyw.study.entity;

public class VerInfo {
	private String _NewVer;
	private int _UpdateNum;
	private String _UpdateUrl;
	private boolean _UpdateMast;
	public String get_NewVer() {
		return _NewVer;
	}
	public void set_NewVer(String _NewVer) {
		this._NewVer = _NewVer;
	}
	public int get_UpdateNum() {
		return _UpdateNum;
	}
	public void set_UpdateNum(int _UpdateNum) {
		this._UpdateNum = _UpdateNum;
	}
	public void set_UpdateNum(String _UpdateNum) {
		
		this._UpdateNum = Common.String2Int(_UpdateNum);
	}
	
	public String get_UpdateUrl() {
		return _UpdateUrl;
	}
	public void set_UpdateUrl(String _UpdateUrl) {
		this._UpdateUrl = _UpdateUrl;
	}
	public boolean is_UpdateMast() {
		return _UpdateMast;
	}
	
	public void set_UpdateMast(boolean _UpdateMast) {
		this._UpdateMast = _UpdateMast;
	}
	
	public  void set_UpdateMast(String _UpdateMast) {
		this._UpdateMast = Common.String2Boolean(_UpdateMast);
	}

}
