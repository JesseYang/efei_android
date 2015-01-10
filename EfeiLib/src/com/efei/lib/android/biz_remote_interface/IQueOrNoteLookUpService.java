package com.efei.lib.android.biz_remote_interface;

import java.util.List;
import java.util.Map;

import com.efei.lib.android.bean.net.ABaseReqBean;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.RespQueOrNote;

public interface IQueOrNoteLookUpService
{
	/**
	 * ��ȡ֪ʶ���б�get@student/topics �� student/topics_controller#index </br> <b>����</b>�� <li>subject����ʾ��Ŀ�������������¼ 1.2. </br> <b>����ֵ</b>�� <li>code:TODO yunzhong
	 * <li>topics�����飬֪ʶ���б�ÿ��Ԫ������һ������Ϊ2�����飬��һ��Ԫ����֪ʶ�㣬�ڶ���Ԫ����ƴ���������Զ���ȫ��ʾ
	 * 
	 * @param subject
	 * @return
	 */
	RespTopics_PinyinEntry get0student$topics(int subject);
	public static class RespTopics_PinyinEntry extends BaseRespBean
	{
		private List<Object[]> topics;
		public List<Object[]> getTopics()
		{
			return topics;
		}

		public void setTopics(List<Object[]> topics)
		{
			this.topics = topics;
		}
	}

	/**
	 * ��ȡ���Ȿ��������ʱ�䣺get@student/notes/note_update_time �� student/notes_controller#note_update_time</br> <b>����</b>����</br> <b>����ֵ</b>�� <li>code��REQUIRE_SIGNIN
	 * <li>note_update_time�����Ȿ����ʱ�䣬Ϊ��ϣ��key�Ǵ���ѧ�Ƶ������������¼����value�Ǹÿ�Ŀ�����д����������ʱ�䣬��������unixʱ���ʾ
	 * 
	 * @return
	 */
	RespNoteUpdateTime get0student$notes$note_update_time();
	public static class RespNoteUpdateTime extends BaseRespBean
	{
		private Map<String, Long> note_update_time;

		public Map<String, Long> getNote_update_time()
		{
			return note_update_time;
		}

		public void setNote_update_time(Map<String, Long> note_update_time)
		{
			this.note_update_time = note_update_time;
		}
	}

	/**
	 * ��ȡ���Ȿ����Ŀid��������ʱ���б�get@student/notes �� student/notes_controller#index</br> <b>����</b>����</br> <b>����ֵ</b>�� <li>code��REQUIRE_SIGNIN <li>
	 * notes�����飬ÿ��Ԫ������һ������Ϊ2�����飬���У���һ��Ԫ����note��id���ڶ���Ԫ���Ǹ�note��������ʱ��
	 * 
	 * @return
	 */
	RespNoteId_UpdateTimeEntry get0student$notes();
	public static class RespNoteId_UpdateTimeEntry extends BaseRespBean
	{
		private List<Object[]> notes;

		public List<Object[]> getNotes()
		{
			return notes;
		}

		public void setNotes(List<Object[]> notes)
		{
			this.notes = notes;
		}
	}

	/**
	 * ��ȡ���Ȿ����Ŀ���ݣ�get@student/notes/#{note_id} �� student/notes_controller#show</br> <b>����</b>�� <li>note_id��url������Ŀid</br> <b>����ֵ</b>�� <li>
	 * code��REQUIRE_SIGNIN��NOTE_NOT_EXIST <li>note����Ŀ���ʼ����ݣ�Ϊ��ϣ���ṹ�����¼
	 */
	RespNestNote get0student$notes(String note_id);
	public static class RespNestNote extends BaseRespBean
	{
		private RespQueOrNote note;

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
	 * ��ȡ���Ȿ�ж����Ŀ�����ݣ�get@student/notes/list �� student/notes_controller#list</br> <b>����</b>�� <li>note_githubids�����ŷָ����Ŀid</br> <b>����ֵ</b> <li>
	 * code��REQUIRE_SIGNIN <li>notes�����飬ÿһ��Ԫ����һ��note�����ݣ�Ϊ��ϣ���ṹ�����¼
	 */
	RespNestQueOrNoteArray get0student$notes$list(String... ids);
	public static class RespNestQueOrNoteArray extends BaseRespBean
	{
		private List<RespQueOrNote> notes;

		public List<RespQueOrNote> getNotes()
		{
			return notes;
		}

		public void setNotes(List<RespQueOrNote> notes)
		{
			this.notes = notes;
		}
	}

	/**
	 * ɾ�����Ȿ�е�һ����Ŀ��delete@student/notes/#{note_id} �� student/notes_controller#destroy</br> <b>����</b> <li>note_id��url������Ŀid</br> <b>����ֵ</b> <li>
	 * code��REQUIRE_SIGNIN��NOTE_NOT_EXIST <li>note����Ŀ���ʼ����ݣ�Ϊ��ϣ���ṹ�����¼ <li>note_update_time�����Ȿ����ʱ�䣬Ϊ��ϣ��key�Ǵ���ѧ�Ƶ������������¼����value�Ǹÿ�Ŀ�����д����������ʱ�䣬��������unixʱ���ʾ
	 */
	RespDeletedOrPuttedNotes delete0student$notes(String note_id);
	/**{@link IQueOrNoteLookUpService#delete0student$notes(String)} and {@link IQueOrNoteLookUpService#put0student$notes(String, String, String, String)}*/
	public static class RespDeletedOrPuttedNotes extends BaseRespBean
	{
		private RespQueOrNote note;
		private Map<String, Long> note_update_time;

		public RespQueOrNote getNote()
		{
			return note;
		}

		public void setNote(RespQueOrNote note)
		{
			this.note = note;
		}

		public Map<String, Long> getNote_update_time()
		{
			return note_update_time;
		}

		public void setNote_update_time(Map<String, Long> note_update_time)
		{
			this.note_update_time = note_update_time;
		}
	}

	/**
	 * ���´��Ȿ�е�һ����Ŀ��put@student/notes/#{note_id} �� student/notes_controller#update</br> <b>����</b> <li>note_id��url������Ŀid <li>tag��ѡ��ı�ǩ���� <li>topics���������ӵ�֪ʶ��
	 * <li>summary���ܽ�ʼ�</br> <b>����ֵ</b></br> <li>code��REQUIRE_SIGNIN��NOTE_NOT_EXIST <li>note����Ŀ���ʼ����ݣ�Ϊ��ϣ���ṹ�����¼ <li>
	 * note_update_time�����Ȿ����ʱ�䣬Ϊ��ϣ��key�Ǵ���ѧ�Ƶ������������¼����value�Ǹÿ�Ŀ�����д����������ʱ�䣬��������unixʱ���ʾ
	 */
	RespDeletedOrPuttedNotes put0student$notes(String note_id , String tag , String topics , String summary);
	public static class ReqUpdateNote extends ABaseReqBean
	{
		private String tag;
		private String topics;
		private String summary;

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
	
	public static class Factory
	{
		private Factory()
		{
		}

		private static IQueOrNoteLookUpService service;

		public synchronized static IQueOrNoteLookUpService getService()
		{
			if (null == service)
				service = new QueOrNoteLookUpImpl();
			return service;
		}
	}
}
