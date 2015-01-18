package com.efei.android.module.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.biz_remote_interface.ISettingService.RespStudentInfo;

public class SettingsMeActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_me);
		setupViews();
	}

	private void setupViews()
	{
		fetchUserInfo();

		findViewById(R.id.ll_name_bar).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
			}
		});

		findViewById(R.id.ll_email_bar).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
			}
		});

		findViewById(R.id.ll_mobile_bar).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
			}
		});
	}

	private void fetchUserInfo()
	{
		Executor.INSTANCE.execute(new JobAsyncTask<RespStudentInfo>(new BizRunner_GetUserInfo(), new IUICallback.Adapter<RespStudentInfo>()
		{
			@Override
			public void onPostExecute(RespStudentInfo result)
			{
				TextView tvName = (TextView) findViewById(R.id.tv_name);
				tvName.setText(result.getName());
				TextView tvEmail = (TextView) findViewById(R.id.tv_email);
				tvEmail.setText(result.getEmail());
				TextView tvMobile = (TextView) findViewById(R.id.tv_mobile);
				tvMobile.setText(result.getMobile());
			}
		}));
	}
}
