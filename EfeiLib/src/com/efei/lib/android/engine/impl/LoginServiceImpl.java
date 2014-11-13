package com.efei.lib.android.engine.impl;

import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.ReqLogin;
import com.efei.lib.android.bean.net.RespLogin;
import com.efei.lib.android.engine.ILoginService;
import com.efei.lib.android.utils.NetUtils;

public class LoginServiceImpl implements ILoginService {
	private static final String URL_API_LOGIN = "account/sessions";

	@Override
	public IJob login(final ReqLogin reqLogin) {
		return Executor.INSTANCE.execute(new JobAsyncTask<RespLogin>(
				new IBusinessCallback<RespLogin>() {

					@Override
					public RespLogin onBusinessLogic(IJob job) {
						return NetUtils.postObjectAsJson(URL_API_LOGIN,
								reqLogin, RespLogin.class);
					}
				}, new IUICallback<RespLogin>() {

					@Override
					public void onPreExecute() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPostExecute(RespLogin result) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressUpdate(int percent, Object... params) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onCancelled() {
						// TODO Auto-generated method stub

					}
				}));
	}

}
