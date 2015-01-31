package com.efei.lib.android.engine;

import java.util.Date;

import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.persistance.Account;
import com.efei.lib.android.biz_remote_interface.IAccountService;
import com.efei.lib.android.exception.EfeiException;
import com.efei.lib.android.exception.KnownEfeiExcepiton;
import com.efei.lib.android.utils.DBManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;

class LoginServiceImpl implements ILoginService
{

	LoginServiceImpl()
	{
	}

	@Override
	public Account register(String email_mobile, String password, String name)
	{
		IAccountService service = IAccountService.Factory.getService();
		BaseRespBean respBean = service.post0account$registrations(email_mobile, password, name);
		return createOrUpdateLocalAccount(email_mobile, respBean);
	}

	@Override
	public Account getDefaultUser()
	{
		try
		{
			Dao<Account, String> dao = DBManager.beginSession(Account.class);
			return dao.queryBuilder().orderBy(Account.Properties.LastLoginTime, false).queryForFirst();
		} catch (Throwable e)
		{
			DBManager.endSession();
			return null;
		}
	}

	@Override
	public Account login(String email_mobile, String password)
	{
		IAccountService service = IAccountService.Factory.getService();
		BaseRespBean respBean = service.post0account$sessions(email_mobile, password);
		return createOrUpdateLocalAccount(email_mobile, respBean);
	}

	private Account createOrUpdateLocalAccount(String email_mobile, BaseRespBean respBean)
	{
		if (!respBean.isSuccess())
			throw new KnownEfeiExcepiton(respBean.getCode());
		try
		{
			Account account = new Account(email_mobile);
			account.setAuthKey(respBean.getAuth_key());
			account.setLastLoginTime(new Date());
			Dao<Account, String> dao = DBManager.beginSession(Account.class);
			CreateOrUpdateStatus status = dao.createOrUpdate(account);
			if (status.getNumLinesChanged() != 1)
				throw new EfeiException("can't save account:" + email_mobile);
			return account;
		} catch (Throwable e)
		{
			throw new EfeiException(e);
		} finally
		{
			DBManager.endSession();
		}
	}

	@Override
	public void logout()
	{
		try
		{
			Dao<Account, String> dao = DBManager.beginSession(Account.class);
			int delete = dao.delete(getDefaultUser());
			if (1 != delete)
				throw new EfeiException("can't delete account");
		} catch (Throwable e)
		{
			throw new EfeiException(e);
		} finally
		{
			DBManager.endSession();
		}
	}

}
