package com.efei.android.module.edit;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.utils.TextUtils;

public class TopicsEditFragment extends Fragment
{
	private QuestionOrNote2 queOrNote;
	private TopicsAdapter adapter;

	public TopicsEditFragment(QuestionOrNote2 queOrNote)
	{
		this.queOrNote = queOrNote;
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
			queOrNote.metaData.setTopics(sb.toString());
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
		lv.setAdapter(adapter);

		view.findViewById(R.id.btn_add).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AutoCompleteTextView editView = (AutoCompleteTextView) view.findViewById(R.id.actv_topic);
				String content = null == editView.getText() ? null : editView.getText().toString();
				if (TextUtils.isEmpty(content))
					return;
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
}
