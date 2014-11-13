package com.efei.lib.android.bean.net;

public final class RespLogin {
	private int code;
	private String auth_key;

	public int getCode() {
		return code;
	}

	void setCode(int code) {
		this.code = code;
	}

	public String getAuth_key() {
		return auth_key;
	}

	void setAuth_key(String auth_key) {
		this.auth_key = auth_key;
	}
}
