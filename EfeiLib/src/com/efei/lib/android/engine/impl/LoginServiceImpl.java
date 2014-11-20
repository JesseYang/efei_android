package com.efei.lib.android.engine.impl;

import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.ReqLogin;
import com.efei.lib.android.bean.net.ReqRegister;
import com.efei.lib.android.bean.net.RespLogin;
import com.efei.lib.android.engine.ILoginService;
import com.efei.lib.android.utils.NetUtils;

public class LoginServiceImpl implements ILoginService {
	private static final String URL_API_LOGIN = "account/sessions";
	private static final String URL_API_REGISTER = "account/registrations";
	
	LoginServiceImpl()
	{
	}

	@Override
	public IJob login(final ReqLogin reqLogin, IUICallback<RespLogin> uiCallback) {
		return Executor.INSTANCE.execute(new JobAsyncTask<RespLogin>(
				new IBusinessCallback<RespLogin>() {

					@Override
					public RespLogin onBusinessLogic(IJob job) {
						return NetUtils.postObjectAsJson(URL_API_LOGIN,
								reqLogin, RespLogin.class);
					}
				}, uiCallback));
	}

	@Override
	public IJob register(final ReqRegister reqRegister,
			IUICallback<RespLogin> uiCallback) {
		return Executor.INSTANCE.execute(new JobAsyncTask<RespLogin>(
				new IBusinessCallback<RespLogin>() {

					@Override
					public RespLogin onBusinessLogic(IJob job) {
						return NetUtils.postObjectAsJson(URL_API_REGISTER,
								reqRegister, RespLogin.class);
					}
				}, uiCallback));
	}

}
