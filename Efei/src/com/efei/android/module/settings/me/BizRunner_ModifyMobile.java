package com.efei.android.module.settings.me;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.biz_remote_interface.ISettingService;

final class BizRunner_ModifyMobile implements IBusinessCallback<BaseRespBean>
{
	private String mobile;

	public BizRunner_ModifyMobile(String mobile)
	{
		this.mobile = mobile;
	}

	@Override
	public BaseRespBean onBusinessLogic(IJob job)
	{
		return ISettingService.Factory.getService().put0student$students$change_mobile(mobile.toString());
	}
}