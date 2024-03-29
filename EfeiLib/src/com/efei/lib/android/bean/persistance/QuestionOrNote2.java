package com.efei.lib.android.bean.persistance;

import java.util.ArrayList;
import java.util.List;

import android.text.Spannable;

import com.efei.lib.android.bean.net.RespQueOrNote;
import com.efei.lib.android.utils.CollectionUtils;
import com.efei.lib.android.utils.UiUtils;

public class QuestionOrNote2
{
	public final RespQueOrNote metaData;
	public final String homework_id;
	public final Spannable content;
	public final Spannable answer;
	public final boolean alreadyInServer;

	public QuestionOrNote2(RespQueOrNote respQueOrNote, String homework_id, boolean alreadyInServer)
	{
		this.metaData = respQueOrNote;
		this.homework_id = homework_id;
		List<String> question = metaData.getContent();
		List<String> choices = metaData.getItems();
		List<String> content = new ArrayList<String>();
		content.addAll(question);
		for (int i = 0; !CollectionUtils.isEmpty(choices) && i < choices.size(); i++)
		{
			final char prefix = (char) ('A' + i);
			content.add(prefix + "." + choices.get(i));
		}
		this.content = UiUtils.richTextToSpannable(content, metaData.getImage_path());

		// if(-1 != respQueOrNote.getAnswer())
		List<String> answer = new ArrayList<String>();
		if (-1 != respQueOrNote.getAnswer())
		{
			final char ans_choice = (char) ('A' + respQueOrNote.getAnswer());
			answer.add(ans_choice + "");
		}
		answer.addAll(metaData.getAnswer_content());
		this.answer = UiUtils.richTextToSpannable(answer, metaData.getImage_path());
		this.alreadyInServer = alreadyInServer;
	}
}
