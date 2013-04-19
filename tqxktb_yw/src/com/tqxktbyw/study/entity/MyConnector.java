package com.tqxktbyw.study.entity;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class MyConnector {
	Socket socket = null;
	DataInputStream din = null;
	DataOutputStream dout = null;

	public static byte[] postFromHttpClient(String path, Map<String, String> params, String encode) throws Exception{
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for(Map.Entry<String, String> entry : params.entrySet()){
			formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, encode);
		HttpPost httppost = new HttpPost(path);
		httppost.setEntity(entity);
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response = httpclient.execute(httppost);
		return readStream(response.getEntity().getContent());
	}
	public static byte[] getByte(String path) throws Exception{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode()==HttpURLConnection.HTTP_OK && conn.getContentLength() > 0){
			InputStream inStream = conn.getInputStream();
			return readStream(inStream);
		}else{
			throw new Exception("request fail");
		}		
	}
    public static byte[] readStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while( (len=inStream.read(buffer)) != -1){
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}
    public MyConnector(){

	}
	/*
	public MyConnector(String address,int port){
		try{
			socket = new Socket(address,port);
			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//
	public void sayBye(){
		try{
			dout.writeUTF("<#USER_LOGOUT#>");
			din.close();
			dout.close();
			socket.close();
			socket=null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
*/
}
