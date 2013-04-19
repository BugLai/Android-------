package com.tqxktbyw.study.packer;

/**
 * @author Administrator 包中的xml信息
 */
public class Filexml
{
	int file_id;
	int teacher_id;
	int teacher_ver;
	String teacher_url;
	String file_filename;
	String file_url;
	String file_zip;
	String file_teacher;

	public int getTeacher_id()
	{
		return teacher_id;
	}

	public void setTeacher_id(int teacher_id)
	{
		this.teacher_id = teacher_id;
	}

	public int getTeacher_ver()
	{
		return teacher_ver;
	}

	public void setTeacher_ver(int teacher_ver)
	{
		this.teacher_ver = teacher_ver;
	}

	public String getTeacher_url()
	{
		return teacher_url;
	}

	public void setTeacher_url(String teacher_url)
	{
		this.teacher_url = teacher_url;
	}

	public int getFile_id()
	{
		return file_id;
	}

	public void setFile_id(int file_id)
	{
		this.file_id = file_id;
	}

	public String getFile_filename()
	{
		return file_filename;
	}

	public void setFile_filename(String file_filename)
	{
		this.file_filename = file_filename;
	}

	public String getFile_url()
	{
		return file_url;
	}

	public void setFile_url(String file_url)
	{
		this.file_url = file_url;
	}

	public String getFile_zip()
	{
		return file_zip;
	}

	public void setFile_zip(String file_zip)
	{
		this.file_zip = file_zip;
	}

	public String getFile_teacher()
	{
		return file_teacher;
	}

	public void setFile_teacher(String file_teacher)
	{
		this.file_teacher = file_teacher;
	}

	@Override
	public String toString()
	{
		return "Filexml [file_id=" + file_id + ", teacher_id=" + teacher_id + ", teacher_ver=" + teacher_ver + ", teacher_url=" + teacher_url + ", file_filename=" + file_filename + ", file_url=" + file_url + ", file_zip=" + file_zip + ", file_teacher=" + file_teacher + "]";
	}

}
