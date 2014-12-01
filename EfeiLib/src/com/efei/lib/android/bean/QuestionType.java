package com.efei.lib.android.bean;

import com.efei.lib.android.exception.EfeiException;

public enum QuestionType
{
	CHOICE("choice"), ANALYSIS("analysis");
	private String typeName;

	private QuestionType(String typeName)
	{
		this.typeName = typeName;
	}

	@Override
	public String toString()
	{
		return typeName;
	}

	public static QuestionType getQuestionTypeByName(String typeName)
	{
		QuestionType[] values = QuestionType.values();
		for (QuestionType type : values)
			if (type.typeName.equals(typeName))
				return type;
		throw new EfeiException("can't find question_type with name:" + typeName);
	}
}
