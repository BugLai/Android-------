package com.tqxktbyw.study.entity;

public class CourseJoin {
	private int _ID;
	private int _GID;
	private int _VID;
	private String _Title;
	public String get_Title() {
		return _Title;
	}
	public void set_Title(String _Title) {
		this._Title = _Title;
	}
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
	public int get_GID() {
		return _GID;
	}
	public void set_GID(int _GID) {
		this._GID = _GID;
	}

	public void set_GID(String _GID) {
		if(_GID ==null || _GID.equals("")){
			this._GID = 0;
		}else
		this._GID = Integer.parseInt(_GID);
	}
	public int get_VID() {
		return _VID;
	}
	public void set_VID(int _VID) {
		this._VID = _VID;
	}
	public void set_VID(String _VID) {
		if(_VID ==null || _VID.equals("")){
			this._VID = 0;
		}else
		this._VID = Integer.parseInt(_VID);
	}

}
