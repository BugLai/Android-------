package com.tqxktbyw.study.entity;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;

public class SMPic implements Serializable{
	/** 
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	 */ 
	private static final long serialVersionUID = 3L;
	private int _state;
	private List<String> _url;
	public List<String> get_url() {
		return _url;
	}
	public  void set_url(List<String> _url) {
		
		this._url = _url;
	}
	public void set_url(JSONArray jsonarray) {
		
		this.set_url(Common.set_grade(jsonarray, ConstUtiles.IMG));
	}
	public void set_url(String jsonarray) {
		try {
			if(jsonarray.startsWith("[")){
				JSONArray jary =new JSONArray(jsonarray);
				this.set_url(jary);
			}else
				this._url = null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this._url = null;
		}
	}
	public int get_state() {
		return _state;
	}
	public void set_state(int _state) {
		this._state = _state;
	}
	public void set_state(String _state) {
		this._state = Common.String2Int(_state);
	}

}
