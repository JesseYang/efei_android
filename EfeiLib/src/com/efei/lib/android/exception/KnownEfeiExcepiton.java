package com.efei.lib.android.exception;

public class KnownEfeiExcepiton extends EfeiException
{
	private static final long serialVersionUID = 1L;

	public static enum Error
	{
		USER_EXIST(-3, "���û��Ѵ���"), //
		USER_NOT_EXIST(-1, "���û�������"), //
		WRONG_PASSWORD(-2, "�������"), //
		BLANK_EMAIL_MOBILE(-4, "��Ч�˻���"), //
		REQUIRE_SIGNIN(-8, "��Ҫ��¼��"), //
		WRONG_VERIFY_CODE(-5, "��֤�����"), //
		WRONG_TOKEN(-6, "sessionʧЧ"), //
		EXPIRED(-7, "session����"), //
		TEACHER_NOT_EXIST(-9, "�ý�ʦ������"), //
		QUESTION_NOT_EXIST(-10, "����Ŀ������"), //
		NOTE_NOT_EXIST(-11, "�ñʼǲ�����"), //

		SERVER_ERROR(500, "����������"), //

		CLIENT_NO_NET(1000, "��ǰ���绷������");

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
