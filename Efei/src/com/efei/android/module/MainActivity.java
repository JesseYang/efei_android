package com.efei.android.module;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.android.module.list.QueFragment;
import com.efei.android.module.scan.ScanActivity;
import com.efei.android.module.settings.SettingsFragment;
import com.efei.lib.android.common.EfeiApplication;

public class MainActivity extends ActionBarActivity
{
	private IndicatorBar barIndicator = new IndicatorBar();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupViews();
	}

	private void setupViews()
	{
		barIndicator.init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.que_list, menu);
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
			// TODO yunzhong:test tmp code
			EfeiApplication.switchToActivity(ScanActivity.class);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class IndicatorBar
	{
		private View viewBar;

		private void init()
		{
			viewBar = findViewById(R.id.bar_indicator);

			viewBar.findViewById(R.id.ll_scan).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					EfeiApplication.switchToActivity(ScanActivity.class);
				}
			});
			viewBar.findViewById(R.id.ll_quelist).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (viewBar.findViewById(R.id.iv_quelist).isSelected())
						return;
					uiRestore();
					((TextView) viewBar.findViewById(R.id.tv_quelist)).setTextColor(0xff4388ff);
					viewBar.findViewById(R.id.iv_quelist).setSelected(true);
					getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container, new QueFragment()).commit();
				}
			});
			viewBar.findViewById(R.id.ll_setting).setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (viewBar.findViewById(R.id.iv_setting).isSelected())
						return;
					uiRestore();
					((TextView) viewBar.findViewById(R.id.tv_setting)).setTextColor(0xff4388ff);
					viewBar.findViewById(R.id.iv_setting).setSelected(true);
					getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container, new SettingsFragment()).commit();
				}
			});

			viewBar.findViewById(R.id.ll_quelist).performClick();
		}

		private void uiRestore()
		{
			viewBar.findViewById(R.id.iv_scan).setSelected(false);
			viewBar.findViewById(R.id.iv_quelist).setSelected(false);
			viewBar.findViewById(R.id.iv_setting).setSelected(false);
			((TextView) viewBar.findViewById(R.id.tv_scan)).setTextColor(0xff757372);
			((TextView) viewBar.findViewById(R.id.tv_quelist)).setTextColor(0xff757372);
			((TextView) viewBar.findViewById(R.id.tv_setting)).setTextColor(0xff757372);
		}
	}

}
