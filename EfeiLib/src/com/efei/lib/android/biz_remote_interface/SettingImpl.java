package com.efei.lib.android.biz_remote_interface;

import java.util.HashMap;
import java.util.Map;

import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.utils.NetUtils;



class SettingImpl extends BaseImpl implements ISettingService
{

	@Override
	public RespStudentInfo get0student$students$info()
	{
		final String API_URL = "student/students/info";
		return get(API_URL, null, RespStudentInfo.class);
	}

	@Override
	public BaseRespBean put0student$students$rename(String name)
	{
		final String API_URL = "student/students/rename";
		ReqRename req = new ReqRename();
		req.setName(name);
		return NetUtils.put(API_URL, req, BaseRespBean.class);
	}

	@Override
	public BaseRespBean put0student$students$change_password(String password, String new_password)
	{
		final String API_URL = "student/students/change_password";
		ReqChangePwd req = new ReqChangePwd();
		req.setPassword(password);
		req.setNew_password(new_password);
		return NetUtils.put(API_URL, req, BaseRespBean.class);
	}

	@Override
	public BaseRespBean put0student$students$change_email(String email)
	{
		final String API_URL = "student/students/change_email";
		ReqChangeEmail req = new ReqChangeEmail();
		req.setEmail(email);
		return NetUtils.put(API_URL, req, BaseRespBean.class);
	}

	@Override
	public BaseRespBean put0student$students$change_mobile(String mobile)
	{
		final String API_URL = "student/students/change_mobile";
		ReqChangeMobile req = new ReqChangeMobile();
		req.setMobile(mobile);
		return NetUtils.put(API_URL, req, BaseRespBean.class);
	}

	@Override
	public BaseRespBean post0student$students$verify_mobile(String verify_code)
	{
		final String API_URL = "student/students/verify_mobile";
		ReqVerifyMobile req = new ReqVerifyMobile();
		req.setVerify_code(verify_code);
		return NetUtils.postObjectAsJson(API_URL, req, BaseRespBean.class);
	}

	@Override
	public RespSearchTeachers get0student$teachers()
	{
		final String API_URL = "student/teachers";
		Map<String, String> params = new HashMap<String, String>();
		params.put("scope", 1 + "");
		return get(API_URL, params, RespSearchTeachers.class);
	}

	@Override
	public RespSearchTeachers get0student$teachers(int subject, String name)
	{
		final String API_URL = "student/teachers";
		Map<String, String> params = new HashMap<String, String>();
		params.put("scope", 0 + "");
		params.put("subject", subject + "");
		params.put("name", name);
		return get(API_URL, params, RespSearchTeachers.class);
	}

	@Override
	public BaseRespBean post0student$teachers(String teacher_id, String class_id)
	{
		final String API_URL = "student/teachers";
		ReqAddTeacher req = new ReqAddTeacher();
		req.setTeacher_id(teacher_id);
		req.setClass_id(class_id);
		return NetUtils.postObjectAsJson(API_URL, req, BaseRespBean.class);
	}

	@Override
	public BaseRespBean delete0student$teachers(String teacher_id)
	{
		final String API_URL = "student/teachers" + "/" + teacher_id;
		return delete(API_URL, null, BaseRespBean.class);
	}

	@Override
	public BaseRespBean post0student$feedbacks(String content)
	{
		final String API_URL = "student/feedbacks";
		ReqFeedBacks req = new ReqFeedBacks();
		req.setContent(content);
		return NetUtils.postObjectAsJson(API_URL, req, BaseRespBean.class);
	}

	@Override
	public RespLatestVersion get0latest$version()
	{
		final String API_URL = "welcome/app_version";
		return get(API_URL, null, RespLatestVersion.class);
	}

}
