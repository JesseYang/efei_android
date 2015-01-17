package com.efei.android.module.list;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.utils.TextUtils;

public class QueListAdapter extends BaseAdapter
{
	final List<QuestionOrNote2> content = new ArrayList<QuestionOrNote2>();

	@Override
	public int getCount()
	{
		return content.size();
	}

	@Override
	public Object getItem(int position)
	{
		return content.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (null == convertView)
		{
			convertView = View.inflate(parent.getContext(), R.layout.item_que_note, null);
			convertView.setTag(new QueViewHolder(convertView));
		}

		QueViewHolder holder = (QueViewHolder) convertView.getTag();
		view_itemMap(content.get(position), holder);

		return convertView;
	}

	private void view_itemMap(QuestionOrNote2 note, QueViewHolder holder)
	{
		setTextContent(holder.tvTag, note.metaData.getTag());
		setTextContent(holder.tvPoint, note.metaData.getTopics());
		setTextContent(holder.tvNote, note.metaData.getSummary());
		setTextContent(holder.tvQue, note.content);
	}

	private void setTextContent(TextView tv, CharSequence text)
	{
		if (TextUtils.isEmpty(text))
			return;
		else
			tv.setText(text);
	}

	private static class QueViewHolder
	{
		private TextView tvTag;
		private TextView tvPoint;
		private TextView tvNote;
		private TextView tvQue;

		private QueViewHolder(View itemView)
		{
			tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
			tvPoint = (TextView) itemView.findViewById(R.id.tv_point);
			tvNote = (TextView) itemView.findViewById(R.id.tv_note);
			tvQue = (TextView) itemView.findViewById(R.id.tv_que);
		}
	}

}
