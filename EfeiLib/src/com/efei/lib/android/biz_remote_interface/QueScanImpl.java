package com.efei.lib.android.biz_remote_interface;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.RespQueOrNote;
import com.efei.lib.android.exception.EfeiException;
import com.efei.lib.android.exception.KnownEfeiExcepiton;
import com.efei.lib.android.utils.NetUtils;
import com.efei.lib.android.utils.TextUtils;

class QueScanImpl extends BaseImpl implements IQueScanService
{

	@Override
	public RespQueId get(String short_url)
	{
		final String API_URL = short_url;
		return get(API_URL, null, RespQueId.class);
	}

	@Override
	public BaseRespBean get0student$questions(String question_id)
	{
		final String API_URL = "student/questions" + "/" + question_id;
		try
		{
			String json = NetUtils.get(API_URL, null);
			JSONObject jsonObj = new JSONObject(json);
			String note_id = jsonObj.optString("note_id");
			final BaseRespBean result;
			if (!TextUtils.isEmpty(note_id))
				result = BaseRespBean.toObject(json, RespNoteId.class);
			else
				result = BaseRespBean.toObject(json, RespQueOrNote.class);
			if (!result.isSuccess())
				throw new KnownEfeiExcepiton(result.getCode());
			return result;
		} catch (IOException e)
		{
			throw new EfeiException(e);
		} catch (JSONException e)
		{
			throw new EfeiException(e);
		}
	}

	@Override
	public RespAddSingleQue post0student$note(String question_id, String tag, String topics, String summary)
	{
		final String API_URL = "student/notes";
		ReqAddSingleQue req = new ReqAddSingleQue();
		req.setQuestion_id(question_id);
		req.setSummary(summary);
		req.setTag(tag);
		req.setTopics(topics);
		return NetUtils.postObjectAsJson(API_URL, req, RespAddSingleQue.class);
	}

	@Override
	public RespAddBatchQues post0student$notes$batch(String... question_ids)
	{
		final String API_URL = "student/notes/batch";
		ReqAddBatchQues req = new ReqAddBatchQues();
		req.setQuestion_ids(question_ids);
		return NetUtils.postObjectAsJson(API_URL, req, RespAddBatchQues.class);
	}

}
