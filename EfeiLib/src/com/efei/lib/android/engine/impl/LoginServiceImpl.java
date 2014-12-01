package com.efei.lib.android.engine.impl;

import java.util.Date;
import java.util.List;

import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.ReqLogin;
import com.efei.lib.android.bean.net.ReqRegister;
import com.efei.lib.android.bean.net.RespLogin;
import com.efei.lib.android.bean.persistance.Account;
import com.efei.lib.android.engine.ILoginService;
import com.efei.lib.android.repository.AccountLocalRepo;
import com.efei.lib.android.utils.CollectionUtils;
import com.efei.lib.android.utils.NetUtils;
import com.efei.lib.android.utils.TextUtils;

class LoginServiceImpl implements ILoginService
{
	private static final String URL_API_LOGIN = "account/sessions";
	private static final String URL_API_REGISTER = "account/registrations";

	LoginServiceImpl()
	{
	}

	@Override
	public IJob login(final ReqLogin reqLogin, IUICallback<RespLogin> uiCallback)
	{
		return Executor.INSTANCE.execute(new JobAsyncTask<RespLogin>(new IBusinessCallback<RespLogin>()
		{

			@Override
			public RespLogin onBusinessLogic(IJob job)
			{
				RespLogin respLogin = NetUtils.postObjectAsJson(URL_API_LOGIN, reqLogin, RespLogin.class);
				if (respLogin.isSuccess())
					// if login succeeds , we will record the user locally as default_user for next time
					createOrUpdateDefaultUser(reqLogin, respLogin);
				return respLogin;
			}
		}, uiCallback));
	}

	@Override
	public IJob register(final ReqRegister reqRegister, IUICallback<RespLogin> uiCallback)
	{
		return Executor.INSTANCE.execute(new JobAsyncTask<RespLogin>(new IBusinessCallback<RespLogin>()
		{

			@Override
			public RespLogin onBusinessLogic(IJob job)
			{
				// TODO yunzhong: maybe cache in local
				return NetUtils.postObjectAsJson(URL_API_REGISTER, reqRegister, RespLogin.class);
			}
		}, uiCallback));
	}

	@Override
	public Account getDefaultUser()
	{
		//TODO the default user may be cached in memory
		AccountLocalRepo repo = AccountLocalRepo.getInstance();
		List<Account> accounts = repo.loadAllOrderBy(Account.Properties.LastLoginTime, false);
		if (!CollectionUtils.isEmpty(accounts) && !TextUtils.isBlank(accounts.get(0).getAuthKey()))
			// last login user regard as default user
			return accounts.get(0);
		// no user in local or empty auth_key, we have to show login form to make user fill in login info;
		// if login succeeds , we will record the user locally as default_user;
		return null;
	}

	// regard the user who logged in latest as default user
	private void createOrUpdateDefaultUser(ReqLogin reqLogin, RespLogin respLogin)
	{
		AccountLocalRepo repo = AccountLocalRepo.getInstance();
		Account account = repo.queryForEq(Account.Properties.Email_Mobile, reqLogin.getEmail_mobile());
		if (null == account)
		{
			account = new Account(reqLogin.getEmail_mobile());
			account.setPassword(reqLogin.getPassword());
			account.setAuthKey(respLogin.getAuth_key());
			account.setLastLoginTime(new Date());
		} else
			account.setLastLoginTime(new Date());
		repo.createOrUpdate(account);
	}

}
