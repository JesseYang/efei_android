package com.efei.android.module.question;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.lib.android.bean.persistance.QuestionOrNote;

public class QueListAdapter extends BaseAdapter
{
	final List<QuestionOrNote> content = new ArrayList<QuestionOrNote>();

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

	private void view_itemMap(QuestionOrNote questionOrNote, QueViewHolder holder)
	{
		holder.tvTag.setText(questionOrNote.getTag_set());
		holder.tvPoint.setText(questionOrNote.getTopics());
		holder.tvNote.setText(questionOrNote.getSummary());
		holder.tvQue.setText(questionOrNote.getFormattedContent());
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
