package com.efei.android.module.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.android.module.MainActivity;
import com.efei.android.module.account.LoginActivity;
import com.efei.android.module.edit.TagTopicsEditActivity.EditContent;
import com.efei.android.module.scan.ScanActivity;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.IUICallback.Adapter;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.persistance.Account;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.biz_remote_interface.IQueScanService;
import com.efei.lib.android.biz_remote_interface.IQueScanService.RespAddSingleQue;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.engine.ILoginService;
import com.efei.lib.android.engine.ServiceFactory;
import com.efei.lib.android.utils.TextUtils;

public class QuestiontEditActivity extends ActionBarActivity
{
	private QuestionOrNote2 queOrNote;
	private EditText etSummary;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("ɨ��");
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_edit);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		EfeiApplication app = (EfeiApplication) getApplication();
		queOrNote = app.removeTemporary(Constants.TMP_QUE);
		setupViews(queOrNote);
	}

	private void setupViews(final QuestionOrNote2 queOrNote)
	{
		TextView tvQue = (TextView) findViewById(R.id.tv_que);
		tvQue.setText(queOrNote.content);
		TextView tvTag = (TextView) findViewById(R.id.tv_tag);
		tvTag.setText(queOrNote.metaData.getTag());
		TextView tvTopics = (TextView) findViewById(R.id.tv_topics);
		tvTopics.setText(queOrNote.metaData.getTopics());
		etSummary = (EditText) findViewById(R.id.et_summary);
		boolean empty = TextUtils.isEmpty(queOrNote.metaData.getSummary());
		if (!empty)
			etSummary.setText(queOrNote.metaData.getSummary());
		findViewById(R.id.tv_show_ans).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				v.setSelected(!v.isSelected());
				CharSequence showTxt = v.isSelected() ? queOrNote.answer : "";
				TextView tvAns = (TextView) findViewById(R.id.tv_ans);
				tvAns.setText(showTxt);
			}
		});

		findViewById(R.id.ll_tag_bar).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				EfeiApplication app = (EfeiApplication) getApplication();
				app.addTemporary(Constants.TMP_QUE, queOrNote);
				TagTopicsEditActivity.forward(EditContent.Tag);
			}
		});

		findViewById(R.id.ll_topics_bar).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				EfeiApplication app = (EfeiApplication) getApplication();
				app.addTemporary(Constants.TMP_QUE, queOrNote);
				TagTopicsEditActivity.forward(EditContent.Topics);
			}
		});
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuItem item = menu.add(Menu.NONE, R.id.menu_save, Menu.NONE, "����");
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
			ILoginService service = ServiceFactory.INSTANCE.getService(ServiceFactory.LOGIN_SERVICE);
			Account defaultUser = service.getDefaultUser();
			if (null == defaultUser)
			{
				EfeiApplication app = (EfeiApplication) getApplication();
				app.addTemporary(Constants.TMP_QUE, queOrNote);
				Intent intent = new Intent(QuestiontEditActivity.this, LoginActivity.class);
				intent.putExtra(Constants.LOGIN_FOR_SAVE_QUE, true);
				startActivity(intent);
				finish();
				return true;
			}

			Editable text = etSummary.getText();
			if (null != text && !TextUtils.isEmpty(text))
				queOrNote.metaData.setSummary(text.toString());
			Executor.INSTANCE.execute(new JobAsyncTask<RespAddSingleQue>(new BizRunner_SaveQue(queOrNote.metaData), uiCallback));
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	private IUICallback.Adapter<RespAddSingleQue> uiCallback = new Adapter<IQueScanService.RespAddSingleQue>()
	{
		public void onPostExecute(RespAddSingleQue result)
		{
			Toast.makeText(getApplicationContext(), "����ɹ���", Toast.LENGTH_SHORT).show();
			EfeiApplication.switchToActivity(MainActivity.class);
			finish();
		};

		public void onError(Throwable e)
		{
			Toast.makeText(getApplicationContext(), "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
		};
	};
}