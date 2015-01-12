package com.efei.lib.android.bean.persistance;

import android.text.Spannable;

import com.efei.lib.android.bean.net.RespQueOrNote;
import com.efei.lib.android.utils.UiUtils;

public class QuestionOrNote2
{
	public final RespQueOrNote metaData;
	public final Spannable content;

	public QuestionOrNote2(RespQueOrNote respQueOrNote)
	{
		this.metaData = respQueOrNote;
		this.content = UiUtils.richTextToSpannable(metaData.getContent());
	}
}
