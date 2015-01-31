package com.efei.android.module.account;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.bean.persistance.Account;
import com.efei.lib.android.engine.ILoginService;

final class BizRunner_Login implements IBusinessCallback<Account>
{
	private String email_mobile;
	private String password;

	public BizRunner_Login(String email_mobile, String password)
	{
		this.email_mobile = email_mobile;
		this.password = password;
	}

	@Override
	public Account onBusinessLogic(IJob job) throws Exception
	{
		return ILoginService.Factory.getService().login(email_mobile, password);
	}
}
