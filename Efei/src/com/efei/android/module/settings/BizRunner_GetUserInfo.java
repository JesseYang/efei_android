package com.efei.android.module.settings;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.biz_remote_interface.ISettingService;
import com.efei.lib.android.biz_remote_interface.ISettingService.RespStudentInfo;

final class BizRunner_GetUserInfo implements IBusinessCallback<RespStudentInfo>
{
	@Override
	public RespStudentInfo onBusinessLogic(IJob job)
	{
		ISettingService service = ISettingService.Factory.getService();
		return service.get0student$students$info();
	}
}
