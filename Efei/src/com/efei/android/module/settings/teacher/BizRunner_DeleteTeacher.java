package com.efei.android.module.settings.teacher;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.common_data.Teacher;
import com.efei.lib.android.biz_remote_interface.ISettingService;

class BizRunner_DeleteTeacher implements IBusinessCallback<BaseRespBean>
{
	private Teacher teacher;

	public BizRunner_DeleteTeacher(Teacher teacher)
	{
		this.teacher = teacher;
	}

	@Override
	public BaseRespBean onBusinessLogic(IJob job) throws Exception
	{
		return ISettingService.Factory.getService().delete0student$teachers(teacher.getId());
	}

}
