package com.efei.lib.android.repository;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.ReqNotesBatch;
import com.efei.lib.android.bean.net.RespNotesBatch;
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
	private static final String TAG = QuestionNoteRepo.class.getSimpleName();
	private final static String URL_API_QUE = "student/questions/";
	// private final static String URL_API_CREATE_NOTE = "student/notes/";
	private final static String URL_API_CREATE_NOTES_BATCH = "student/notes/batch/";

	private QuestionNoteRepo()
	{
	}

	public QuestionOrNote queryByShortLink(String shortLink)
	{
		try
		{
			String jsonQueNoteId = NetUtils.get(encodeShortLink(shortLink), null);
			RespQueNoteId respQueId = BaseRespBean.toObject(jsonQueNoteId, RespQueNoteId.class);
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

			String json = NetUtils.get(URL_API_QUE + respQueId.getQuestion_id(), null);
			RespQueOrNote resp = BaseRespBean.toObject(json, RespQueOrNote.class);
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
		// http://dev.efei.org/~hJzX4
		return shortLink.substring(shortLink.indexOf("~"));
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

	private static final class RespQueNoteId extends BaseRespBean
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

	private void createOrUpdateInLocal(final QuestionOrNote... queOrNotes)
	{
		execute(new DBExecutor<Void, QuestionOrNote>()
		{
			@Override
			public Void execute(Dao<QuestionOrNote, String> dao) throws SQLException
			{
				ILoginService service = ServiceFactory.INSTANCE.getService(ServiceFactory.LOGIN_SERVICE);
				Account defaultUser = service.getDefaultUser();
				if (null == defaultUser)
					throw new EfeiException("no default user!");
				for (QuestionOrNote queOrNote : queOrNotes)
				{
					queOrNote.setAccount(defaultUser);
					if (dao.createOrUpdate(queOrNote).getNumLinesChanged() != 1)
						throw new EfeiException("create " + queOrNote + "in " + TAG + "failed");
				}
				return null;
			}
		}, QuestionOrNote.class);
	}

	public void createOrUpdate(final List<QuestionOrNote> queOrNotes)
	{
		List<String> queIds = new ArrayList<String>();
		for (QuestionOrNote queOrNote : queOrNotes)
			queIds.add(queOrNote.getId());
		ReqNotesBatch req = new ReqNotesBatch();
		req.setQuestion_ids(queIds);
		RespNotesBatch resp = NetUtils.postObjectAsJson(URL_API_CREATE_NOTES_BATCH, req, RespNotesBatch.class);
		if (!resp.isSuccess())
			throw new EfeiException("post question or notes failed");
		createOrUpdateInLocal(queOrNotes.toArray(new QuestionOrNote[0]));
	}

	/**
	 * load all que or note for default user
	 * 
	 * @return
	 */
	public List<QuestionOrNote> loadAll()
	{
		return execute(new DBExecutor<List<QuestionOrNote>, QuestionOrNote>()
		{
			@Override
			public List<QuestionOrNote> execute(Dao<QuestionOrNote, String> dao) throws SQLException
			{
				ILoginService service = ServiceFactory.INSTANCE.getService(ServiceFactory.LOGIN_SERVICE);
				Account defaultUser = service.getDefaultUser();
				if (null == defaultUser)
					throw new EfeiException("no default user!");
				List<QuestionOrNote> results = dao.queryForEq(QuestionOrNote.Properties.AccountId, defaultUser.getEmail_mobile());
				for (QuestionOrNote result : results)
					result.setFormattedContent(new RichText(result.getContent()).getReformatText());

				return results;
			}
		}, QuestionOrNote.class);
	}
	
}
