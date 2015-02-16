package com.efei.lib.android.bean.net;

import java.util.List;

public class RespQueOrNote extends BaseRespBean
{
	private String _id;
	private int subject;
	private String type;
	private List<String> content;
	private List<String> items;
	// answer
	private List<String> answer_content;
	private String tag_set;
	private String summary;
	private String topic_str;
	private String tag;
	private long last_update_time;

	void setLast_update_time(long last_update_time)
	{
		this.last_update_time = last_update_time;
	}

	public long getLast_update_time()
	{
		return last_update_time;
	}

	public String get_id()
	{
		return _id;
	}

	void set_id(String _id)
	{
		this._id = _id;
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

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public String getTopic_str()
	{
		return topic_str;
	}

	public void setTopic_str(String topic_str)
	{
		this.topic_str = topic_str;
	}

	public String getTag()
	{
		return tag;
	}

	public void setTag(String tag)
	{
		this.tag = tag;
	}

}
