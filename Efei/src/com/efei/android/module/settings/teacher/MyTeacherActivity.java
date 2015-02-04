package com.efei.android.module.settings.teacher;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.efei.android.R;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.common_data.Teacher;
import com.efei.lib.android.biz_remote_interface.ISettingService.RespSearchTeachers;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.utils.CollectionUtils;

public class MyTeacherActivity extends ActionBarActivity
{
	public static Map<String, Teacher> MY_TEACHERS = new HashMap<String, Teacher>();

	private TeacherAdapter adapter;
	private ListView lvContent;
	private View progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("我的老师");
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_my_teacher);
		lvContent = (ListView) findViewById(R.id.lv);
		progressBar = findViewById(R.id.pb_progress);
		registerForContextMenu(lvContent);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		lvContent.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		Executor.INSTANCE.execute(new JobAsyncTask<RespSearchTeachers>(new BizRunner_GetMyTeachers(), new IUICallback.Adapter<RespSearchTeachers>()
		{

			@Override
			public void onPostExecute(RespSearchTeachers result)
			{
				progressBar.setVisibility(View.GONE);
				lvContent.setVisibility(View.VISIBLE);
				List<Teacher> teachers = result.getTeachers();
				if (CollectionUtils.isEmpty(teachers))
					return;
				adapter = new TeacherAdapter(teachers, false);
				lvContent.setAdapter(adapter);
			}

			@Override
			public void onError(Throwable e)
			{
				super.onError(e);
				progressBar.setVisibility(View.GONE);
			}
		}));
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuItem item = menu.add(Menu.NONE, R.id.menu_add, Menu.NONE, "添加");
		MenuCompat.setShowAsAction(item, MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			return true;

		case R.id.menu_add:
			EfeiApplication.switchToActivity(TeacherSearchActivity.class);
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		menu.add("删除").setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				ContextMenuInfo info = item.getMenuInfo();
				if (info instanceof AdapterContextMenuInfo)
				{
					final Teacher teacher = adapter.teachers.get(((AdapterContextMenuInfo) info).position);
					Executor.INSTANCE.execute(new JobAsyncTask<BaseRespBean>(new BizRunner_DeleteTeacher(teacher),
							new IUICallback.Adapter<BaseRespBean>()
							{
								@Override
								public void onPostExecute(BaseRespBean result)
								{
									Iterator<Teacher> iterator = adapter.teachers.iterator();
									while (iterator.hasNext())
									{
										Teacher next = iterator.next();
										if (teacher.getId() == next.getId())
										{
											iterator.remove();
											break;
										}
									}
									adapter.notifyDataSetChanged();
								}
							}));
				}
				return true;
			}
		});
		super.onCreateContextMenu(menu, v, menuInfo);
	}

}
