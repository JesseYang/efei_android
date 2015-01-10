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
	 * �����ӽ�������Ҫ��һ����¼����get@#{short_url}</br>
	 * <b>������</b>
	 * <li>short_url�����������ݣ�Ϊɨ����ǰ����ϡ�~��������ɨ����ΪQZRJs����ô����Ķ�����Ϊ~QZRJs</br>
	 * <b>����ֵ��</b>
	 * <li>code��QUESTION_NOT_EXIST
	 * <li>question_id��������������id
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
	 * ��ȡ��Ŀ���ݣ���Ҫ��һ����¼����get@student/questions/#{question_id} �� student/questions_controller#show</br>
	 * <b>������</b>
	 * <li>question_id��url������Ŀid</br>
	 * <b>����ֵ��</b>
	 * <li>code��QUESTION_NOT_EXIST
	 * <li>note_id�����û��Ѿ�����˸����������Ȿʱ������note_id
	 * <li>question�����û�δ��Ӹ����������Ȿʱ�������������ݣ�Ϊ��ϣ���ṹ�����¼
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
	 * ��ӵ�����Ŀ�����Ȿ��post@student/notes �� student/notes_controller#create</br>
	 * <b>������</b>
	 * <li>question_id����Ŀid
	 * <li>tag��ѡ��ı�ǩ����
	 * <li>topics���������ӵ�֪ʶ��
	 * <li>summary���ܽ�ʼ�</br>
	 * <b>����ֵ</b>
	 * <li>code��REQUIRE_SIGNIN��QUESTION_NOT_EXIST
	 * <li>note����������Ȿ����Ŀ��Ϊ��ϣ���ṹ�����¼
	 * <li>note_update_time�����Ȿ����ʱ�䣬Ϊ��ϣ��key�Ǵ���ѧ�Ƶ������������¼����value�Ǹÿ�Ŀ�����д����������ʱ�䣬��������unixʱ���ʾ
	 * <li>teacher�������û��Ľ�ʦ�в������������Ŀ�Ľ�ʦʱ�����ؽ�ʦ��Ϣ��Ϊ��ϣ������ṹΪ
		.	id����ʦid
		.	name����ʦ����
		.	subject��������ʾ��ѧ��
		.	school��ѧУ����
		.	classes�����飬�ý�ʦ�İ༶�б�ÿ��Ԫ��Ϊһ����ϣ������ṹΪ��
			.	id���༶id
			.	name���༶����
			.	desc���༶����
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
	 * ��Ӷ����Ŀ�����Ȿ��post@student/notes/batch �� student/notes_controller#batch</br>
	 * <b>����</b>
	 * <li>question_ids�����飬ÿ��Ԫ��Ϊһ��Ҫ��ӵ���Ŀid</br>
	 * <b>����ֵ</b>
	 * <li>code��REQUIRE_SIGNIN��QUESTION_NOT_EXIST
	 * <li>note_update_time�����Ȿ����ʱ�䣬Ϊ��ϣ��key�Ǵ���ѧ�Ƶ������������¼����value�Ǹÿ�Ŀ�����д����������ʱ�䣬��������unixʱ���ʾ
	 * <li>teachers�����飬�����û��Ľ�ʦ�в������������Ŀ�Ľ�ʦʱ�����ؽ�ʦ��Ϣ��ÿһ��Ԫ��Ϊһ����ʦ����Ϣ������ṹͬ��һ���ӿ�
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
