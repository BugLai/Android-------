package com.tqxktbyw.study.entity;

public abstract class MyBase {
	private boolean _success;
	public  boolean isSuccess() {
		return _success;
	}
	public  void set_success(boolean success) {
		this._success = success;
	}
	public  void set_success(String success) {
		this._success = success.equals("1");
	}
}
