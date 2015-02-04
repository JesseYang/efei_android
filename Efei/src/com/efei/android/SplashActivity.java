package com.efei.android;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.efei.android.module.account.LoginActivity;
import com.efei.android.module.settings.teacher.MyTeacherActivity;
import com.efei.lib.android.bean.net.common_data.Teacher;
import com.efei.lib.android.biz_remote_interface.ISettingService;
import com.efei.lib.android.biz_remote_interface.ISettingService.RespSearchTeachers;
import com.efei.lib.android.utils.CollectionUtils;

public class SplashActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new Thread(new TestRunner()).start();
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

	private class TestRunner implements Runnable
	{

		@Override
		public void run()
		{
			try
			{
				RespSearchTeachers teachers = ISettingService.Factory.getService().get0student$teachers();
				List<Teacher> teachersList = teachers.getTeachers();
				if (!CollectionUtils.isEmpty(teachersList))
				{
					for (Teacher teacher : teachersList)
						MyTeacherActivity.MY_TEACHERS.put(teacher.getId(), teacher);
				}
				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
