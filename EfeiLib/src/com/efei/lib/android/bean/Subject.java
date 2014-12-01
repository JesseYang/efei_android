package com.efei.lib.android.bean;

import com.efei.lib.android.exception.EfeiException;

public enum Subject
{
	//@formatter:off
	CHINESE(1, "语文"), MATH(2, "数学"), ENGLISH(4, "英语"), PHYSICS(8, "物理"), CHEMISTRY(16, "化学"), 
	BIOLOGY(32, "生物"), HISTORY(64, "历史"), GEOGRAPHY(128, "地理"), politics(256, "政治"), other(512, "其他");
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
