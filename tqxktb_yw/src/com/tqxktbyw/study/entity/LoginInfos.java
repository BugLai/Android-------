package com.tqxktbyw.study.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class LoginInfos extends MyBase{
	
	private List<VerInfo> _ver;
	private List<UserLogInfo> _user;
	private List<AddressInfo> _addressV;
	private List<AddressInfo> _addressZ;
	private List<LoginXmlFileInfo> _xmlFile;
	public VerInfo get_Version(){
		return _ver.get(0);
	}
	public UserLogInfo get_User(){
		return _user.get(0);
	}
	public List<VerInfo> get_ver() {
		return _ver;
	}
	public  void set_ver(List<Object> _ver) {
		List<VerInfo> sigs = new ArrayList<VerInfo>();
		for(Object o:_ver){
			sigs.add((VerInfo)o);
		}
		this._ver = sigs;
	}
	public void set_ver(JSONArray jsonarray) {
		
		this.set_ver(Common.set_grade(jsonarray, VerInfo.class, ConstUtiles.VER_COLUMNS));
	}
	public void set_ver(String jsonarray) {
		try {
			JSONArray jary =new JSONArray(jsonarray);
			this.set_ver(jary);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this._ver = null;
		}
	}
	
	public List<UserLogInfo> get_user() {
		return _user;
	}
	public  void set_user(List<Object> _user) {
		List<UserLogInfo> sigs = new ArrayList<UserLogInfo>();
		for(Object o:_user){
			sigs.add((UserLogInfo)o);
		}
		this._user = sigs;
	}
	public void set_user(JSONArray jsonarray) {
		
		this.set_user(Common.set_grade(jsonarray, UserLogInfo.class, ConstUtiles.USER_COLUMNS));
	}
	public void set_user(String jsonarray) {
		try {
			JSONArray jary =new JSONArray(jsonarray);
			this.set_user(jary);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this._user = null;
		}
	}
	public List<AddressInfo> get_addressV() {
		return _addressV;
	}
	public  void set_addressV(List<Object> _addressV) {
		List<AddressInfo> sigs = new ArrayList<AddressInfo>();
		for(Object o:_addressV){
			sigs.add((AddressInfo)o);
		}
		this._addressV = sigs;
	}
	public void set_addressV(JSONArray jsonarray) {
		
		this.set_addressV(Common.set_grade(jsonarray, AddressInfo.class, ConstUtiles.ADDR_COLUMNS));
	}
	public void set_addressV(String jsonarray) {
		try {
			JSONArray jary =new JSONArray(jsonarray);
			this.set_addressV(jary);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this._addressV = null;
		}
	}
	public List<AddressInfo> get_addressZ() {
		return _addressZ;
	}

	public  void set_addressZ(List<Object> _addressZ) {
		List<AddressInfo> sigs = new ArrayList<AddressInfo>();
		for(Object o:_addressZ){
			sigs.add((AddressInfo)o);
		}
		this._addressZ = sigs;
	}
	public void set_addressZ(JSONArray jsonarray) {
		
		this.set_addressZ(Common.set_grade(jsonarray, AddressInfo.class, ConstUtiles.ADDR_COLUMNS));
	}
	public void set_addressZ(String jsonarray) {
		try {
			JSONArray jary =new JSONArray(jsonarray);
			this.set_addressZ(jary);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this._addressZ = null;
		}
	}
	public List<LoginXmlFileInfo> get_xmlFile() {
		return _xmlFile;
	}

	public LoginXmlFileInfo get_FirstxmlFile() {
		return _xmlFile.get(0);
	}

	public  void set_xmlFile(List<Object> _xmlFile) {
		List<LoginXmlFileInfo> sigs = new ArrayList<LoginXmlFileInfo>();
		for(Object o:_xmlFile){
			sigs.add((LoginXmlFileInfo)o);
		}
		this._xmlFile = sigs;
	}
	public void set_xmlFile(JSONArray jsonarray) {
		
		this.set_xmlFile(Common.set_grade(jsonarray, LoginXmlFileInfo.class, ConstUtiles.XML_COLUMNS));
	}
	public void set_xmlFile(String jsonarray) {
		try {
			JSONArray jary =new JSONArray(jsonarray);
			this.set_xmlFile(jary);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this._xmlFile = null;
		}
	}
	private String _checkURL;
	public String get_checkURL() {
		return _checkURL;
	}
	public void set_checkURL(String _checkURL) {
		this._checkURL = _checkURL;
	}

}
