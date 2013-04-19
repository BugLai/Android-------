package com.tqxktbyw.study.entity;

public class AddressInfo {
	private int _aid;
	private int _sid;
	private String _sname;
	public int get_aid() {
		return _aid;
	}
	public void set_aid(int _aid) {
		this._aid = _aid;
	}

	public void set_aid(String _aid) {
		this._aid = Common.String2Int(_aid);
	}
	public int get_sid() {
		return _sid;
	}
	public void set_sid(int _sid) {
		this._sid = _sid;
	}

	public void set_sid(String _sid) {
		this._sid = Common.String2Int(_sid);
	}
	public String get_sname() {
		return _sname;
	}
	public void set_sname(String _sname) {
		this._sname = _sname;
	}

}
