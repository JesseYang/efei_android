package com.efei.lib.android.persistence.greendao;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.persistence.greendao.dao.DaoMaster;
import com.efei.lib.android.persistence.greendao.dao.DaoMaster.DevOpenHelper;
import com.efei.lib.android.persistence.greendao.dao.DaoMaster.OpenHelper;
import com.efei.lib.android.persistence.greendao.dao.DaoSession;

public final class DBManager
{
	private static Map<DaoSession, OpenHelper> openedSessions = new HashMap<DaoSession, DaoMaster.OpenHelper>();

	private DBManager()
	{
	}

	public static OpenHelper getDaoMaster(Context context)
	{
		return new DevOpenHelper(context, "efei-db", null);
	}

	public static DaoSession beginSession()
	{
		DevOpenHelper dbHelper = new DevOpenHelper(EfeiApplication.getContext(), "efei-db", null);
		DaoMaster daoMaster = new DaoMaster(dbHelper.getWritableDatabase());
		DaoSession newSession = daoMaster.newSession();
		openedSessions.put(newSession, dbHelper);
		return newSession;
	}

	public static void endSession(DaoSession session)
	{
		OpenHelper dbHelper = openedSessions.remove(session);
		if (null != dbHelper)
			dbHelper.close();
	}
}
