package com.efei.lib.android.bean.net;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ABaseRespBean
{
	private boolean success;
	private int code;
	private String auth_key;

	public boolean isSuccess()
	{
		return success;
	}

	void setSuccess(boolean success)
	{
		this.success = success;
	}

	public int getCode()
	{
		return code;
	}

	void setCode(int code)
	{
		this.code = code;
	}

	public String getAuth_key()
	{
		return auth_key;
	}

	void setAuth_key(String auth_key)
	{
		this.auth_key = auth_key;
	}

	public static <T> T toObject(String json, Class<T> clazz)
	{
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return mapper.readValue(json, clazz);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

}
