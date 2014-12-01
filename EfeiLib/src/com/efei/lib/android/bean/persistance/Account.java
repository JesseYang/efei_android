package com.efei.lib.android.bean.persistance;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "T_ACCOUNT")
public final class Account
{
	public static final class Properties
	{
		private Properties()
		{
		};

		public static final String Email_Mobile = "email_mobile";
		public static final String AuthKey = "auth_key";
		public static final String LastLoginTime = "last_login_time";
	}

	@DatabaseField(id = true, canBeNull = true, columnName = Properties.Email_Mobile)
	private String email_mobile;
	@DatabaseField(columnName = Properties.AuthKey)
	private String authKey;
	@DatabaseField(columnName = Properties.LastLoginTime)
	private Date lastLoginTime;

	private transient String password;

	Account()
	{
	}

	public Account(String emailMobile)
	{
		this.email_mobile = emailMobile;
	}

	public String getEmail_mobile()
	{
		return email_mobile;
	}

	public void setEmail_mobile(String email_mobile)
	{
		this.email_mobile = email_mobile;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Date getLastLoginTime()
	{
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime)
	{
		this.lastLoginTime = lastLoginTime;
	}

	public String getAuthKey()
	{
		return authKey;
	}

	public void setAuthKey(String authKey)
	{
		this.authKey = authKey;
	}

}
