package com.efei.lib.android.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public final class CommonUtils
{
	private CommonUtils()
	{
	}

	public static String getCurrentApkVersion(Context context)
	{
		try
		{
			final PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
			return "";
		}
	}
}
