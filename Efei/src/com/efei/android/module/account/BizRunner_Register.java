package com.efei.android.module.account;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.bean.persistance.Account;
import com.efei.lib.android.engine.ILoginService;

final class BizRunner_Register implements IBusinessCallback<Account>
{
	private String email_mobile;
	private String password;
	private String name;

	public BizRunner_Register(String email_mobile, String password, String name)
	{
		this.email_mobile = email_mobile;
		this.password = password;
		this.name = name;
	}

	@Override
	public Account onBusinessLogic(IJob job)
	{
		return ILoginService.Factory.getService().register(email_mobile, password, name);
	}
}
