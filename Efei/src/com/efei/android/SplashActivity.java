package com.efei.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.efei.android.module.account.LoginActivity;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.RespQueOrNote;
import com.efei.lib.android.biz_remote_interface.IAccountService;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService;
import com.efei.lib.android.biz_remote_interface.IQueScanService;
import com.efei.lib.android.biz_remote_interface.IQueScanService.RespAddBatchQues;
import com.efei.lib.android.biz_remote_interface.IQueScanService.RespQueId;
import com.efei.lib.android.biz_remote_interface.IQueScanService.RespNoteId;
import com.efei.lib.android.engine.ILoginService;

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
				// Thread.sleep(1000);
//				RespQueId id = (RespQueId) IQueScanService.Factory.getService().get("~L7Yrv");
////				RespQueId que = (RespQueId) IQueScanService.Factory.getService().get0student$questions(id.getQuestion_id());
//				IQueScanService.Factory.getService().post0student$note(id.getQuestion_id(), "≤‚ ‘tag", "≤‚ ‘topics", "≤‚ ‘summary");
				
//				IQueScanService service = IQueScanService.Factory.getService();
//				RespQueId id1 = service.get("~cYB88");
//				RespQueId id2 = service.get("~v6OjH");
//				RespAddBatchQues ques = service.post0student$notes$batch(id1.getQuestion_id() , id2.getQuestion_id());
				
				IAccountService service = IAccountService.Factory.getService();
				BaseRespBean post0account$registrations = service.put0account$passwords("654321", "78r-fgbTE87WlsOk7tB9uidat_3onbU-Hw5tmVg5Gp2LlL02l90MsRyt0xQ-i7C-");
				
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
