package com.efei.lib.android.repository;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.efei.lib.android.bean.persistance.Account;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.exception.EfeiException;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

final class DBManager
{
	private DBManager()
	{
	}

	static <T> Dao<T, Long> beginSession(Class<T> beanClazz)
	{
		ConnectionSource connectionSource = OpenHelperManager.getHelper(EfeiApplication.getContext(), EfeiSqliteOpenHelper.class).getConnectionSource();

		try
		{
			// instantiate the DAO to handle beanClazz with Long id
			return DaoManager.createDao(connectionSource, beanClazz);
		} catch (SQLException e)
		{
			endSession();
			throw new EfeiException(e);
		}
	}

	static void endSession()
	{
		OpenHelperManager.releaseHelper();
	}

	private static class EfeiSqliteOpenHelper extends OrmLiteSqliteOpenHelper
	{

		public EfeiSqliteOpenHelper(Context context)
		{
			super(context, "efei-db", null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
		{
			try
			{
				TableUtils.createTableIfNotExists(connectionSource, Account.class);
			} catch (SQLException e)
			{
				throw new EfeiException(e);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion)
		{
			// TODO Auto-generated method stub

		}

	}
}
