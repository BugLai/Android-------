package com.tqxktbyw.study.packer;  
  
public class FileInfo {  
     String File_Name;  
     String File_Path;  
     int File_Status;
     public FileInfo()
 	{
 		// TODO Auto-generated constructor stub
 	}
     public FileInfo(String filename,String filepath,int filestatus,int id){
    	 this.File_Name = filename;
    	 this.File_Path = filepath;
    	 this.File_Status = filestatus;
     }
	
	public String getFile_Name()
	{
		return File_Name;
	}
	public void setFile_Name(String file_Name)
	{
		File_Name = file_Name;
	}
	public String getFile_Path()
	{
		return File_Path;
	}
	public void setFile_Path(String file_Path)
	{
		File_Path = file_Path;
	}
	public int getFile_Status()
	{
		return File_Status;
	}
	public void setFile_Status(int file_Status)
	{
		File_Status = file_Status;
	}
	@Override
	public String toString()
	{
		return "FileInfo [File_Name=" + File_Name + ", File_Path=" + File_Path + ", File_Status=" + File_Status + "]";
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((File_Name == null) ? 0 : File_Name.hashCode());
		result = prime * result + ((File_Path == null) ? 0 : File_Path.hashCode());
		result = prime * result + File_Status;
		return result;
	}
	//注意这里重写了equals方法  
    @Override  
    public boolean equals(Object obj){  
        if(obj == null){  
            return false;  
        }else {           
                if(this.getClass() == obj.getClass()){  
                    FileInfo u = (FileInfo) obj;  
                    if(this.getFile_Path().equals(u.getFile_Path())&&this.getFile_Name().equals(u.getFile_Name())){  
                        return true;  
                    }else{  
                        return false;  
                    }  
                  
            }else{  
                return false;  
            }  
        }             
    } 
 
      
}  