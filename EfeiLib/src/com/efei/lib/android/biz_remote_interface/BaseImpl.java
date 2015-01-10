package com.efei.lib.android.biz_remote_interface;

import java.util.Map;

import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.exception.EfeiException;
import com.efei.lib.android.utils.NetUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

class BaseImpl
{
	ObjectMapper newMapper()
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper;
	}

	<T extends BaseRespBean> T get(String API_URL, Map<String, String> params, Class<T> clazzResp)
	{
		try
		{
			String json = NetUtils.get(API_URL, params);
			ObjectMapper mapper = newMapper();
			return mapper.readValue(json, clazzResp);
		} catch (Exception e)
		{
			throw new EfeiException(e);
		}
	}

	<T extends BaseRespBean> T delete(String API_URL, Map<String, String> params, Class<T> clazzResp)
	{
		try
		{
			String json = NetUtils.delete(API_URL, params);
			ObjectMapper mapper = newMapper();
			return mapper.readValue(json, clazzResp);
		} catch (Exception e)
		{
			throw new EfeiException(e);
		}
	}
}
