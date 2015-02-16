package com.efei.lib.android.bean.persistance;

import android.text.Spannable;

import com.efei.lib.android.bean.QuestionType;
import com.efei.lib.android.bean.Subject;
import com.efei.lib.android.bean.net.RespQueOrNote;
import com.efei.lib.android.utils.TextUtils;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

//TODO
@DatabaseTable(tableName = "T_QUESTION_OR_NOTE")
public class QuestionOrNote
{
	public static final class Properties
	{
		private Properties()
		{
		};

		public static final String Id = "id";
		public static final String SubjectIndex = "subject_index";
		public static final String Type = "type";
		public static final String Content = "content";
		public static final String Item = "item";
		public static final String AnswerContent = "answer_content";
		public static final String TagSet = "tag_set";
		public static final String Summary = "summary";
		public static final String Topics = "topics";
		public static final String Tag = "tag";
		public static final String AccountId = "account_id";
	}

	@DatabaseField(id = true, columnName = Properties.Id)
	private String id;
	@DatabaseField(columnName = Properties.SubjectIndex)
	private int subjectIndex;
	@DatabaseField(columnName = Properties.Type)
	private String typeName;
	@DatabaseField(columnName = Properties.Content)
	private String content;
	@DatabaseField(columnName = Properties.Item)
	private String items;
	// answer
	@DatabaseField(columnName = Properties.AnswerContent)
	private String answer_content;
	@DatabaseField(columnName = Properties.TagSet)
	private String tag_set;
	@DatabaseField(columnName = Properties.Summary)
	private String summary;
	@DatabaseField(columnName = Properties.Topics)
	private String topics;
	@DatabaseField(columnName = Properties.Tag)
	private String tag;
	@DatabaseField(canBeNull = false, foreign = true, columnName = Properties.AccountId)
	private Account account;

	QuestionOrNote()
	{
	}

	public QuestionOrNote(RespQueOrNote resp, Account account)
	{
		setId(resp.get_id());
		setSubjectIndex(resp.getSubject());
		setType(resp.getType());
		setContent(TextUtils.concat(resp.getContent()));
		setItems(TextUtils.concat(resp.getItems()));
		// answer
		setAnswerContent(TextUtils.concat(resp.getAnswer_content()));
		setTagSet(resp.getTag_set());
		setSummary(resp.getSummary());
		setTopics(resp.getTopic_str());
		setTag(resp.getTag());
		setAccount(account);
	}

	public String getId()
	{
		return id;
	}

	void setId(String id)
	{
		this.id = id;
	}

	public String getType()
	{
		return typeName;
	}

	void setType(String type)
	{
		this.typeName = type;
	}

	public String getContent()
	{
		return content;
	}

	void setContent(String content)
	{
		this.content = content;
	}

	public String getItems()
	{
		return items;
	}

	void setItems(String items)
	{
		this.items = items;
	}

	public String getAnswer_content()
	{
		return answer_content;
	}

	void setAnswerContent(String answer_content)
	{
		this.answer_content = answer_content;
	}

	public String getTag_set()
	{
		return tag_set;
	}

	void setTagSet(String tag_set)
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

	String getTag()
	{
		return tag;
	}

	void setTag(String tag)
	{
		this.tag = tag;
	}

	int getSubjectIndex()
	{
		return subjectIndex;
	}

	void setSubjectIndex(int subjectIndex)
	{
		this.subjectIndex = subjectIndex;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public Account getAccount()
	{
		return account;
	}

	// ___________________________ transiant___________________________
	private Subject subject;
	private QuestionType type;
	private Spannable spContent;

	public Subject getSubject()
	{
		if (null == subject)
			subject = Subject.getSubjectByIndex(subjectIndex);
		return subject;
	}

	public QuestionType getQuestionType()
	{
		if (null == type)
			type = QuestionType.getQuestionTypeByName(typeName);
		return type;
	}

	public Spannable getFormattedContent()
	{
		return spContent;
	}

	public void setFormattedContent(Spannable spContent)
	{
		this.spContent = spContent;
	}

}
