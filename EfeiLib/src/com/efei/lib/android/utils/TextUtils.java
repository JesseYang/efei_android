package com.efei.lib.android.utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextUtils
{
	private TextUtils()
	{
	}

	public static boolean isBlank(CharSequence string)
	{
		int strLen;
		if (string == null || (strLen = string.length()) == 0)
			return true;

		for (int i = 0; i < strLen; i++)
			if (!Character.isWhitespace(string.charAt(i)))
				return false;

		return true;
	}

	/**
	 * Returns true if the string is null or 0-length.
	 * 
	 * @param string
	 *                the string to be examined
	 * @return true if str is null or zero length
	 */
	public static boolean isEmpty(CharSequence string)
	{
		if (string == null || string.length() == 0)
			return true;
		else
			return false;
	}

	public static String concat(List<String> subStrings)
	{
		StringBuilder sb = new StringBuilder();
		for (String string : subStrings)
			sb.append(string);
		return sb.toString();
	}

	public static boolean isMobilePhoneNumber(String mobiles)
	{
		/*
		 * �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188 ��ͨ��130��131��132��152��155��156��185��186 ���ţ�133��153��180��189����1349��ͨ��
		 * �ܽ��������ǵ�һλ�ض�Ϊ1���ڶ�λ�ض�Ϊ3��5��8������λ�õĿ���Ϊ0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"�����1λΪ����1��"[358]"����ڶ�λ����Ϊ3��5��8�е�һ����"\\d{9}"��������ǿ�����0��9�����֣���9λ��
		if (isBlank(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	public static boolean isValidatePassword(String password)
	{
		if (isBlank(password))
			return false;
		return password.length() >= 6 && password.length() <= 16;
	}

	/**
	 * ��������ַ�Ƿ�Ϸ�
	 * 
	 * @param email
	 * @return true�Ϸ� false���Ϸ�
	 */
	public static boolean isEmail(String email)
	{
		if (isBlank(email))
			return false;
		// Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //��ƥ��
		Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// ����ƥ��
		Matcher m = p.matcher(email);
		return m.matches();
	}
}
