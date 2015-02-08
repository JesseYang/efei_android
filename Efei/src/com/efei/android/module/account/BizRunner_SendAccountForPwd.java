package com.efei.android.module.account;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.biz_remote_interface.IAccountService;

final class BizRunner_SendAccountForPwd implements IBusinessCallback<BaseRespBean>
{

	private String email_mobile;

	public BizRunner_SendAccountForPwd(String email_mobile)
	{
		this.email_mobile = email_mobile;
	}

	@Override
	public BaseRespBean onBusinessLogic(IJob job) throws Exception
	{
		return IAccountService.Factory.getService().post0account$passwords(email_mobile);
	}
}