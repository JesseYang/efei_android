package com.efei.android.module.settings.teacher;

import java.util.List;

import android.content.Intent;
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
import android.widget.Toast;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.android.module.MainActivity;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.common_data.Teacher;
import com.efei.lib.android.bean.net.common_data.Teacher.Classs;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.utils.CollectionUtils;

public class ClassSelectorActivity extends ActionBarActivity
{
	public static final String KEY_WHO_START_ME = "key_who_start_me";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_teacher_class);

		getSupportActionBar().setTitle("选择班级");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		EfeiApplication app = (EfeiApplication) getApplication();
		final Teacher teacher = app.removeTemporary(Constants.TMP_TEACHER);
		if (CollectionUtils.isEmpty(teacher.getClasses()) || teacher.getClasses().size() < 2)
		{
			final Classs classs = CollectionUtils.isEmpty(teacher.getClasses()) ? null : teacher.getClasses().get(0);
			Executor.INSTANCE.execute(new JobAsyncTask<BaseRespBean>(new BizRunner_AddTeacher(teacher, classs),
					new IUICallback.Adapter<BaseRespBean>()
					{
						@Override
						public void onPostExecute(BaseRespBean result)
						{
							Toast.makeText(ClassSelectorActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
							finish();
							forwardDestination();
						}
					}));
			return;
		}

		ListView lv = (ListView) findViewById(R.id.lv);
		lv.setAdapter(new ClassAdapter(teacher.getClasses()));
		lv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Executor.INSTANCE.execute(new JobAsyncTask<BaseRespBean>(new BizRunner_AddTeacher(teacher, teacher.getClasses().get(position)),
						new IUICallback.Adapter<BaseRespBean>()
						{
							@Override
							public void onPostExecute(BaseRespBean result)
							{
								Toast.makeText(ClassSelectorActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
								finish();
								forwardDestination();
							}

						}));
			}
		});
	}

	private void forwardDestination()
	{
		String whoStartMe = getIntent().getStringExtra(KEY_WHO_START_ME);
		if (TeacherSearchActivity.class.getSimpleName().equals(whoStartMe))
		{
			Intent intent = new Intent(ClassSelectorActivity.this, MyTeacherActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else
			EfeiApplication.switchToActivity(MainActivity.class);
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
