package com.efei.android.module.settings.me;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.efei.android.R;
import com.efei.lib.android.utils.TextUtils;

public class EmailActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me_mobile_or_email);
		String email = getIntent().getStringExtra(SettingsMeActivity.KEY_EMAIL);
		if (TextUtils.isBlank(email))
			getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container, new Email_FillFragment(email)).commitAllowingStateLoss();
		else
			getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container, new Email_CompleteFragment(email)).commitAllowingStateLoss();
		getSupportActionBar().setTitle("” œ‰…Ë÷√");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (android.R.id.home == item.getItemId())
			finish();
		return super.onOptionsItemSelected(item);
	}

}
