package com.efei.android.module.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.exception.EfeiException;

public class TagTopicsEditActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_topics_edit);
		Intent intent = getIntent();
		String strEditContent = intent.getStringExtra(KEY_EDIT_CONTENT);
		EditContent editContent = EditContent.valueOf(strEditContent);
		EfeiApplication app = (EfeiApplication) getApplication();
		QuestionOrNote2 queOrNote = app.removeTemporary(Constants.TMP_QUE);
		switch (editContent)
		{
		case Tag:
			getSupportFragmentManager().beginTransaction().add(R.id.fl_container, new TagEditFragment(queOrNote), "tag").commit();
			return;
		case Topics:
			getSupportFragmentManager().beginTransaction().add(R.id.fl_container, new TopicsEditFragment(queOrNote), "topics").commit();
			return;
		default:
			throw new EfeiException("can't start activity for edit_content:" + editContent);
		}
	}

	public static enum EditContent
	{
		Topics, Tag;
	}

	private static final String KEY_EDIT_CONTENT = "key.edit.content";

	public static void forward(EditContent content)
	{
		Intent intent = new Intent(EfeiApplication.getContext(), TagTopicsEditActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(KEY_EDIT_CONTENT, content.name());
		EfeiApplication.getContext().startActivity(intent);
	}
}
