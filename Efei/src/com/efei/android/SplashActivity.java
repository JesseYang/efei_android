package com.efei.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.efei.android.module.Constants;
import com.efei.android.module.account.LoginActivity;
import com.efei.lib.android.biz_remote_interface.ISettingService;
import com.efei.lib.android.biz_remote_interface.ISettingService.RespStudentInfo;
import com.efei.lib.android.common.EfeiApplication;

public class SplashActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new Thread(new InitRunner()).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class InitRunner implements Runnable
	{

		@Override
		public void run()
		{
			try
			{
				ISettingService service = ISettingService.Factory.getService();
				final RespStudentInfo studentInfo = service.get0student$students$info();
				EfeiApplication app = (EfeiApplication) getApplication();
				app.addTemporary(Constants.KEY_EMAIL, studentInfo.getEmail());
			} catch (Exception e)
			{
				e.printStackTrace();
			} finally
			{
				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		}
	}
}
