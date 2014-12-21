package com.efei.lib.android.bean.net;

import java.util.List;
import java.util.Map;

import com.efei.lib.android.bean.net.common_data.Teacher;

public class RespNotesBatch extends ABaseRespBean
{
	private Map<Integer, Long> note_update_time;
	private List<Teacher> teachers;

	public Map<Integer, Long> getNote_update_time()
	{
		return note_update_time;
	}

	public void setNote_update_time(Map<Integer, Long> note_update_time)
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
