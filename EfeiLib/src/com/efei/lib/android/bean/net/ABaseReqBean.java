package com.efei.lib.android.bean.net;

import com.efei.lib.android.exception.EfeiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ABaseReqBean {
	private String client;
	private String auth_key;

	public ABaseReqBean() {
		this.client = "test_client_android_version";
		this.auth_key = "test_auth_key";
	}

	public String getClient() {
		return client;
	}

	public String getAuth_key() {
		return auth_key;
	}

	public void setAuth_key(String auth_key) {
		this.auth_key = auth_key;
	}

	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new EfeiException(e);
		}
	}

}
