package com.efei.android.module.question;

import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.efei.android.R;
import com.efei.android.module.scan.ScanActivity;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.persistance.QuestionOrNote;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.repository.QuestionNoteRepo;

public class QueListActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_que_list);

		ListView lv = (ListView) findViewById(R.id.lv_que_or_note);
		final QueListAdapter adapter = new QueListAdapter();
		lv.setAdapter(adapter);

		Executor.INSTANCE.execute(new JobAsyncTask<List<QuestionOrNote>>(new IBusinessCallback<List<QuestionOrNote>>()
		{

			@Override
			public List<QuestionOrNote> onBusinessLogic(IJob job)
			{
				return QuestionNoteRepo.getInstance().loadAll();
			}
		}, new IUICallback.Adapter<List<QuestionOrNote>>()
		{
			@Override
			public void onPostExecute(List<QuestionOrNote> result)
			{
				adapter.content.addAll(result);
				adapter.notifyDataSetChanged();
			}
		}));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.que_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			// TODO yunzhong:test tmp code
			EfeiApplication.switchToActivity(ScanActivity.class);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
