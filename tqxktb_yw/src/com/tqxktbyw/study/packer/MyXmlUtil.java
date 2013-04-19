package com.tqxktbyw.study.packer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;

public class MyXmlUtil
{

	private final String fileName = "myPullXML.xml";
	private Context mContext;
	private List<FileInfo> personsList = new ArrayList<FileInfo>(); // 保存xml的person节点
	private List<Filexml> filesList = new ArrayList<Filexml>(); // 保存xml的person节点

	public MyXmlUtil(Context context)
	{
		this.mContext = context;

	}

	/** Pull方式，创建 XML */
	public String pullXMLCreate(List<FileInfo> fileInfos)
	{

		StringWriter xmlWriter = new StringWriter();
		personsList = fileInfos;
		try
		{
			// // 方式一：使用Android提供的实用工具类android.util.Xml
			// XmlSerializer xmlSerializer = Xml.newSerializer();

			// 方式二：使用工厂类XmlPullParserFactory的方式
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlSerializer xmlSerializer = factory.newSerializer();

			xmlSerializer.setOutput(xmlWriter); // 保存创建的xml

			xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			// xmlSerializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation",
			// " "); // 设置属性
			// xmlSerializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator",
			// "\n");
			xmlSerializer.startDocument("utf-8", null); // <?xml version='1.0'
														// encoding='UTF-8'
														// standalone='yes' ?>

			xmlSerializer.startTag("", "root");
			xmlSerializer.attribute("", "author", "CuiWang");
			xmlSerializer.attribute("", "date", "2013-02-25");

			int personsLen = personsList.size();
			System.out.println("personsLen = " + personsLen);
			for (int i = 0; i < personsLen; i++)
			{
				xmlSerializer.startTag("", "fileinfo"); // 创建person节点

				xmlSerializer.startTag("", "name");
				xmlSerializer.text(personsList.get(i).getFile_Name());
				xmlSerializer.endTag("", "name");

				xmlSerializer.startTag("", "path");
				xmlSerializer.text(personsList.get(i).getFile_Path());
				xmlSerializer.endTag("", "path");

				xmlSerializer.startTag("", "status");
				xmlSerializer.text(personsList.get(i).getFile_Status() + "");
				xmlSerializer.endTag("", "status");

				xmlSerializer.endTag("", "fileinfo");
			}

			xmlSerializer.endTag("", "root");
			xmlSerializer.endDocument();

		} catch (XmlPullParserException e)
		{ // XmlPullParserFactory.newInstance
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{ // xmlSerializer.setOutput
			e.printStackTrace();
		} catch (IllegalStateException e)
		{ // xmlSerializer.setOutput
			e.printStackTrace();
		} catch (IOException e)
		{ // xmlSerializer.setOutput
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		savedXML(fileName, xmlWriter.toString());
		return xmlWriter.toString();
	}

	/** Pull方式，解析 XML */
	public List<FileInfo> pullXMLResolve()
	{

		StringWriter xmlWriter = new StringWriter();

		InputStream is = readXML(fileName,0);
		try
		{
			// // 方式一：使用Android提供的实用工具类android.util.Xml
			// XmlPullParser xpp = Xml.newPullParser();

			// 方式二：使用工厂类XmlPullParserFactory的方式
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xpp = factory.newPullParser();

			xpp.setInput(is, "utf-8");

			FileInfo fInfo = null;
			StringBuffer xmlHeader = null; // 保存xml头部
			String ele = null; // Element flag

			int eventType = xpp.getEventType();
			while (XmlPullParser.END_DOCUMENT != eventType)
			{
				switch (eventType)
				{
				case XmlPullParser.START_DOCUMENT:

					xmlHeader = new StringBuffer(); // 初始化xmlHeader
					break;
				case XmlPullParser.START_TAG:
					if ("root".equals(xpp.getName()))
					{
						String attrAuthor = xpp.getAttributeName(0);
						String attrDate = xpp.getAttributeName(1);
						xmlHeader.append("root").append("\t\t");
						xmlHeader.append(attrAuthor).append("\t");
						xmlHeader.append(attrDate).append("\n");
					} else if ("fileinfo".equals(xpp.getName()))
					{
						fInfo = new FileInfo(); // 创建person实例
					} else if ("id".equals(xpp.getName()))
					{
						ele = "id";
					} else if ("name".equals(xpp.getName()))
					{
						ele = "name";
					} else if ("path".equals(xpp.getName()))
					{
						ele = "path";
					} else if ("status".equals(xpp.getName()))
					{
						ele = "status";
					} else
					{
						ele = null;
					}
					break;

				case XmlPullParser.TEXT:
					if (null != ele)
					{
						 if ("name".equals(ele))
						{
							fInfo.setFile_Name(xpp.getText());
						} else if ("path".equals(ele))
						{
							fInfo.setFile_Path(xpp.getText());
						} else if ("status".equals(ele))
						{
							fInfo.setFile_Status(Integer.parseInt(xpp.getText()));
						}

					}
					break;

				case XmlPullParser.END_TAG:
					if ("fileinfo".equals(xpp.getName()))
					{
						personsList.add(fInfo);
						fInfo = null;
					}
					ele = null;
					break;
				}

				eventType = xpp.next(); // 下一个事件类型
			}

			xmlWriter.append(xmlHeader);
			int personsLen = personsList.size();
			for (int i = 0; i < personsLen; i++)
			{
				xmlWriter.append(personsList.get(i).toString());
			}

		} catch (XmlPullParserException e)
		{ // XmlPullParserFactory.newInstance
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return personsList;
	}
	
/*
 * 解析c++生成的xml
 */
	/** Pull方式，解析 XML */
	public List<Filexml> pullXMLFromC(String xmlname)
	{
		StringWriter xmlWriter = new StringWriter();

		InputStream is = readXML(xmlname,1);
		try
		{
			// // 方式一：使用Android提供的实用工具类android.util.Xml
			// XmlPullParser xpp = Xml.newPullParser();

			// 方式二：使用工厂类XmlPullParserFactory的方式
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xpp = factory.newPullParser();

			xpp.setInput(is, "GB2312");

			Filexml filexml = null;
			StringBuffer xmlHeader = null; // 保存xml头部
			String ele = null; // Element flag

			int eventType = xpp.getEventType();
			while (XmlPullParser.END_DOCUMENT != eventType)
			{
				switch (eventType)
				{
				case XmlPullParser.START_DOCUMENT:

					xmlHeader = new StringBuffer(); // 初始化xmlHeader
					break;
				case XmlPullParser.START_TAG:
					if ("root".equals(xpp.getName()))
					{
						String attrAuthor = xpp.getAttributeName(0);
						String attrDate = xpp.getAttributeName(1);
						xmlHeader.append("root").append("\t\t");
						xmlHeader.append(attrAuthor).append("\t");
						xmlHeader.append(attrDate).append("\n");
					} else if ("fileinfo".equals(xpp.getName()))
					{
						filexml = new Filexml(); // 创建person实例
					} else if ("file_id".equals(xpp.getName()))
					{
						ele = "file_id";
					} else if ("file_filename".equals(xpp.getName()))
					{
						ele = "file_filename";
					} else if ("file_url".equals(xpp.getName()))
					{
						ele = "file_url";
					}else if ("file_zip".equals(xpp.getName()))
					{
						ele = "file_zip";
					} else if ("teacher_id".equals(xpp.getName())) {
						ele = "teacher_id";
					}
					else if ("teacher_ver".equals(xpp.getName()))
					{
						ele = "teacher_ver";
					} else if ("teacher_vurl".equals(xpp.getName()))
					{
						ele = "teacher_vurl";
					} 
					else
					{
						ele = null;
					}
					break;

				case XmlPullParser.TEXT:
					if (null != ele)
					{
						 if ("file_id".equals(ele))
						{
							 filexml.setFile_id(Integer.parseInt(xpp.getText()));  
						} else if ("file_filename".equals(ele))
						{
							filexml.setFile_filename(xpp.getText());
						} else if ("file_url".equals(ele))
						{
							filexml.setFile_url(xpp.getText());
						} 
						else if ("file_zip".equals(ele))
						{
							filexml.setFile_zip(xpp.getText());
						} 
						else if ("teacher_id".equals(ele))
						{
							filexml.setTeacher_id(Integer.parseInt(xpp.getText()));
						}
						else if ("teacher_ver".equals(ele))
						{
							filexml.setTeacher_ver(Integer.parseInt(xpp.getText()));
						}
						else if ("teacher_vurl".equals(ele))
						{
							filexml.setTeacher_url(xpp.getText());
						} 

					}
					break;

				case XmlPullParser.END_TAG:
					if ("fileinfo".equals(xpp.getName()))
					{
						filesList.add(filexml);
						filexml = null;
					}
					ele = null;
					break;
				}

				eventType = xpp.next(); // 下一个事件类型
			}

			xmlWriter.append(xmlHeader);
			int personsLen = filesList.size();
			for (int i = 0; i < personsLen; i++)
			{
				xmlWriter.append(filesList.get(i).toString());
			}

		} catch (XmlPullParserException e)
		{ // XmlPullParserFactory.newInstance
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return filesList;
	}
	private void savedXML(String fileName, String xml)
	{
		FileOutputStream fos = null;

		try
		{
			fos = mContext.openFileOutput(fileName, mContext.MODE_PRIVATE);
			byte[] buffer = xml.getBytes();
			fos.write(buffer);
			fos.close();
		} catch (FileNotFoundException e)
		{ // mContext.openFileOutput
			e.printStackTrace();
		} catch (IOException e)
		{ // fos.write
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private InputStream readXML(String fileName,int position)
	{
		FileInputStream fin = null;
		String fileNameString = fileName;
		int Position = position;
		if (Position ==1)
		{
			File file = new File(fileName);
			try
			{
				fin = new FileInputStream(file);
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				System.out.println("C++ 打开sd卡中的xml失败");
				e.printStackTrace();
			}
		}
		else {
			
		
		try
		{
			fin = mContext.openFileInput(fileNameString);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		}
		return fin;
	}
}
