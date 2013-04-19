package com.tqxktbyw.study.entity;

public class BaseInfo {
	private int _ID;
	private String _Title;
	public int get_ID() {
		return _ID;
	}
	public void set_ID(int _ID) {
		this._ID = _ID;
	}
	public void set_ID(String _ID) {
		if(_ID ==null || _ID.equals("")){
			this._ID = 0;
		}else
		this._ID = Integer.parseInt(_ID);
	}
	public String get_Title() {
		return _Title;
	}
	public void set_Title(String _Title) {
		this._Title = _Title;
	}

}
