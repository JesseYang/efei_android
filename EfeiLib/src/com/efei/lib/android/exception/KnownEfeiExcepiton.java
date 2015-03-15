package com.efei.lib.android.exception;

public class KnownEfeiExcepiton extends EfeiException
{
	private static final long serialVersionUID = 1L;

	public static enum Error
	{
		USER_EXIST(-3, "该用户已存在"), //
		USER_NOT_EXIST(-1, "该用户不存在"), //
		WRONG_PASSWORD(-2, "密码错误"), //
		BLANK_EMAIL_MOBILE(-4, "无效账户名"), //
		REQUIRE_SIGNIN(-8, "需要登录！"), //
		WRONG_VERIFY_CODE(-5, "验证码错误"), //
		WRONG_TOKEN(-6, "session失效"), //
		EXPIRED(-7, "session过期"), //
		TEACHER_NOT_EXIST(-9, "该教师不存在"), //
		QUESTION_NOT_EXIST(-10, "该题目不存在"), //
		NOTE_NOT_EXIST(-11, "该笔记不存在"), //

		SERVER_ERROR(500, "服务器故障"), //

		CLIENT_NO_NET(1000, "当前网络环境不好");

		private final int codeError;
		public final String desc;

		private Error(int codeError, String desc)
		{
			this.codeError = codeError;
			this.desc = desc;
		}

		private static Error getErrorByCode(int errorCode)
		{
			Error[] errors = Error.values();
			for (Error error : errors)
			{
				if (errorCode == error.codeError)
					return error;
			}
			throw new EfeiException("can't find error by code:" + errorCode);
		}

		@Override
		public String toString()
		{
			return desc;
		}

	}

	public final Error error;

	public KnownEfeiExcepiton(int errorCode)
	{
		this.error = Error.getErrorByCode(errorCode);
	}
}
