package com.efei.android.module.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
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
import com.efei.android.module.list.BizRunner_DeleteQue;
import com.efei.android.module.list.ExportActivity;
import com.efei.android.module.scan.ScanActivity;
import com.efei.android.module.settings.teacher.ConfirmAddTeacherActivity;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.IUICallback.Adapter;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.persistance.Account;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService.RespDeletedOrPuttedNotes;
import com.efei.lib.android.biz_remote_interface.IQueScanService.RespAddSingleQue;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.engine.ILoginService;
import com.efei.lib.android.utils.TextUtils;

public class QuestiontEditActivity extends ActionBarActivity
{
	public static final String KEY_CREATE_QUE = "key_create_que";
	public static final String KEY_WHO_START_ME = "key_who_start_me";
	private QuestionOrNote2 queOrNote;
	private EditText etSummary;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		if (ScanActivity.class.getName().equals(getIntent().getStringExtra(KEY_WHO_START_ME)))
			actionBar.setTitle("扫一扫");
		else
			actionBar.setTitle("错题本");
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
		tvTopics.setText(queOrNote.metaData.getTopic_str());
		etSummary = (EditText) findViewById(R.id.et_summary);
		boolean empty = TextUtils.isEmpty(queOrNote.metaData.getSummary());
		if (!empty)
			etSummary.setText(queOrNote.metaData.getSummary());
		findViewById(R.id.tv_show_ans).setOnClickListener(new OnClickListener()
		{
			private boolean initClick = true;

			@Override
			public void onClick(View v)
			{
				final TextView tvAns = (TextView) findViewById(R.id.tv_ans);
				final TextView btnAns = (TextView) v;

				if (TextUtils.isBlank(queOrNote.answer))
				{
					findViewById(R.id.ll_ans_container).setVisibility(View.GONE);
					tvAns.setText("");
					btnAns.setCompoundDrawablesWithIntrinsicBounds(R.drawable.visible, 0, 0, 0);
					if (initClick)
						initClick = false;
					else
						Toast.makeText(QuestiontEditActivity.this, "答案未上传！", Toast.LENGTH_SHORT).show();
					return;
				}

				v.setSelected(!v.isSelected());

				if (v.isSelected())
				{
					findViewById(R.id.ll_ans_container).setVisibility(View.VISIBLE);
					tvAns.setText(queOrNote.answer);
					btnAns.setCompoundDrawablesWithIntrinsicBounds(R.drawable.visible, 0, 0, 0);
				} else
				{
					findViewById(R.id.ll_ans_container).setVisibility(View.GONE);
					tvAns.setText("");
					btnAns.setCompoundDrawablesWithIntrinsicBounds(R.drawable.invisible, 0, 0, 0);
				}
			}
		});

		findViewById(R.id.tv_show_ans).performClick();

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
		final MenuItem itemSave = menu.add(Menu.NONE, R.id.menu_save, Menu.NONE, "保存");
		MenuCompat.setShowAsAction(itemSave, MenuItem.SHOW_AS_ACTION_ALWAYS);

		if (getIntent().getBooleanExtra(KEY_CREATE_QUE, false))
			return super.onCreateOptionsMenu(menu);

		final MenuItem itemDelete = menu.add("\t删除");
		itemDelete.setIcon(R.drawable.delete).setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				Executor.INSTANCE.execute(new JobAsyncTask<RespDeletedOrPuttedNotes>(new BizRunner_DeleteQue(queOrNote.metaData.get_id()),
						new IUICallback.Adapter<RespDeletedOrPuttedNotes>()
						{
							@Override
							public void onPostExecute(RespDeletedOrPuttedNotes result)
							{
								Toast.makeText(QuestiontEditActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
								finish();
							}
						}));
				return true;
			}
		});
		MenuCompat.setShowAsAction(itemDelete, MenuItem.SHOW_AS_ACTION_NEVER);

		final MenuItem itemExport = menu.add("\t导出");
		itemExport.setIcon(R.drawable.export).setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				Intent intent = new Intent(QuestiontEditActivity.this, ExportActivity.class);
				intent.putExtra(ExportActivity.KEY_EXPORT_QUE_IDS, new String[]
				{ queOrNote.metaData.get_id() });
				startActivity(intent);
				return true;
			}
		});
		MenuCompat.setShowAsAction(itemExport, MenuItem.SHOW_AS_ACTION_NEVER);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			return true;

		case R.id.menu_save:
			Account defaultUser = ILoginService.Factory.getService().getDefaultUser();
			if (null == defaultUser)
			{
				EfeiApplication app = (EfeiApplication) getApplication();
				app.addTemporary(Constants.TMP_QUE, queOrNote);
				Intent intent = new Intent(QuestiontEditActivity.this, LoginActivity.class);
				intent.putExtra(Constants.KEY_FOR_SAVE_QUE, true);
				startActivity(intent);
				finish();
				return true;
			}

			Editable text = etSummary.getText();
			if (null != text && !TextUtils.isEmpty(text))
				queOrNote.metaData.setSummary(text.toString());
			Executor.INSTANCE.execute(new JobAsyncTask<BaseRespBean>(new BizRunner_SaveQue(queOrNote, getIntent().getBooleanExtra(KEY_CREATE_QUE,
					false)), uiCallback));
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	private IUICallback.Adapter<BaseRespBean> uiCallback = new Adapter<BaseRespBean>()
	{
		public void onPostExecute(BaseRespBean result)
		{
			Toast.makeText(getApplicationContext(), "保存成功！", Toast.LENGTH_SHORT).show();
			finish();
			if (result instanceof RespAddSingleQue)
			{
				RespAddSingleQue resp = (RespAddSingleQue) result;
				if (null == resp.getTeacher())
					EfeiApplication.switchToActivity(MainActivity.class);
				else
				{
					EfeiApplication app = (EfeiApplication) getApplication();
					app.addTemporary(Constants.TMP_TEACHER, resp.getTeacher());
					Intent intent = new Intent(QuestiontEditActivity.this, ConfirmAddTeacherActivity.class);
					intent.putExtra(Constants.KEY_FOR_SAVE_QUE, true);
					startActivity(intent);
				}
			} else
				EfeiApplication.switchToActivity(MainActivity.class);
		};

	};
}
