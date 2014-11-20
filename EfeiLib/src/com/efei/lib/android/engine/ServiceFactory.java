package com.efei.lib.android.engine;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import com.efei.lib.android.exception.EfeiException;

public enum ServiceFactory
{
	INSTANCE;

	public static final String LOGIN_SERVICE = "login_service";

	private final Map<String, SoftReference<Object>> serviceCache = new HashMap<String, SoftReference<Object>>();

	@SuppressWarnings("unchecked")
	public <T> T getService(String serviceName)
	{
		if (null == serviceName || serviceName.trim().equals(""))
			return null;
		SoftReference<Object> softObj = serviceCache.get(serviceName);
		if (null == softObj || null == softObj.get())
		{
			if (serviceName.equals(LOGIN_SERVICE))
			{
				ILoginService service = createLoginService();
				serviceCache.put(serviceName, new SoftReference<Object>(service));
			} else
				throw new EfeiException("no service---" + serviceName);
		}
		return (T) serviceCache.get(serviceName).get();
	}

	private ILoginService createLoginService()
	{
		try
		{
			return (ILoginService) Class.forName("com.efei.lib.android.engine.impl.LoginServiceImpl").newInstance();
		} catch (Exception e)
		{
			throw new EfeiException(e);
		}
	}
}
