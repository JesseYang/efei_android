package com.efei.lib.android.bean.net;

public final class ReqRegister extends ABaseReqBean {
	private String email_mobile;
	private String password;
	private String name;

	public String getEmail_mobile() {
		return email_mobile;
	}

	public void setEmail_mobile(String email_mobile) {
		this.email_mobile = email_mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
