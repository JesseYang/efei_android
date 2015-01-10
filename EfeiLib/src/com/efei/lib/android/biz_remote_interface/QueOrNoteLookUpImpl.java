package com.efei.lib.android.biz_remote_interface;

import java.util.HashMap;
import java.util.Map;

import com.efei.lib.android.utils.NetUtils;

class QueOrNoteLookUpImpl extends BaseImpl implements IQueOrNoteLookUpService
{
	QueOrNoteLookUpImpl()
	{
	}

	@Override
	public RespTopics_PinyinEntry get0student$topics(int subject)
	{
		final String API_URL = "student/topics";

		Map<String, String> params = new HashMap<String, String>();
		params.put("subject", subject + "");

		return get(API_URL, params, RespTopics_PinyinEntry.class);
	}

	@Override
	public RespNoteUpdateTime get0student$notes$note_update_time()
	{
		final String API_URL = "student/notes/note_update_time";
		return get(API_URL, null, RespNoteUpdateTime.class);
	}

	@Override
	public RespNoteId_UpdateTimeEntry get0student$notes()
	{
		final String API_URL = "student/notes";
		return get(API_URL, null, RespNoteId_UpdateTimeEntry.class);
	}

	@Override
	public RespNestNote get0student$notes(String note_id)
	{
		final String API_URL = "student/notes" + "/" + note_id;
		return get(API_URL, null, RespNestNote.class);
	}

	@Override
	public RespNestQueOrNoteArray get0student$notes$list(String... ids)
	{
		final String API_URL = "student/notes/list";

		String allId = "";
		for (String id : ids)
			allId += (id + ",");
		Map<String, String> params = new HashMap<String, String>();
		params.put("note_ids", allId);

		return get(API_URL, params, RespNestQueOrNoteArray.class);
	}

	@Override
	public RespDeletedOrPuttedNotes delete0student$notes(String note_id)
	{
		final String API_URL = "student/notes" + "/" + note_id;

		return delete(API_URL, null, RespDeletedOrPuttedNotes.class);
	}

	@Override
	public RespDeletedOrPuttedNotes put0student$notes(String note_id, String tag, String topics, String summary)
	{
		final String API_URL = "student/notes" + "/" + note_id;
		ReqUpdateNote req = new ReqUpdateNote();
		req.setTag(tag);
		req.setTopics(topics);
		req.setSummary(summary);
		return NetUtils.put(API_URL, req, RespDeletedOrPuttedNotes.class);
	}

}
