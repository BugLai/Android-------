/**
 * 
 */
package com.tqxktbyw.study.entity;

/**
 * @author jishubu_03 2012-4-6
 */
public class CItem {

	private String ID = "";
	private String Value = "";

	public CItem() {
		ID = "";
		Value = "";
	}

	public CItem(String _ID, String _Value) {
		ID = _ID;
		Value = _Value;
	}

	@Override
	public String toString() { // 为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()
		// TODO Auto-generated method stub
		return Value;
	}

	public String GetID() {
		return ID;
	}

	public String GetValue() {
		return Value;
	}

	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}

	/**
	 * @param iD
	 *            the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return Value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		Value = value;
	}
}
