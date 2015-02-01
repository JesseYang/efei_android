package com.efei.android.module.list;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.efei.android.R;

public class SelectorActivity extends ActionBarActivity
{
	public static enum SelectorType implements Serializable
	{
		Subject("科目选择", new String[]
		{ "语文", "数学", "英语", "物理", "化学", "生物", "历史", "地理", "政治", "其他" }), //
		Time("时间选择", new String[]
		{ "最近一周", "最近一个月", "最近三个月", "最近半年", "全部时间" }), //
		Tag("标签选择", new String[]
		{ "不懂", "不会", "不对", "典型题", "全部标签" });//

		final private String name;
		final String[] content;

		private SelectorType(String name, String[] content)
		{
			this.name = name;
			this.content = content;
		}
	}

	public static class SelectorResult implements Serializable
	{
		private static final long serialVersionUID = 1L;
		public final int index;
		public final SelectorType type;

		public SelectorResult(int index, SelectorType type)
		{
			this.index = index;
			this.type = type;
		}
	}

	private static final String KEY_TYPE = "key_type";
	public static final String KEY_RESULT = "key_result";
	private SelectorType type;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		type = (SelectorType) getIntent().getSerializableExtra(KEY_TYPE);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(type.name);
		actionBar.setDisplayHomeAsUpEnabled(true);
		setupViewsByType(type);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setupViewsByType(final SelectorType type)
	{
		MyAdapter adapter = new MyAdapter(type.content);
		ListView lv = new ListView(this);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Intent intent = new Intent();
				intent.putExtra(KEY_RESULT, new SelectorResult(position, type));
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		setContentView(lv);
	}

	static void forwardForResult(QueListFragment source, SelectorType type)
	{
		Intent intent = new Intent(source.getActivity(), SelectorActivity.class);
		intent.putExtra(KEY_TYPE, type);
		source.startActivityForResult(intent, 1);
	}

	private static class MyAdapter extends BaseAdapter
	{
		private String[] content;

		public MyAdapter(String[] content)
		{
			this.content = content;
		}

		@Override
		public int getCount()
		{
			return content.length;
		}

		@Override
		public Object getItem(int position)
		{
			return content[position];
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
				convertView = View.inflate(parent.getContext(), R.layout.item_selector, null);
			TextView tv = (TextView) convertView.findViewById(R.id.tv_item_name);
			tv.setText(content[position]);
			return convertView;
		}
	}

}
