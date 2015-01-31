package com.efei.android.module.settings.me;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.biz_remote_interface.ISettingService;

final class BizRunner_ModifyEmail implements IBusinessCallback<BaseRespBean>
{
	private String email;

	public BizRunner_ModifyEmail(String email)
	{
		this.email = email;
	}

	@Override
	public BaseRespBean onBusinessLogic(IJob job)
	{
		return ISettingService.Factory.getService().put0student$students$change_email(email.toString());
	}
}