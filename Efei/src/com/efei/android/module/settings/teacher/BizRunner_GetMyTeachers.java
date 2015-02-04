package com.efei.android.module.settings.teacher;

import java.util.List;

import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.bean.net.common_data.Teacher;
import com.efei.lib.android.biz_remote_interface.ISettingService;
import com.efei.lib.android.biz_remote_interface.ISettingService.RespSearchTeachers;
import com.efei.lib.android.utils.CollectionUtils;

class BizRunner_GetMyTeachers implements IBusinessCallback<RespSearchTeachers>
{

	@Override
	public RespSearchTeachers onBusinessLogic(IJob job) throws Exception
	{
		RespSearchTeachers teachers = ISettingService.Factory.getService().get0student$teachers();
		List<Teacher> teachersList = teachers.getTeachers();
		if (!CollectionUtils.isEmpty(teachersList))
		{
			for (Teacher teacher : teachersList)
				MyTeacherActivity.MY_TEACHERS.put(teacher.getId(), teacher);
		}
		return teachers;
	}

}
