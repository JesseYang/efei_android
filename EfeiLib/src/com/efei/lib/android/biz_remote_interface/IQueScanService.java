package com.efei.lib.android.biz_remote_interface;

import java.util.List;
import java.util.Map;

import com.efei.lib.android.bean.net.ABaseReqBean;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.RespQueOrNote;
import com.efei.lib.android.bean.net.common_data.Teacher;

public interface IQueScanService
{
	public static class Factory
	{
		private Factory()
		{
		}

		private static IQueScanService service;

		public synchronized static IQueScanService getService()
		{
			if (null == service)
				service = new QueScanImpl();
			return service;
		}
	}
	
	/**
	 * 短链接解析（不要求一定登录）：get@#{short_url}</br>
	 * <b>参数：</b>
	 * <li>short_url：短链接内容，为扫码结果前面加上“~”，例如扫码结果为QZRJs，那么这里的短链接为~QZRJs</br>
	 * <b>返回值：</b>
	 * <li>code：QUESTION_NOT_EXIST
	 * <li>question_id：解析出的问题id
	 */
	RespQueId get(String short_url);
	public static class RespQueId extends BaseRespBean
	{
		private String question_id;

		public String getQuestion_id()
		{
			return question_id;
		}

		public void setQuestion_id(String question_id)
		{
			this.question_id = question_id;
		}
	}
	
	/**
	 * 获取题目内容（不要求一定登录）：get@student/questions/#{question_id} → student/questions_controller#show</br>
	 * <b>参数：</b>
	 * <li>question_id（url）：题目id</br>
	 * <b>返回值：</b>
	 * <li>code：QUESTION_NOT_EXIST
	 * <li>note_id：当用户已经添加了该问题至错题本时，返回note_id
	 * <li>question：当用户未添加该问题至错题本时，返回问题内容，为哈希，结构详见附录
	 */
	BaseRespBean get0student$questions(String question_id);
	public static class RespNoteId extends BaseRespBean
	{
		private String note_id;
		public String getNote_id()
		{
			return note_id;
		}
		public void setNote_id(String note_id)
		{
			this.note_id = note_id;
		}
	}
	
	/**
	 * 添加单道题目至错题本：post@student/notes → student/notes_controller#create</br>
	 * <b>参数：</b>
	 * <li>question_id：题目id
	 * <li>tag：选择的标签内容
	 * <li>topics：逗号连接的知识点
	 * <li>summary：总结笔记</br>
	 * <b>返回值</b>
	 * <li>code：REQUIRE_SIGNIN，QUESTION_NOT_EXIST
	 * <li>note：添加至错题本的题目，为哈希，结构详见附录
	 * <li>note_update_time：错题本更新时间，为哈希，key是代表学科的整数（详见附录），value是该科目下所有错题的最后更新时间，用整数的unix时间表示
	 * <li>teacher：当该用户的教师中不含有所添加题目的教师时，返回教师信息，为哈希，具体结构为
		.	id：教师id
		.	name：教师姓名
		.	subject：整数表示的学科
		.	school：学校名称
		.	classes：数组，该教师的班级列表，每个元素为一个哈希，具体结构为：
			.	id：班级id
			.	name：班级名称
			.	desc：班级描述
	 */
	RespAddSingleQue post0student$note(String question_id, String tag , String topics , String summary);
	public static class ReqAddSingleQue extends ABaseReqBean
	{
		private String question_id;
		private String tag;
		private String topics;
		private String summary;
		public String getQuestion_id()
		{
			return question_id;
		}
		public void setQuestion_id(String question_id)
		{
			this.question_id = question_id;
		}
		public String getTag()
		{
			return tag;
		}
		public void setTag(String tag)
		{
			this.tag = tag;
		}
		public String getTopics()
		{
			return topics;
		}
		public void setTopics(String topics)
		{
			this.topics = topics;
		}
		public String getSummary()
		{
			return summary;
		}
		public void setSummary(String summary)
		{
			this.summary = summary;
		}
		
	}
	public static class RespAddSingleQue extends BaseRespBean
	{
		private RespQueOrNote note;
		private Teacher teacher;
		private Map<String, Long> note_update_time;
		public Teacher getTeacher()
		{
			return teacher;
		}
		public void setTeacher(Teacher teacher)
		{
			this.teacher = teacher;
		}
		public Map<String, Long> getNote_update_time()
		{
			return note_update_time;
		}
		public void setNote_update_time(Map<String, Long> note_update_time)
		{
			this.note_update_time = note_update_time;
		}
		public RespQueOrNote getNote()
		{
			return note;
		}
		public void setNote(RespQueOrNote note)
		{
			this.note = note;
		}
	}
	
	/**
	 * 添加多道题目至错题本：post@student/notes/batch → student/notes_controller#batch</br>
	 * <b>参数</b>
	 * <li>question_ids：数组，每个元素为一个要添加的题目id</br>
	 * <b>返回值</b>
	 * <li>code：REQUIRE_SIGNIN，QUESTION_NOT_EXIST
	 * <li>note_update_time：错题本更新时间，为哈希，key是代表学科的整数（详见附录），value是该科目下所有错题的最后更新时间，用整数的unix时间表示
	 * <li>teachers：数组，当该用户的教师中不含有所添加题目的教师时，返回教师信息，每一个元素为一个教师的信息，具体结构同上一个接口
	 */
	RespAddBatchQues post0student$notes$batch(String... question_ids);
	public static class ReqAddBatchQues extends ABaseReqBean
	{
		private String[] question_ids;

		public String[] getQuestion_ids()
		{
			return question_ids;
		}

		public void setQuestion_ids(String[] question_ids)
		{
			this.question_ids = question_ids;
		}
	}
	public static class RespAddBatchQues extends BaseRespBean
	{
		private Map<String, Long> note_update_time;
		private List<Teacher> teachers;
		public Map<String, Long> getNote_update_time()
		{
			return note_update_time;
		}
		public void setNote_update_time(Map<String, Long> note_update_time)
		{
			this.note_update_time = note_update_time;
		}
		public List<Teacher> getTeachers()
		{
			return teachers;
		}
		public void setTeachers(List<Teacher> teachers)
		{
			this.teachers = teachers;
		}
	}
}
