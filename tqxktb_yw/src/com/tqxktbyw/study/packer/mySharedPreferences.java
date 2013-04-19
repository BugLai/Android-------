package com.tqxktbyw.study.packer;

import java.util.ArrayList;

import android.content.SharedPreferences;


public class mySharedPreferences
{
	private static final String PREFERENCES_NAME = "fileInfo";//xml表名
	private SharedPreferences mSharedPreferences;
	private String[] save_ListInFo_name;//保存list信息进SharedPreferences
	private String[] save_ListInFo_path;//保存list信息进SharedPreferences
	private String[] save_ListInFo_result;//保存list信息进SharedPreferences
	private String[] load_ListInFo_Name;//保存list信息进SharedPreferences
	private String[] load_ListInFo_Path;//保存list信息进SharedPreferences
	private String[] load_ListInFo_Result;//保存list信息进SharedPreferences
	private ArrayList<String> ArrListInFo_name = new ArrayList<String>();
	private ArrayList<String> ArrListInFo_path = new ArrayList<String>(); 
	private ArrayList<String> ArrListInFo_result = new ArrayList<String>(); 
	/*
	 * / 删除数据
	 */
	/*private void delSharedPreferences( int i)
	{
		Editor editor = getSharedPreferences(PREFERENCES_NAME, 0).edit();
		editor.putInt("Size", ArrListInFo_path.size());
		editor.remove("Name" + i);
		editor.remove("Path" + i);
		editor.remove("Result" + i);
		editor.commit();
	}

	
	 * / 保存数据 Arraylist 转 Sting[] 
	 
	private void saveSharedPreferences()
	{
		save_ListInFo_name=(String[]) ArrListInFo_name.toArray(new String[ArrListInFo_path.size()]);
		save_ListInFo_path=(String[]) ArrListInFo_path.toArray(new String[ArrListInFo_path.size()]);
		save_ListInFo_result=(String[]) ArrListInFo_result.toArray(new String[ArrListInFo_path.size()]);
		
		Editor editor = getSharedPreferences(PREFERENCES_NAME, 0).edit();
		editor.putInt("Size", ArrListInFo_path.size());
		for (int i = 0; i < ArrListInFo_path.size(); i++)
		{
			editor.putString("Name" + i, save_ListInFo_name[i]);
			editor.putString("Path" + i, save_ListInFo_path[i]);
			editor.putString("Result" + i, save_ListInFo_result[i]);
		}

		editor.commit();
	}

	
	 * // 加载数据
	 

	private void loadSharedPreferences()
	{
		mSharedPreferences = getSharedPreferences(PREFERENCES_NAME, 0);
		
		ArrListInFo_name.clear();
		ArrListInFo_path.clear();
		ArrListInFo_result.clear();
		
		int Msize = mSharedPreferences.getInt("Size", 0);

		 load_ListInFo_Name = new String[Msize];
		 load_ListInFo_Path = new String[Msize];
		 load_ListInFo_Result = new String[Msize];

		for (int i = 0; i < Msize; i++)
		{
			ArrListInFo_name.add(mSharedPreferences.getString("Name" + i, null));
			ArrListInFo_path.add(mSharedPreferences.getString("Path" + i, null));
			ArrListInFo_result.add(mSharedPreferences.getString("Result" + i, null));
		}

	}*/
}
