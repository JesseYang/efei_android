package com.efei.android.module;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.android.module.list.QueListFragment;
import com.efei.android.module.scan.ScanActivity;
import com.efei.android.module.settings.SettingsFragment;
import com.efei.lib.android.biz_remote_interface.ISettingService.RespLatestVersion;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.utils.CommonUtils;

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

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu)
	// {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.que_list, menu);
	// return true;
	// }

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
					getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container, new QueListFragment())
							.commitAllowingStateLoss();
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
					getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container, new SettingsFragment())
							.commitAllowingStateLoss();
				}
			});

			viewBar.findViewById(R.id.ll_quelist).performClick();

			EfeiApplication app = (EfeiApplication) getApplication();
			final RespLatestVersion version = app.removeTemporary(Constants.KEY_LATEST_VERSION);
			app.addTemporary(Constants.KEY_LATEST_VERSION, version);
			if (null != version)
			{
				String curVersion = CommonUtils.getCurrentApkVersion(MainActivity.this);
				final int visibility = version.getAndroid().equals(curVersion) ? View.GONE : View.VISIBLE;
				viewBar.findViewById(R.id.iv_red_point).setVisibility(visibility);
			} else
				viewBar.findViewById(R.id.iv_red_point).setVisibility(View.GONE);
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
