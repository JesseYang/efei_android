package com.efei.android.module.edit;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.android.module.scan.ScanActivity;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.common.EfeiApplication;

public class QuestiontEditActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("É¨Âë");
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_edit);

		EfeiApplication app = (EfeiApplication) getApplication();
		QuestionOrNote2 queOrNote = app.removeTemporary(Constants.TMP_QUE);
		setupViews(queOrNote);
	}

	private void setupViews(QuestionOrNote2 queOrNote)
	{
		TextView tvQue = (TextView) findViewById(R.id.tv_que);
		tvQue.setText(queOrNote.content);
		TextView tvTag = (TextView) findViewById(R.id.tv_tag);
		tvTag.setText(queOrNote.metaData.getTag());
		TextView tvTopics = (TextView) findViewById(R.id.tv_topics);
		tvTopics.setText(queOrNote.metaData.getTopics());
		TextView tvSummary = (TextView) findViewById(R.id.tv_summary);
		tvSummary.setText(queOrNote.metaData.getSummary());
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuItem item = menu.add(Menu.NONE, R.id.menu_save, Menu.NONE, "±£´æ");
		MenuCompat.setShowAsAction(item, MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			EfeiApplication.switchToActivity(ScanActivity.class);
			return true;

		case R.id.menu_save:
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}
}
