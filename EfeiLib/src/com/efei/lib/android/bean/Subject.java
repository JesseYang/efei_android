package com.efei.lib.android.bean;

import com.efei.lib.android.exception.EfeiException;

public enum Subject
{
	//@formatter:off
	CHINESE(1, "����"), MATH(2, "��ѧ"), ENGLISH(4, "Ӣ��"), PHYSICS(8, "����"), CHEMISTRY(16, "��ѧ"), 
	BIOLOGY(32, "����"), HISTORY(64, "��ʷ"), GEOGRAPHY(128, "����"), politics(256, "����"), other(512, "����");
	//@formatter:on
	final private int index;
	final public String name;

	private Subject(int index, String name)
	{
		this.index = index;
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name;
	}

	public static Subject getSubjectByIndex(int index)
	{
		Subject[] values = Subject.values();
		for (Subject subject : values)
		{
			if (subject.index == index)
				return subject;
		}
		throw new EfeiException("can't find subject with index:" + index);
	}

}
