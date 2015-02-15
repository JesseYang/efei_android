package com.efei.android.module.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.lib.android.bean.persistance.QuestionOrNote2;
import com.efei.lib.android.common.EfeiApplication;

public class TagEditFragment extends Fragment
{
	private QuestionOrNote2 queOrNote;
	private String tagSelected;

	public TagEditFragment(QuestionOrNote2 queOrNote)
	{
		this.queOrNote = queOrNote;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = View.inflate(getActivity(), R.layout.fragment_tag_edit, null);
		tagSelected = queOrNote.metaData.getTag();
		ListView lv = (ListView) view.findViewById(R.id.lv);
		final String[] tags = queOrNote.metaData.getTag_set().split("\\,");
		final BaseAdapter adapter = new TagAdapter(tags);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				tagSelected = tags[position];
				adapter.notifyDataSetChanged();
			}
		});
		return view;
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
			queOrNote.metaData.setTag(tagSelected);
			EfeiApplication app = (EfeiApplication) getActivity().getApplication();
			app.addTemporary(Constants.TMP_QUE, queOrNote);
			getActivity().finish();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class TagAdapter extends BaseAdapter
	{
		private String[] tags;

		public TagAdapter(String[] tags)
		{
			this.tags = tags;
		}

		@Override
		public int getCount()
		{
			return tags.length;
		}

		@Override
		public Object getItem(int position)
		{
			return tags[position];
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
				convertView = View.inflate(parent.getContext(), R.layout.item_tag_edit, null);
				ViewHolder holder = new ViewHolder();
				holder.ivCheck = (ImageView) convertView.findViewById(R.id.iv_check);
				holder.tvTag = (TextView) convertView.findViewById(R.id.tv_tag);
				convertView.setTag(holder);
			}

			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.tvTag.setText(tags[position]);
			final int visibility = tags[position].equals(tagSelected) ? View.VISIBLE : View.INVISIBLE;
			holder.ivCheck.setVisibility(visibility);
			return convertView;
		}
	}

	private static class ViewHolder
	{
		private TextView tvTag;
		private ImageView ivCheck;
	}
}
