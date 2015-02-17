package com.efei.android.module.edit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.biz_remote_interface.IQueOrNoteLookUpService.RespTopics_PinyinEntry;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.utils.TextUtils;

public class TopicsEditFragment extends Fragment
{
	private QuestionOrNote2 queOrNote;
	private TopicsAdapter adapter;
	private RespTopics_PinyinEntry pinyinEntry;

	public TopicsEditFragment(QuestionOrNote2 queOrNote, RespTopics_PinyinEntry pinyinEntry)
	{
		this.queOrNote = queOrNote;
		this.pinyinEntry = pinyinEntry;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = View.inflate(getActivity(), R.layout.fragment_topics_edit, null);
		setupViews(view);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		MenuItem item = menu.add(Menu.NONE, R.id.menu_complete, Menu.NONE, "Íê³É");
		MenuCompat.setShowAsAction(item, MenuItem.SHOW_AS_ACTION_ALWAYS);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_complete:
			StringBuilder sb = new StringBuilder();
			for (String topic : adapter.topics)
				sb.append(topic).append(',');
			queOrNote.metaData.setTopic_str(sb.toString());
			EfeiApplication app = (EfeiApplication) getActivity().getApplication();
			app.addTemporary(Constants.TMP_QUE, queOrNote);
			getActivity().finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setupViews(final View view)
	{
		ListView lv = (ListView) view.findViewById(R.id.lv);
		adapter = new TopicsAdapter();
		if (!TextUtils.isBlank(queOrNote.metaData.getTopic_str()))
		{
			List<String> topicStrs = new ArrayList<String>(Arrays.asList(queOrNote.metaData.getTopic_str().split(",")));
			Iterator<String> iterator = topicStrs.iterator();
			while (iterator.hasNext())
			{
				String next = iterator.next();
				if (TextUtils.isBlank(next))
					iterator.remove();
			}
			adapter.topics.addAll(topicStrs);
		}
		lv.setAdapter(adapter);

		final AutoCompleteTextView editView = (AutoCompleteTextView) view.findViewById(R.id.actv_topic);
		List<Object[]> topics = pinyinEntry.getTopics();
		final ArrayAdapter<String> autoAdapter = new AutoAdapter(getActivity(), R.layout.item_auto_complete, R.id.tv, topics);
		editView.setAdapter(autoAdapter);
		editView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				view.findViewById(R.id.btn_add).performClick();
			}
		});
		editView.setThreshold(1);
		view.findViewById(R.id.btn_add).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String content = null == editView.getText() ? null : editView.getText().toString();
				if (TextUtils.isEmpty(content))
					return;
				editView.setText("");
				adapter.topics.add(content);
				adapter.notifyDataSetChanged();
			}
		});
	}

	private class TopicsAdapter extends BaseAdapter
	{
		private List<String> topics = new ArrayList<String>();

		@Override
		public int getCount()
		{
			return topics.size();
		}

		@Override
		public Object getItem(int position)
		{
			return topics.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			if (null == convertView)
			{
				convertView = View.inflate(parent.getContext(), R.layout.item_topics_edit, null);
				ViewHolder holder = new ViewHolder();
				holder.tvTopic = (TextView) convertView.findViewById(R.id.tv_topic);
				convertView.setTag(holder);
			}

			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.tvTopic.setText(topics.get(position));
			holder.tvTopic.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					topics.remove(position);
					adapter.notifyDataSetChanged();
				}
			});
			return convertView;
		}
	}

	private static class ViewHolder
	{
		private TextView tvTopic;
	}

	private static class AutoAdapter extends ArrayAdapter<String>
	{

		private List<Object[]> topicsActual;

		public AutoAdapter(Context context, int resource, int textViewResourceId, List<Object[]> topicsActual)
		{
			super(context, resource, textViewResourceId);
			this.topicsActual = topicsActual;
		}

		@Override
		public Filter getFilter()
		{
			return new ArrayFilter(topicsActual, this);
		}

	}

	@SuppressLint("DefaultLocale")
	private static class ArrayFilter extends Filter
	{
		final private List<Object[]> topicsActual;
		private ArrayAdapter<String> adapter;

		public ArrayFilter(List<Object[]> topicsActual, ArrayAdapter<String> adapter)
		{
			this.topicsActual = topicsActual;
			this.adapter = adapter;
		}

		@Override
		protected FilterResults performFiltering(CharSequence prefix)
		{
			FilterResults results = new FilterResults();

			if (prefix == null || prefix.length() == 0)
			{
				ArrayList<String> list = new ArrayList<String>();
				results.values = list;
				results.count = list.size();
			} else
			{
				String prefixString = prefix.toString().toLowerCase();

				ArrayList<Object[]> values = new ArrayList<Object[]>(topicsActual);

				final int count = values.size();
				final ArrayList<String> matchRes = new ArrayList<String>();

				for (int i = 0; i < count; i++)
				{
					final Object[] entry = values.get(i);
					final String entryHanzi = entry[0].toString().toLowerCase();
					final String entryPinyin = entry[1].toString().toLowerCase();

					// First match against the whole, non-splitted value
					if (entryHanzi.startsWith(prefixString) || entryPinyin.startsWith(prefixString))
						matchRes.add(entryHanzi);
				}

				results.values = matchRes;
				results.count = matchRes.size();
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results)
		{
			adapter.clear();
			adapter.addAll((Collection<String>) results.values);
		}
	}

}
