package com.efei.lib.android.bean.net;

public abstract class ABaseRespBean {
	private boolean success;
	private int code;

	public boolean isSuccess() {
		return success;
	}

	void setSuccess(boolean success) {
		this.success = success;
	}

	public int getCode() {
		return code;
	}

	void setCode(int code) {
		this.code = code;
	}
}
