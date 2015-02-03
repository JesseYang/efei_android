package com.efei.android.module.settings.teacher;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.biz_remote_interface.ISettingService;
import com.efei.lib.android.biz_remote_interface.ISettingService.RespSearchTeachers;

class BizRunner_SearchTeachers implements IBusinessCallback<RespSearchTeachers>
{

	private String name;
	
	public BizRunner_SearchTeachers(String name)
	{
		this.name = name;
	}

	@Override
	public RespSearchTeachers onBusinessLogic(IJob job) throws Exception
	{
		return ISettingService.Factory.getService().get0student$teachers(0, name);
	}

}
