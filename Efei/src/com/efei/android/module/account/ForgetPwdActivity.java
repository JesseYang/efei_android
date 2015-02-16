package com.efei.android.module.account;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.efei.android.R;

public class ForgetPwdActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_pwdl);
		getSupportActionBar().setTitle("’“ªÿ√‹¬Î");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container, new ForgetPwd_FillAccountFragment()).commitAllowingStateLoss();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (android.R.id.home == item.getItemId())
			finish();
		return super.onOptionsItemSelected(item);
	}

}
