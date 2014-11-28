package com.efei.lib.android.repository;

import java.io.IOException;
import java.lang.ref.SoftReference;

import com.efei.lib.android.bean.net.ABaseRespBean;
import com.efei.lib.android.bean.net.RespQueOrNote;
import com.efei.lib.android.exception.EfeiException;
import com.efei.lib.android.utils.NetUtils;

public class QuestionNoteRepo
{
	private final static String URL_API_QUE = "student/questions/";

	private QuestionNoteRepo()
	{
	}

	public RespQueOrNote queryByShortLink(String shortLink)
	{
		try
		{
			String jsonQueNoteId = NetUtils.getAsString(encodeShortLink(shortLink), null);
			RespQueId respQueId = ABaseRespBean.toObject(jsonQueNoteId, RespQueId.class);
			if (!respQueId.isSuccess())
				throw new EfeiException("get question id by short url--" + shortLink + " failed");
			String json = NetUtils.getAsString(URL_API_QUE + respQueId.getQuestion_id(), null);
			return ABaseRespBean.toObject(json, RespQueOrNote.class);
		} catch (IOException e)
		{
			throw new EfeiException(e);
		}
	}

	private String encodeShortLink(String shortLink)
	{
		return "~" + shortLink;
	}

	private static SoftReference<QuestionNoteRepo> repo;

	public static QuestionNoteRepo getInstance()
	{
		if (null == repo || null == repo.get())
		{
			QuestionNoteRepo repoResult = new QuestionNoteRepo();
			repo = new SoftReference<QuestionNoteRepo>(repoResult);
			return repoResult;
		}

		return repo.get();
	}

	private static final class RespQueId extends ABaseRespBean
	{
		private String question_id;

		public String getQuestion_id()
		{
			return question_id;
		}

		@SuppressWarnings("unused")
		void setQuestion_id(String question_id)
		{
			this.question_id = question_id;
		}
	}
}
