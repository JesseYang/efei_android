package com.efei.lib.android.biz_remote_interface;

import java.util.List;
import java.util.Map;

import com.efei.lib.android.bean.net.ABaseReqBean;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.RespQueOrNote;

public interface IQueOrNoteLookUpService
{
	/**
	 * 获取知识点列表：get@student/topics → student/topics_controller#index </br> <b>参数</b>： <li>subject：表示科目的整数，详见附录 1.2. </br> <b>返回值</b>： <li>code:TODO yunzhong
	 * <li>topics：数组，知识点列表，每个元素又是一个长度为2的数组，第一个元素是知识点，第二个元素是拼音，用于自动补全显示
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
	 * 获取错题本的最后更新时间：get@student/notes/note_update_time → student/notes_controller#note_update_time</br> <b>参数</b>：无</br> <b>返回值</b>： <li>code：REQUIRE_SIGNIN
	 * <li>note_update_time：错题本更新时间，为哈希，key是代表学科的整数（详见附录），value是该科目下所有错题的最后更新时间，用整数的unix时间表示
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
	 * 获取错题本的题目id及最后更新时间列表：get@student/notes → student/notes_controller#index</br> <b>参数</b>：无</br> <b>返回值</b>： <li>code：REQUIRE_SIGNIN <li>
	 * notes：数组，每个元素又是一个长度为2的数组，其中，第一个元素是note的id，第二个元素是该note的最后更新时间
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
	 * 获取错题本中题目内容：get@student/notes/#{note_id} → student/notes_controller#show</br> <b>参数</b>： <li>note_id（url）：题目id</br> <b>返回值</b>： <li>
	 * code：REQUIRE_SIGNIN，NOTE_NOT_EXIST <li>note：题目及笔记内容，为哈希，结构详见附录
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
	 * 获取错题本中多道题目的内容：get@student/notes/list → student/notes_controller#list</br> <b>参数</b>： <li>note_githubids：逗号分割的题目id</br> <b>返回值</b> <li>
	 * code：REQUIRE_SIGNIN <li>notes：数组，每一个元素是一个note的内容，为哈希，结构详见附录
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
	 * 删除错题本中的一道题目：delete@student/notes/#{note_id} → student/notes_controller#destroy</br> <b>参数</b> <li>note_id（url）：题目id</br> <b>返回值</b> <li>
	 * code：REQUIRE_SIGNIN，NOTE_NOT_EXIST <li>note：题目及笔记内容，为哈希，结构详见附录 <li>note_update_time：错题本更新时间，为哈希，key是代表学科的整数（详见附录），value是该科目下所有错题的最后更新时间，用整数的unix时间表示
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
	 * 更新错题本中的一道题目：put@student/notes/#{note_id} → student/notes_controller#update</br> <b>参数</b> <li>note_id（url）：题目id <li>tag：选择的标签内容 <li>topics：逗号连接的知识点
	 * <li>summary：总结笔记</br> <b>返回值</b></br> <li>code：REQUIRE_SIGNIN，NOTE_NOT_EXIST <li>note：题目及笔记内容，为哈希，结构详见附录 <li>
	 * note_update_time：错题本更新时间，为哈希，key是代表学科的整数（详见附录），value是该科目下所有错题的最后更新时间，用整数的unix时间表示
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
