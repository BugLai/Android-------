package com.tqxktbyw.videoplayer;

public class AesVideoDec {

	public native String getkey();

	public native String getIv();
	public native int depack(String despath, String srcpath);
	public native byte[] getData(String PATH);

	public native void setData(String SPATH, String DPATH, byte[] DATA);

	static {
		System.loadLibrary("iaes");
	}
}
