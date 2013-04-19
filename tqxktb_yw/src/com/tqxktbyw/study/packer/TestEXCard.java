package com.tqxktbyw.study.packer;

import java.io.File;
import java.util.Date;
import java.text.DecimalFormat;
import java.io.FileInputStream;

public class TestEXCard
{

	private  boolean sdcardAvailable;
	private  boolean sdcardAvailabilityDetected;

	public boolean detectSDCardAvailability()
	{

		boolean result = false;
		try
		{
			Date now = new Date();
			long times = now.getTime();
			String fileName = "/mnt/external_sd/" + times + ".test";
			File file = new File(fileName);
			result = file.createNewFile();
			file.delete();
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			sdcardAvailabilityDetected = true;
			sdcardAvailable = result;
		}
		return result;
	}

	/**
	 * @return SD is available ?
	 */
	public boolean isSDCardAvailable()
	{

		if (!sdcardAvailabilityDetected)
		{
			sdcardAvailable = detectSDCardAvailability();
			sdcardAvailabilityDetected = true;
		}
		return sdcardAvailable;
	}

	/*** 获取文件大小 ***/
	public long getFileSizes(File f) throws Exception
	{
		long s = 0;
		if (f.exists())
		{
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else
		{
			f.createNewFile();
			System.out.println("文件不存在");
		}
		return s;
	}

	/*** 获取文件夹大小 ***/
	public long getFileSize(File f) throws Exception
	{
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++)
		{
			if (flist[i].isDirectory())
			{
				size = size + getFileSize(flist[i]);
			} else
			{
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/*** 转换文件大小单位(b/kb/mb/gb) ***/
	public String FormetFileSize(long fileS)
	{// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024)
		{
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576)
		{
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824)
		{
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else
		{
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}
/*
 * 
 */
	public long FileFolder_Free_Size(String path)  
	  
	   {  
	  
	       File pathFile = new File(path);                       // 取得sdcard文件路径    
	  
	       android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());            
	  
	       long nTotalBlocks = statfs.getBlockCount();            // 获取SDCard上BLOCK总数     
	  
	       long nBlocSize = statfs.getBlockSize();                   // 获取SDCard上每个block的SIZE      
	  
	       long nAvailaBlock = statfs.getAvailableBlocks();          // 获取可供程序使用的Block的数量    
	  
	       long nFreeBlock = statfs.getFreeBlocks();                 // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)     
	  
	       long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024;   // 计算 SDCard 剩余大小MB   
	  
	       return nSDFreeSize;  
	  
	   }  
	/*
	 * 
	 */
	public long FileFolder_All_Size(String path)  
	  
	   {  
	  
	       File pathFile = new File(path);                       // 取得sdcard文件路径    
	  
	       android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());            
	  
	       long nTotalBlocks = statfs.getBlockCount();           // 获取SDCard上BLOCK总数     
	  
	       long nBlocSize = statfs.getBlockSize();                   // 获取SDCard上每个block的SIZE      
	  
	       long nAvailaBlock = statfs.getAvailableBlocks();          // 获取可供程序使用的Block的数量    
	  
	       long nFreeBlock = statfs.getFreeBlocks();                 // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)  
	  
	       long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024;  // 计算SDCard 总容量大小MB     
	  
	       return nSDTotalSize;  
	  
	   }  
	/*** 获取文件个数 ***/
	public long getlist(File f)
	{// 递归求取目录文件个数
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++)
		{
			if (flist[i].isDirectory())
			{
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;
	}

}
