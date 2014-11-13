package com.efei.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.bean.net.ReqLogin;
import com.efei.lib.android.bean.net.ReqRegister;
import com.efei.lib.android.bean.net.RespLogin;
import com.efei.lib.android.engine.impl.LoginServiceImpl;

public class SplashActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
//		ReqRegister reqRegister = new ReqRegister();
//		reqRegister.setEmail_mobile("642209019@qq.com");
//		reqRegister.setPassword("123456");
//		new LoginServiceImpl().register(reqRegister);
		
		ReqLogin reqLogin = new ReqLogin();
		reqLogin.setEmail_mobile("642209019@qq.com");
		reqLogin.setPassword("123456");
		new LoginServiceImpl().login(reqLogin , new IUICallback<RespLogin>() {
			
			@Override
			public void onProgressUpdate(int percent, Object... params) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPreExecute() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPostExecute(RespLogin result) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCancelled() {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
