package com.efei.lib.android.repository;

import java.lang.ref.SoftReference;
import java.sql.SQLException;
import java.util.List;

import com.efei.lib.android.bean.persistance.Account;
import com.efei.lib.android.exception.EfeiException;
import com.efei.lib.android.utils.CollectionUtils;
import com.j256.ormlite.dao.Dao;

public final class AccountLocalRepo
{
	private static final String TAG = AccountLocalRepo.class.getSimpleName();

	private AccountLocalRepo()
	{
	}

	public void createOrUpdate(final Account account)
	{
		execute(new DBExecutor<Void>()
		{
			@Override
			public Void execute(Dao<Account, Long> dao) throws SQLException
			{
				if (dao.createOrUpdate(account).getNumLinesChanged() != 1)
					throw new EfeiException("create " + account + "in " + TAG + "failed");
				return null;
			}
		});
	}

	public Account queryForEq(final String accountProperties , final Object value)
	{
		return execute(new DBExecutor<Account>()
		{
			@Override
			public Account execute(Dao<Account, Long> dao) throws SQLException
			{
				List<Account> accounts = dao.queryForEq(accountProperties, value);
				if (CollectionUtils.isEmpty(accounts))
					return null;
				return accounts.get(0);
			}
		});
	}

	public List<Account> loadAllOrderBy(final String accountProperties, final boolean ascending)
	{
		return execute(new DBExecutor<List<Account>>()
		{
			@Override
			public List<Account> execute(Dao<Account, Long> dao) throws SQLException
			{
				return dao.queryBuilder().orderBy(accountProperties, ascending).query();
			}
		});
	}

	private <T> T execute(DBExecutor<T> executor)
	{
		Dao<Account, Long> dao = DBManager.beginSession(Account.class);
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

	private static interface DBExecutor<T>
	{
		T execute(Dao<Account, Long> dao) throws SQLException;
	}

	private static SoftReference<AccountLocalRepo> repo;

	public static AccountLocalRepo getInstance()
	{
		if (null == repo || null == repo.get())
		{
			AccountLocalRepo repoResult = new AccountLocalRepo();
			repo = new SoftReference<AccountLocalRepo>(repoResult);
			return repoResult;
		}

		return repo.get();
	}

}
