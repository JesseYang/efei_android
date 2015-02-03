package com.efei.android.module.settings.teacher;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.common_data.Teacher;
import com.efei.lib.android.bean.net.common_data.Teacher.Classs;
import com.efei.lib.android.biz_remote_interface.ISettingService;

class BizRunner_AddTeacher implements IBusinessCallback<BaseRespBean>
{
	private Teacher teacher;
	private Classs classs;

	public BizRunner_AddTeacher(Teacher teacher, Classs classs)
	{
		this.teacher = teacher;
		this.classs = classs;
	}

	@Override
	public BaseRespBean onBusinessLogic(IJob job) throws Exception
	{
		return ISettingService.Factory.getService().post0student$teachers(teacher.getId(), null == classs ? null : classs.getId());
	}

}
