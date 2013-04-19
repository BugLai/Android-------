package com.tqxktbyw.study.entity;

import java.io.Serializable;

public class StudyMethodDetail implements Serializable
{

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 2L;

	public StudyMethodDetail()
	{

	}

	private boolean _success;

	private String _Baseurl;
	private int _NID;
	private String _NT;
	private String _NContend;
	private SMPic _PIC;

	public boolean isSuccess()
	{
		return _success;
	}

	public void set_success(boolean success)
	{
		this._success = success;
	}

	public void set_success(String success)
	{
		this._success = success.equals("1");
	}

	public String get_Baseurl()
	{
		return _Baseurl;
	}

	public void set_Baseurl(String _Baseurl)
	{
		this._Baseurl = _Baseurl;
	}

	public int get_NID()
	{
		return _NID;
	}

	public void set_NID(int _NID)
	{
		this._NID = _NID;
	}

	public void set_NID(String _NID)
	{
		this._NID = Common.String2Int(_NID);
	}

	public String get_NT()
	{
		return _NT;
	}

	public void set_NT(String _NT)
	{
		this._NT = _NT;
	}

	public String get_NContend()
	{
		return _NContend;
	}

	public void set_NContend(String _NContend)
	{
		this._NContend = _NContend;
	}

	public SMPic get_PIC()
	{
		return _PIC;
	}

	public void set_PIC(SMPic _PIC)
	{

		this._PIC = _PIC;
	}

	public void set_PIC(String jsonarray)
	{

		this.set_PIC((SMPic) Common.set_Objet(jsonarray, SMPic.class, ConstUtiles.STUDYMETHODDETAIL_PIC_COLUMNS));
	}

	@Override
	public String toString()
	{
		return "StudyMethodDetail [_success=" + _success + ", _Baseurl=" + _Baseurl + ", _NID=" + _NID + ", _NT=" + _NT + ", _NContend=" + _NContend + ", _PIC=" + _PIC + "]";
	}

}
