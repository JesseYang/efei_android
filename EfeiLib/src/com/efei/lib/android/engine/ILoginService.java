package com.efei.lib.android.engine;

import com.efei.lib.android.bean.persistance.Account;

public interface ILoginService
{
	Account login(String email_mobile, String password) throws Exception;

	Account register(String email_mobile, String password, String name);

	void logout();

	Account getDefaultUser();

	public static class Factory
	{
		private Factory()
		{
		}

		private static ILoginService service;

		public synchronized static ILoginService getService()
		{
			if (null == service)
				service = new LoginServiceImpl();
			return service;
		}
	}

}
