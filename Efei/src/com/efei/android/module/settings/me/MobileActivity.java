package com.efei.android.module.settings.me;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.efei.android.R;
import com.efei.lib.android.utils.TextUtils;

public class MobileActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me_mobile);
		String mobile = getIntent().getStringExtra(SettingsMeActivity.KEY_MOBILE);
		if (TextUtils.isBlank(mobile))
			getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container, new Mobile_PhoneCodeFragment(mobile)).commit();
		else
			getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_container, new Mobile_CompleteFragment(mobile)).commit();
		getSupportActionBar().setTitle(" ÷ª˙…Ë÷√");
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
