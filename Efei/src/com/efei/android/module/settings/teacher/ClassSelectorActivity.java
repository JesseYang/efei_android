package com.efei.android.module.settings.teacher;

import java.util.List;

import android.os.Bundle;
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
import com.efei.android.module.Constants;
import com.efei.lib.android.bean.net.common_data.Teacher;
import com.efei.lib.android.bean.net.common_data.Teacher.Classs;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.utils.CollectionUtils;

public class ClassSelectorActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_teacher_class);

		getSupportActionBar().setTitle("Ñ¡Ôñ°à¼¶");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		EfeiApplication app = (EfeiApplication) getApplication();
		final Teacher teacher = app.removeTemporary(Constants.TMP_TEACHER);
		if (CollectionUtils.isEmpty(teacher.getClasses()))
		{
			finish();
			app.addTemporary(Constants.TMP_TEACHER, teacher);
			EfeiApplication.switchToActivity(ConfirmAddTeacherActivity.class);
			return;
		}

		ListView lv = (ListView) findViewById(R.id.lv);
		lv.setAdapter(new ClassAdapter(teacher.getClasses()));
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				EfeiApplication app = (EfeiApplication) getApplication();
				app.addTemporary(Constants.TMP_TEACHER, teacher);
				ClassAdapter adapter = (ClassAdapter) parent.getAdapter();
				app.addTemporary(Constants.TMP_CLASS, adapter.classes.get(position));
				EfeiApplication.switchToActivity(ConfirmAddTeacherActivity.class);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (android.R.id.home == item.getItemId())
		{
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private static class ClassAdapter extends BaseAdapter
	{
		private List<Classs> classes;

		public ClassAdapter(List<Classs> classes)
		{
			this.classes = classes;
		}

		@Override
		public int getCount()
		{
			return classes.size();
		}

		@Override
		public Object getItem(int position)
		{
			return classes.get(position);
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
				convertView = View.inflate(parent.getContext(), R.layout.item_selector, null);
				TextView itemName = (TextView) convertView.findViewById(R.id.tv_item_name);
				itemName.setTag(itemName);
			}
			TextView tv = (TextView) convertView.getTag();
			tv.setText(classes.get(position).getName());
			return convertView;
		}

	}
}
