package com.efei.android.module.settings.teacher;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.biz_remote_interface.ISettingService;
import com.efei.lib.android.biz_remote_interface.ISettingService.RespSearchTeachers;

class BizRunner_GetMyTeachers implements IBusinessCallback<RespSearchTeachers>
{

	@Override
	public RespSearchTeachers onBusinessLogic(IJob job) throws Exception
	{
		RespSearchTeachers teachers = ISettingService.Factory.getService().get0student$teachers();
		return teachers;
	}

}
