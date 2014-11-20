package com.efei.lib.android.utils;

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
}
