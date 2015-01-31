package com.efei.android.module.settings.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.biz_remote_interface.ISettingService;
import com.efei.lib.android.biz_remote_interface.ISettingService.RespStudentInfo;
import com.efei.lib.android.common.EfeiApplication;

public class SettingsMeActivity extends ActionBarActivity
{
	static final String KEY_MOBILE = "key_mobile";
	static final String KEY_EMAIL = "key_email";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_me);
		getSupportActionBar().setTitle("∏ˆ»À…Ë÷√");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setupViews();
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

	@Override
	protected void onResume()
	{
		super.onResume();
		fetchUserInfo();
	}

	private void setupViews()
	{
		findViewById(R.id.ll_name_bar).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				EfeiApplication.switchToActivity(NameActivity.class);
			}
		});

		findViewById(R.id.ll_email_bar).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(SettingsMeActivity.this, EmailActivity.class);
				TextView tv = (TextView) v.findViewById(R.id.tv_email);
				intent.putExtra(KEY_EMAIL, tv.getText());
				startActivity(intent);
			}
		});

		findViewById(R.id.ll_mobile_bar).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(SettingsMeActivity.this, MobileActivity.class);
				TextView tv = (TextView) v.findViewById(R.id.tv_mobile);
				intent.putExtra(KEY_MOBILE, tv.getText());
				startActivity(intent);
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

	static final class BizRunner_GetUserInfo implements IBusinessCallback<RespStudentInfo>
	{
		@Override
		public RespStudentInfo onBusinessLogic(IJob job)
		{
			ISettingService service = ISettingService.Factory.getService();
			return service.get0student$students$info();
		}
	}
}
