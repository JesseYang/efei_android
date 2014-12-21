package com.efei.lib.android.bean.net;

import java.util.List;

public class ReqNotesBatch extends ABaseReqBean
{
	private List<String> question_ids;

	public List<String> getQuestion_ids()
	{
		return question_ids;
	}

	public void setQuestion_ids(List<String> question_ids)
	{
		this.question_ids = question_ids;
	}
}
