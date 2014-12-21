package com.efei.lib.android.bean.net.common_data;

import java.util.List;

public class Teacher
{
	private String id;
	private String name;
	private int subject;
	private String school;
	private List<Classs> classes;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getSubject()
	{
		return subject;
	}

	public void setSubject(int subject)
	{
		this.subject = subject;
	}

	public String getSchool()
	{
		return school;
	}

	public void setSchool(String school)
	{
		this.school = school;
	}

	public List<Classs> getClasses()
	{
		return classes;
	}

	public void setClasses(List<Classs> classes)
	{
		this.classes = classes;
	}

	public static class Classs
	{
		private String id;
		private String name;
		private String desc;

		public String getId()
		{
			return id;
		}

		public void setId(String id)
		{
			this.id = id;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
		}

		public String getDesc()
		{
			return desc;
		}

		public void setDesc(String desc)
		{
			this.desc = desc;
		}
	}
}
