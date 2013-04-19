package com.tqxktbyw.study.entity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Common {
	public static int String2Int(String ss){
		if(ss ==null || ss.equals("")){
			return 0;
		}else
		return Integer.parseInt(ss);
	}
	public static boolean String2Boolean(String ss){
		if(ss.equals("1")){
			return true;
		}else
		return false;
	}
public static List<String> set_grade(JSONArray jsonarray,String GRADE_COLUMNS) {
		
		List<String> sigs = new ArrayList<String>();
		for(int j =0;j<jsonarray.length();j++){
			JSONObject jop;
			try {
				jop = jsonarray.getJSONObject(j);
					if(!jop.isNull(GRADE_COLUMNS)){

						sigs.add(jop.getString(GRADE_COLUMNS));
					}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
		}
		return sigs;
	}
	public static List<Object> set_grade(JSONArray jsonarray,Class<?> classtype,String[] GRADE_COLUMNS) {
		
		List<Object> sigs = new ArrayList<Object>();
		for(int j =0;j<jsonarray.length();j++){
			JSONObject jop;
			try {
				Object nl = classtype.newInstance();
				jop = jsonarray.getJSONObject(j);
				for(int i=0;i<GRADE_COLUMNS.length;i++){
					String columnName = GRADE_COLUMNS[i];
					if(!jop.isNull(columnName)){
						//nl.put(columnName, jop.getString(columnName));
						classtype.getMethod("set_" + columnName, String.class).invoke(nl, jop.getString(columnName));
						
					}
				}
				sigs.add(nl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
		}
		return sigs;
	}

	public static Object set_Objet(String jsonarray,Class<?> classtype,String[] GRADE_COLUMNS) {
		try {
			JSONObject jop = new JSONObject(jsonarray);
				Object nl = classtype.newInstance();
				for(int i=0;i<GRADE_COLUMNS.length;i++){
					String columnName = GRADE_COLUMNS[i];
					if(!jop.isNull(columnName)){
						//nl.put(columnName, jop.getString(columnName));
						classtype.getMethod("set_" + columnName, String.class).invoke(nl, jop.getString(columnName));
						
					}
				}
				return nl;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	public static List<Object> set_grade(String jsonarray,Class<?> classtype,String[] GRADE_COLUMNS) {
		try {
			JSONArray jary =new JSONArray(jsonarray);
			return set_grade(jary,classtype,GRADE_COLUMNS);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
