package com.efei.lib.android.biz_remote_interface;

import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.utils.NetUtils;

class AccountImpl implements IAccountService
{
	AccountImpl()
	{
	}

	@Override
	public BaseRespBean post0account$sessions(String email_mobile, String password)
	{
		final String API_URL = "account/sessions";
		ReqLogin req = new ReqLogin();
		req.setEmail_mobile(email_mobile);
		req.setPassword(password);
		return NetUtils.postObjectAsJson(API_URL, req, BaseRespBean.class);
	}

	@Override
	public BaseRespBean post0account$registrations(String email_mobile, String password, String name)
	{
		final String API_URL = "account/registrations";
		ReqRegister req = new ReqRegister();
		req.setEmail_mobile(email_mobile);
		req.setPassword(password);
		req.setName(name);
		return NetUtils.postObjectAsJson(API_URL, req, BaseRespBean.class);
	}

	@Override
	public BaseRespBean post0account$passwords(String email_mobile)
	{
		final String API_URL = "account/passwords";
		ReqLogin req = new ReqLogin();
		req.setEmail_mobile(email_mobile);
		return NetUtils.postObjectAsJson(API_URL, req, BaseRespBean.class);
	}

	@Override
	public RespVerifyPwd post0account$passwords$verify_code(String mobile, String verify_code)
	{
		final String API_URL = "account/passwords/verify_code";
		ReqVerifyPwd req = new ReqVerifyPwd();
		req.setMobile(mobile);
		req.setVerify_code(verify_code);
		return NetUtils.postObjectAsJson(API_URL, req, RespVerifyPwd.class);
	}

	@Override
	public BaseRespBean put0account$passwords(String password, String reset_password_token)
	{
		final String API_URL = "account/passwords";
		ReqResetPwd req = new ReqResetPwd();
		req.setPassword(password);
		req.setReset_password_token(reset_password_token);
		return NetUtils.put(API_URL, req, BaseRespBean.class);
	}
}
