package com.efei.lib.android.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class EfeiApplication extends Application
{
	private static EfeiApplication application;

	@Override
	public void onCreate()
	{
		super.onCreate();
		application = this;
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
		ImageLoader.getInstance().init(config);
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

	public static void switchToActivity(Class<? extends Activity> clazz)
	{
		Intent intent = new Intent(EfeiApplication.getContext(), clazz);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		EfeiApplication.getContext().startActivity(intent);
	}
}
