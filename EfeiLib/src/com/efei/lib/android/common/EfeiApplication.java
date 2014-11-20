package com.efei.lib.android.common;

import android.app.Application;
import android.content.Context;

public class EfeiApplication extends Application
{
	private static EfeiApplication application;

	@Override
	public void onCreate()
	{
		super.onCreate();
		application = this;
	}

	@Override
	public void onTerminate()
	{
		application = null;
		super.onTerminate();
	}

	public static Context getContext()
	{
		return application;
	}
}
