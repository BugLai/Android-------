package com.tqxktbyw.study.entity;
/*
 * 课程信息实体类
 */
public class AllCourseInfo extends MyLessonInfo{
	private int _level;
	private boolean _final;
	private long _endtime;

	public int get_level() {
		return _level;
	}
	public void set_level(int _level) {
		this._level = _level;
	}
	public boolean get_final() {
		return _final;
	}
	public void set_final(boolean _final) {
		this._final = _final;
	}
	public long get_endtime() {
		return _endtime;
	}
	public void set_endtime(long _endtime) {
		this._endtime = _endtime;
	}
}
