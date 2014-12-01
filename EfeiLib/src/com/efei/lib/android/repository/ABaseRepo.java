package com.efei.lib.android.repository;

import java.sql.SQLException;
import java.util.List;

import com.efei.lib.android.exception.EfeiException;
import com.efei.lib.android.utils.CollectionUtils;
import com.j256.ormlite.dao.Dao;

class ABaseRepo<RepoBean>
{
	final <ExeResult> ExeResult execute(DBExecutor<ExeResult, RepoBean> executor, Class<RepoBean> clazzBean)
	{
		Dao<RepoBean, String> dao = DBManager.beginSession(clazzBean);
		try
		{
			return executor.execute(dao);
		} catch (SQLException e)
		{
			throw new EfeiException(e);
		} finally
		{
			DBManager.endSession();
		}
	}

	static interface DBExecutor<ExeResult, RepoBean>
	{
		ExeResult execute(Dao<RepoBean, String> dao) throws SQLException;
	}

	static class QueryByFieldExecutor<RepoBean> implements DBExecutor<RepoBean, RepoBean>
	{
		private String beanProperties;
		private String queryValue;

		QueryByFieldExecutor(String beanProperties, String queryValue)
		{
			this.beanProperties = beanProperties;
			this.queryValue = queryValue;
		}

		@Override
		public RepoBean execute(Dao<RepoBean, String> dao) throws SQLException
		{
			List<RepoBean> accounts = dao.queryForEq(beanProperties, queryValue);
			if (CollectionUtils.isEmpty(accounts))
				return null;
			return accounts.get(0);
		}
	}

}
