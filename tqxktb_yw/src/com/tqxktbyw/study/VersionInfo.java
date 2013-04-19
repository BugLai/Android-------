package com.tqxktbyw.study;

public class VersionInfo {
	private int _verNum;
	private int _mver;
	private int _lver;
	public int get_verNum() {
		return _verNum;
	}
	public void set_verNum(int _verNum) {
		this._verNum = _verNum;
	}
	public int get_mver() {
		return _mver;
	}
	public void set_mver(int _mver) {
		this._mver = _mver;
	}
	public int get_lver() {
		return _lver;
	}
	public void set_lver(int _lver) {
		this._lver = _lver;
	}
	public VersionInfo() {
		this._lver=0;
		this._mver=0;
		this._verNum=0;
	}

}
