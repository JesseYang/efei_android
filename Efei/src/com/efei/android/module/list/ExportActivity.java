package com.efei.android.module.list;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.android.module.settings.me.EmailActivity;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService.RespExport;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.utils.NetUtils;
import com.efei.lib.android.utils.TextUtils;

public class ExportActivity extends ActionBarActivity
{
	public static final String KEY_EXPORT_QUE_IDS = "key_export_que_ids";
	private String[] queIds;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("导出");
		queIds = getIntent().getStringArrayExtra(KEY_EXPORT_QUE_IDS);
		if (null == queIds || 0 == queIds.length)
		{
			finish();
			return;
		}
		setContentView(R.layout.activity_export);
		// setupViews();
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		setupViews();
	}

	private boolean hasAnswer;
	private boolean hasNote;
	private View barExportDownload;
	private View barExportEmail;
	private String email;

	private void setupViews()
	{
		TextView tvPrompt = (TextView) findViewById(R.id.tv_prompt);
		tvPrompt.setText("导出选中的" + queIds.length + "道题目");

		final View barContainsAns = findViewById(R.id.contains_ans);
		TextView tv = (TextView) barContainsAns.findViewById(R.id.tv);
		tv.setText("包含答案");
		barContainsAns.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				View check = v.findViewById(R.id.iv_check);
				v.setSelected(!v.isSelected());
				final int visible = v.isSelected() ? View.VISIBLE : View.INVISIBLE;
				check.setVisibility(visible);
				hasAnswer = v.isSelected();
			}
		});
		barContainsAns.performClick();

		final View barContainsNote = findViewById(R.id.contains_note);
		tv = (TextView) barContainsNote.findViewById(R.id.tv);
		tv.setText("包含笔记");
		barContainsNote.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				View check = v.findViewById(R.id.iv_check);
				v.setSelected(!v.isSelected());
				final int visible = v.isSelected() ? View.VISIBLE : View.INVISIBLE;
				check.setVisibility(visible);
				hasNote = check.isSelected();
			}
		});
		barContainsNote.performClick();

		barExportDownload = findViewById(R.id.export_by_download);
		tv = (TextView) barExportDownload.findViewById(R.id.tv);
		tv.setText("直接下载");
		barExportDownload.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				View check = v.findViewById(R.id.iv_check);
				v.setSelected(!v.isSelected());
				int visible = v.isSelected() ? View.VISIBLE : View.INVISIBLE;
				check.setVisibility(visible);

				barExportEmail.setSelected(!v.isSelected());
				visible = barExportEmail.isSelected() ? View.VISIBLE : View.INVISIBLE;
				barExportEmail.findViewById(R.id.iv_check).setVisibility(visible);

				// findViewById(R.id.email).setEnabled(!v.isSelected());
				// if (v.isSelected())
				// {
				// EditText email = (EditText) findViewById(R.id.email);
				// email.setText("");
				// }
			}
		});

		EfeiApplication app = (EfeiApplication) getApplication();
		email = app.removeTemporary(Constants.KEY_EMAIL);
		app.addTemporary(Constants.KEY_EMAIL, email);
		final String emailDisplay = TextUtils.isBlank(email) ? "（未设置）" : email;

		barExportEmail = findViewById(R.id.export_by_email);
		tv = (TextView) barExportEmail.findViewById(R.id.tv);
		tv.setText("发送至邮箱" + emailDisplay);
		barExportEmail.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (TextUtils.isBlank(email))
				{
					EfeiApplication.switchToActivity(EmailActivity.class);
					return;
				}

				View check = v.findViewById(R.id.iv_check);
				v.setSelected(!v.isSelected());
				int visible = v.isSelected() ? View.VISIBLE : View.INVISIBLE;
				check.setVisibility(visible);

				barExportDownload.setSelected(!v.isSelected());
				visible = barExportDownload.isSelected() ? View.VISIBLE : View.INVISIBLE;
				barExportDownload.findViewById(R.id.iv_check).setVisibility(visible);

				// findViewById(R.id.email).setEnabled(v.isSelected());
				// if (v.isSelected())
				// {
				// EditText email = (EditText) findViewById(R.id.email);
				// email.requestFocus();
				// }
			}
		});

		barExportDownload.setSelected(false);
		barExportEmail.setSelected(true);
		barExportDownload.performClick();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuItem item = menu.add("完成").setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				Executor.INSTANCE.execute(new JobAsyncTask<RespExport>(new BizRunner_Export(hasAnswer, hasNote, queIdsArrayToString(),
						getEmail()), new IUICallback.Adapter<RespExport>()
				{
					@Override
					public void onPostExecute(RespExport result)
					{
						if (barExportDownload.isSelected())
							Toast.makeText(ExportActivity.this,
									"导出成功，存放在" + Environment.getExternalStorageDirectory() + "/efei/download/目录下",
									Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(ExportActivity.this, "导出成功，已经发送到" + email + "，请查收", Toast.LENGTH_SHORT).show();
						finish();
					}
				}));
				return true;
			}
		});
		MenuCompat.setShowAsAction(item, MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	private String queIdsArrayToString()
	{
		String res = "";
		for (String id : queIds)
			res += (id + ",");
		return res;
	}

	private String getEmail()
	{
		return TextUtils.isBlank(email) ? null : email;
		// EditText et = (EditText) findViewById(R.id.email);
		// Editable text = et.getText();
		// if (TextUtils.isBlank(text))
		// return null;
		// return text.toString();
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

	private static class BizRunner_Export implements IBusinessCallback<RespExport>
	{
		private boolean has_answer;
		private boolean has_note;
		private String note_id_str;
		private String email;

		public BizRunner_Export(boolean has_answer, boolean has_note, String note_id_str, String email)
		{
			this.has_answer = has_answer;
			this.has_note = has_note;
			this.note_id_str = note_id_str;
			this.email = email;
		}

		@Override
		public RespExport onBusinessLogic(IJob job) throws Exception
		{
			RespExport resp = IQueOrNoteLookUpService.Factory.getService().get0student$notes$export(has_answer, has_note, note_id_str, email);
			if (TextUtils.isBlank(email))
			{
				InputStream is = NetUtils.getAsStream(resp.getFile_path(), null);
				String download = Environment.getExternalStorageDirectory() + "/efei/download/";
				File file = new File(download, resp.getFile_path().substring(resp.getFile_path().lastIndexOf("/")));
				OutputStream os = FileUtils.openOutputStream(file);
				int iLen = -1;
				byte[] buffer = new byte[1024];
				while (-1 != (iLen = is.read(buffer)))
					os.write(buffer, 0, iLen);
				is.close();
				os.close();
			}
			return resp;
		}
	}
}
