package com.efei.android.module.edit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService.RespTopics_PinyinEntry;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.exception.EfeiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TagTopicsEditActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_topics_edit);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		String strEditContent = intent.getStringExtra(KEY_EDIT_CONTENT);
		EditContent editContent = EditContent.valueOf(strEditContent);
		EfeiApplication app = (EfeiApplication) getApplication();
		final QuestionOrNote2 queOrNote = app.removeTemporary(Constants.TMP_QUE);
		switch (editContent)
		{
		case Tag:
			actionBar.setTitle("标签编辑");
			getSupportFragmentManager().beginTransaction().add(R.id.fl_container, new TagEditFragment(queOrNote), "tag").commitAllowingStateLoss();
			return;
		case Topics:
			actionBar.setTitle("知识点编辑");
			findViewById(R.id.pb_progress).setVisibility(View.VISIBLE);
			Executor.INSTANCE.execute(new JobAsyncTask<RespTopics_PinyinEntry>(new BizRunner_GetAutoCompleteTopics(queOrNote),
					new IUICallback.Adapter<RespTopics_PinyinEntry>()
					{
						@Override
						public void onProgressUpdate(int percent, Object... params)
						{
							findViewById(R.id.pb_progress).setVisibility(View.GONE);
							getSupportFragmentManager()
									.beginTransaction()
									.add(R.id.fl_container,
											new TopicsEditFragment(queOrNote, (RespTopics_PinyinEntry) params[0]),
											"topics").commitAllowingStateLoss();
						}

					}));
			return;
		default:
			throw new EfeiException("can't start activity for edit_content:" + editContent);
		}
	}

	public static enum EditContent
	{
		Topics, Tag;
	}

	private static final String KEY_EDIT_CONTENT = "key.edit.content";

	public static void forward(EditContent content)
	{
		Intent intent = new Intent(EfeiApplication.getContext(), TagTopicsEditActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(KEY_EDIT_CONTENT, content.name());
		EfeiApplication.getContext().startActivity(intent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (android.R.id.home == item.getItemId())
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private static class BizRunner_GetAutoCompleteTopics implements IBusinessCallback<RespTopics_PinyinEntry>
	{
		private QuestionOrNote2 queOrNote;

		public BizRunner_GetAutoCompleteTopics(QuestionOrNote2 queOrNote)
		{
			this.queOrNote = queOrNote;
		}

		@Override
		public RespTopics_PinyinEntry onBusinessLogic(IJob job) throws Exception
		{
			String cacheKey = "get0student$topics" + queOrNote.metaData.getSubject();
			SharedPreferences sp = EfeiApplication.getContext().getSharedPreferences("cache", Context.MODE_PRIVATE);
			final RespTopics_PinyinEntry res;
			Editor editor = sp.edit();
			String cacheValue = sp.getString(cacheKey, null);
			final ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if (null == cacheValue)
			{
				res = IQueOrNoteLookUpService.Factory.getService().get0student$topics(queOrNote.metaData.getSubject());
				job.publishProgress(0, res);

				try
				{
					cacheValue = mapper.writeValueAsString(res);
					editor.putString(cacheKey, cacheValue).commit();
				} catch (JsonProcessingException e)
				{
					throw new EfeiException(e);
				}
			} else
			{
				job.publishProgress(0, mapper.readValue(cacheValue, RespTopics_PinyinEntry.class));

				res = IQueOrNoteLookUpService.Factory.getService().get0student$topics(queOrNote.metaData.getSubject());
				try
				{
					cacheValue = mapper.writeValueAsString(res);
					editor.putString(cacheKey, cacheValue).commit();
				} catch (JsonProcessingException e)
				{
					throw new EfeiException(e);
				}
			}

			return res;
		}
	}
}
