package com.efei.lib.android.bean.net;

import java.util.List;

public class RespQueOrNote extends ABaseRespBean
{
	private long id;
	private int subject;
	private String type;
	private List<String> content;
	private List<String> items;
	// answer
	private List<String> answer_content;
	private String tag_set;
	private String summary;
	private String topics;
	private String tag;

	public long getId()
	{
		return id;
	}

	void setId(long id)
	{
		this.id = id;
	}

	public int getSubject()
	{
		return subject;
	}

	void setSubject(int subject)
	{
		this.subject = subject;
	}

	public String getType()
	{
		return type;
	}

	void setType(String type)
	{
		this.type = type;
	}

	public List<String> getContent()
	{
		return content;
	}

	void setContent(List<String> content)
	{
		this.content = content;
	}

	public List<String> getItems()
	{
		return items;
	}

	void setItems(List<String> items)
	{
		this.items = items;
	}

	public List<String> getAnswer_content()
	{
		return answer_content;
	}

	void setAnswer_content(List<String> answer_content)
	{
		this.answer_content = answer_content;
	}

	public String getTag_set()
	{
		return tag_set;
	}

	void setTag_set(String tag_set)
	{
		this.tag_set = tag_set;
	}

	public String getSummary()
	{
		return summary;
	}

	void setSummary(String summary)
	{
		this.summary = summary;
	}

	public String getTopics()
	{
		return topics;
	}

	void setTopics(String topics)
	{
		this.topics = topics;
	}

	public String getTag()
	{
		return tag;
	}

	void setTag(String tag)
	{
		this.tag = tag;
	}

}
