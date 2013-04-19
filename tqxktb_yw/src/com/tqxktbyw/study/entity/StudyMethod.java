package com.tqxktbyw.study.entity;

import java.io.Serializable;

public class StudyMethod implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * {"success":"1",
	 * "vURL":"http://wt.tqpad.com/_padcourse2/file/xxx.zip",
	 * "zURL":"http://wt.tqpad.com/_padcourse2/file/a.html",
	 * "BURL":"http://pad2.tqpad.com/file/course_StudyMothod_List_YW_7_47.xml"
	 * , "studyMethod":[
	 * {"gid":"7","ver":"2","xmlFile":"course_StudyMothod_List_YW_7_46.xml"
	 * },
	 * {"gid":"7","ver":"2","xmlFile":"course_StudyMothod_List_YW_7_47.xml"
	 * },
	 * {"gid":"7","ver":"3","xmlFile":"course_StudyMothod_List_YW_7_7.xml"
	 * }], "tid":"2", "tver":"4",
	 * "turl":"http://pad2.tqpad.com/upfile/teacherVideo/a.mp4"}
	 */
	public StudyMethod()
	{

	}

	private int _gid;
	private int _ver;
	private String _xmlFile;

	private int _nid;

	public int get_gid()
	{
		return _gid;
	}

	public void set_gid(int _gid)
	{
		this._gid = _gid;
	}

	public void set_gid(String _gid)
	{
		if (_gid == null || _gid.equals(""))
		{
			this._gid = 0;
		}
		else
			this._gid = Integer.parseInt(_gid);
	}

	public int get_ver()
	{
		return _ver;
	}

	public void set_ver(int _ver)
	{
		this._ver = _ver;
	}

	public void set_ver(String _ver)
	{
		if (_ver == null || _ver.equals(""))
		{
			this._ver = 0;
		}
		else
			this._ver = Integer.parseInt(_ver);
	}

	public String get_xmlFile()
	{
		return _xmlFile;
	}

	public void set_xmlFile(String _xmlFile)
	{
		this._xmlFile = _xmlFile;
	}

	public int get_nid()
	{
		return _nid;
	}

	public void set_nid(int _nid)
	{
		this._nid = _nid;
	}

	public void set_nid(String _nid)
	{
		this._nid = Common.String2Int(_nid);
	}

	@Override
	public String toString()
	{
		return "StudyMethod [_gid=" + _gid + ", _ver=" + _ver + ", _xmlFile=" + _xmlFile + ", _nid=" + _nid + "]";
	}
}
