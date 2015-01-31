package com.efei.lib.android.biz_remote_interface;

import java.io.IOException;
import java.util.Map;

import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.exception.EfeiException;
import com.efei.lib.android.exception.KnownEfeiExcepiton;
import com.efei.lib.android.utils.NetUtils;

class BaseImpl
{
	<T extends BaseRespBean> T get(String API_URL, Map<String, String> params, Class<T> clazzResp)
	{
		try
		{
			String json = NetUtils.get(API_URL, params);
			T res = BaseRespBean.toObject(json, clazzResp);
			if (!res.isSuccess())
				throw new KnownEfeiExcepiton(res.getCode());
			return res;
		} catch (IOException e)
		{
			throw new EfeiException(e);
		}
	}

	<T extends BaseRespBean> T delete(String API_URL, Map<String, String> params, Class<T> clazzResp)
	{
		try
		{
			String json = NetUtils.delete(API_URL, params);
			T res = BaseRespBean.toObject(json, clazzResp);
			if (!res.isSuccess())
				throw new KnownEfeiExcepiton(res.getCode());
			return res;
		} catch (IOException e)
		{
			throw new EfeiException(e);
		}
	}
}
