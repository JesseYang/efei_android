package com.efei.android.module.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.utils.TextUtils;

class QueListAdapter extends BaseAdapter
{
	final List<QuestionOrNote2> content = new ArrayList<QuestionOrNote2>();
	private QueListFragment queListFragment;

	private Map<String, QuestionOrNote2> selectedQues = new HashMap<String, QuestionOrNote2>();

	QueListAdapter(QueListFragment fragment)
	{
		this.queListFragment = fragment;
	}

	public Map<String, QuestionOrNote2> getSelectedQues()
	{
		return selectedQues;
	}

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

	void selectAll()
	{
		for (QuestionOrNote2 note : content)
			selectedQues.put(note.metaData.get_id(), note);
		notifyDataSetChanged();
	}

	void cancelSelectAll()
	{
		selectedQues.clear();
		notifyDataSetChanged();
	}

	private void view_itemMap(final QuestionOrNote2 note, final QueViewHolder holder)
	{
		setTextContent(holder.tvTag, note.metaData.getTag(), "标签");
		setTextContent(holder.tvPoint, note.metaData.getTopic_str(), "知识点");
		setTextContent(holder.tvNote, note.metaData.getSummary(), "总结");
		setTextContent(holder.tvQue, note.content, "题目");

		if (queListFragment.isSelectMode())
		{
			holder.flSelect.setVisibility(View.VISIBLE);
			holder.ivSelect.setVisibility(View.VISIBLE);
			holder.ivSelect.setSelected(selectedQues.containsKey(note.metaData.get_id()));
			holder.flSelect.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					v.setSelected(!v.isSelected());
					if (v.isSelected())
						selectedQues.put(note.metaData.get_id(), note);
					else
						selectedQues.remove(note.metaData.get_id());
					holder.ivSelect.setSelected(selectedQues.containsKey(note.metaData.get_id()));
				}
			});
		} else
		{
			holder.flSelect.setVisibility(View.GONE);
			holder.ivSelect.setVisibility(View.GONE);
			holder.flSelect.setSelected(false);
			holder.ivSelect.setSelected(false);
		}
	}

	private void setTextContent(TextView tv, CharSequence text, CharSequence defaultString)
	{
		if (TextUtils.isEmpty(text))
			tv.setText(defaultString);
		else
			tv.setText(text);
	}

	private static class QueViewHolder
	{
		private TextView tvTag;
		private TextView tvPoint;
		private TextView tvNote;
		private TextView tvQue;

		private FrameLayout flSelect;
		private ImageView ivSelect;

		private QueViewHolder(View itemView)
		{
			tvTag = (TextView) itemView.findViewById(R.id.tv_tag);
			tvPoint = (TextView) itemView.findViewById(R.id.tv_point);
			tvNote = (TextView) itemView.findViewById(R.id.tv_note);
			tvQue = (TextView) itemView.findViewById(R.id.tv_que);
			flSelect = (FrameLayout) itemView.findViewById(R.id.fl_select);
			ivSelect = (ImageView) itemView.findViewById(R.id.iv_check);
		}
	}

}
