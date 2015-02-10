package com.efei.android.module.list;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.efei.android.R;
import com.efei.lib.android.utils.CollectionUtils;
import com.efei.lib.android.utils.TextUtils;

public class QueSearchActivity extends Activity implements OnItemClickListener
{
	public static final String KEY_SEARCH_KEY_WORD = "key_search_key_word";

	private String key_words = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_que_search);

		ListView lv = (ListView) findViewById(R.id.lv);
		try
		{
			lv.setAdapter(new SearchHistoryAdapter());
			lv.setOnItemClickListener(this);
		} catch (IOException e)
		{
			e.printStackTrace();
			return;
		}

		findViewById(R.id.tvBack).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});

		findViewById(R.id.tvSearch).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				EditText et = (EditText) findViewById(R.id.actv_search);
				search(et.getText());
			}
		});
	}

	private boolean containsKeywordInHistory(String text)
	{
		String[] keywords = key_words.split(":");
		for (String keyword : keywords)
		{
			if (keyword.equals(text))
				return true;
		}
		return false;
	}

	private void search(CharSequence text)
	{
		try
		{
			if (!TextUtils.isBlank(text) && !containsKeywordInHistory(text.toString()))
			{
				String search = text.toString();
				key_words = search + ":" + key_words;
				File file = new File(getCacheDir(), "search_history");
				if (!file.exists())
					file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(key_words.getBytes());
				fos.close();
			}
			Intent intent = new Intent();
			intent.putExtra(KEY_SEARCH_KEY_WORD, text.toString());
			setResult(RESULT_OK, intent);
			finish();
		} catch (Exception e)
		{
			finish();
		}
	}

	private class SearchHistoryAdapter extends BaseAdapter
	{
		private List<String> history_key_words = new ArrayList<String>();

		public SearchHistoryAdapter() throws IOException
		{
			File file = new File(getCacheDir(), "search_history");
			if (!file.exists())
				file.createNewFile();
			else
			{
				FileInputStream fis = new FileInputStream(file);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int len = -1;
				byte[] buffer = new byte[1024];
				while (-1 != (len = fis.read(buffer)))
					bos.write(buffer, 0, len);
				key_words = new String(bos.toByteArray());
				if (!TextUtils.isBlank(key_words))
				{
					final String[] key_word_array = key_words.split(":");
					int counts = 0;
					for (String key_word : key_word_array)
					{
						history_key_words.add(key_word);
						if (++counts > 6)
							break;
					}
					if (!CollectionUtils.isEmpty(history_key_words))
						history_key_words.add("Çå³ýËÑË÷¼ÇÂ¼");
				}
				fis.close();
			}
		}

		@Override
		public int getCount()
		{
			return history_key_words.size();
		}

		@Override
		public Object getItem(int position)
		{
			return history_key_words.get(position);
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
				convertView = View.inflate(QueSearchActivity.this, R.layout.item_que_keyword_search, null);
			TextView tv = (TextView) convertView.findViewById(R.id.tv);
			tv.setText(history_key_words.get(position));
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		BaseAdapter adapter = (BaseAdapter) parent.getAdapter();
		String key_word = (String) adapter.getItem(position);
		if (position + 1 == adapter.getCount() && "Çå³ýËÑË÷¼ÇÂ¼".equals(key_word))
		{
			File file = new File(getCacheDir(), "search_history");
			if (file.exists())
				file.delete();
			ListView lv = (ListView) parent;
			try
			{
				lv.setAdapter(new SearchHistoryAdapter());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			return;
		}
		search(key_word);
	}
}
