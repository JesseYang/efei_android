package com.efei.lib.android.repository;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.sql.SQLException;

import com.efei.lib.android.bean.net.ABaseRespBean;
import com.efei.lib.android.bean.net.RespQueOrNote;
import com.efei.lib.android.bean.persistance.Account;
import com.efei.lib.android.bean.persistance.QuestionOrNote;
import com.efei.lib.android.engine.ILoginService;
import com.efei.lib.android.engine.ServiceFactory;
import com.efei.lib.android.exception.EfeiException;
import com.efei.lib.android.grammar.RichText;
import com.efei.lib.android.utils.NetUtils;
import com.j256.ormlite.dao.Dao;

public class QuestionNoteRepo extends ABaseRepo<QuestionOrNote>
{
	private final static String URL_API_QUE = "student/questions/";
	protected static final String TAG = QuestionNoteRepo.class.getSimpleName();

	private QuestionNoteRepo()
	{
	}

	public QuestionOrNote queryByShortLink(String shortLink)
	{
		try
		{
			String jsonQueNoteId = NetUtils.getAsString(encodeShortLink(shortLink), null);
			RespQueNoteId respQueId = ABaseRespBean.toObject(jsonQueNoteId, RespQueNoteId.class);
			if (!respQueId.isSuccess())
				throw new EfeiException("get question id by short url--" + shortLink + " failed");
			QuestionOrNote queOrNoteLocal = execute(
					new QueryByFieldExecutor<QuestionOrNote>(QuestionOrNote.Properties.Id, respQueId.getQuestion_id()),
					QuestionOrNote.class);
			if (null != queOrNoteLocal)
			{
				queOrNoteLocal.setFormattedContent(new RichText(queOrNoteLocal.getContent()).getReformatText());
				return queOrNoteLocal;
			}

			String json = NetUtils.getAsString(URL_API_QUE + respQueId.getQuestion_id(), null);
			RespQueOrNote resp = ABaseRespBean.toObject(json, RespQueOrNote.class);
			ILoginService loginService = ServiceFactory.INSTANCE.getService(ServiceFactory.LOGIN_SERVICE);
			Account defaultUser = loginService.getDefaultUser();
			QuestionOrNote queOrNote = new QuestionOrNote(resp, defaultUser);
			queOrNote.setFormattedContent(new RichText(queOrNote.getContent()).getReformatText());
			if (null == defaultUser)
				return queOrNote;
			createOrUpdateInLocal(queOrNote);
			return queOrNote;
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

	private static final class RespQueNoteId extends ABaseRespBean
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

	private void createOrUpdateInLocal(final QuestionOrNote queOrNote)
	{
		execute(new DBExecutor<Void, QuestionOrNote>()
		{
			@Override
			public Void execute(Dao<QuestionOrNote, String> dao) throws SQLException
			{
				if (dao.createOrUpdate(queOrNote).getNumLinesChanged() != 1)
					throw new EfeiException("create " + queOrNote + "in " + TAG + "failed");
				return null;
			}
		}, QuestionOrNote.class);
	}

}
