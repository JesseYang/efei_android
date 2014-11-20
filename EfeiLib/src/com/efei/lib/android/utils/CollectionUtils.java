package com.efei.lib.android.utils;

import java.util.Collection;

public final class CollectionUtils
{
	private CollectionUtils()
	{
	}

	public static boolean isEmpty(Collection<?> collection)
	{
		return null == collection || collection.isEmpty();
	}
}
