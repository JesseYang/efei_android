package com.efei.android.module.settings.teacher;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.common_data.Teacher;
import com.efei.lib.android.bean.net.common_data.Teacher.Classs;
import com.efei.lib.android.biz_remote_interface.ISettingService.RespSearchTeachers;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.utils.CollectionUtils;
import com.efei.lib.android.utils.TextUtils;

public class TeacherSearchActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_teacher_search);

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

	private void search(CharSequence text)
	{
		if (TextUtils.isBlank(text))
			return;
		findViewById(R.id.lv).setVisibility(View.GONE);
		findViewById(R.id.pb_progress).setVisibility(View.VISIBLE);
		Executor.INSTANCE.execute(new JobAsyncTask<RespSearchTeachers>(new BizRunner_SearchTeachers(text.toString()),
				new IUICallback.Adapter<RespSearchTeachers>()
				{
					@Override
					public void onPostExecute(RespSearchTeachers result)
					{
						View progressBar = findViewById(R.id.pb_progress);
						progressBar.setVisibility(View.GONE);
						ListView lvContent = (ListView) findViewById(R.id.lv);
						lvContent.setVisibility(View.VISIBLE);
						List<Teacher> teachers = result.getTeachers();
						if (CollectionUtils.isEmpty(teachers))
							return;
						lvContent.setAdapter(new MyAdapter(teachers, true));
					}

					@Override
					public void onError(Throwable e)
					{
						super.onError(e);
						findViewById(R.id.pb_progress).setVisibility(View.GONE);
					}
				}));
	}

	private class MyAdapter extends TeacherAdapter
	{
		MyAdapter(List<Teacher> teachers, boolean add)
		{
			super(teachers, add);
		}

		@Override
		protected void updateItemView(ViewHolder holder, final Teacher teacher)
		{
			holder.ivAdd.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if (CollectionUtils.isEmpty(teacher.getClasses()) || teacher.getClasses().size() < 2)
					{
						final Classs classs = CollectionUtils.isEmpty(teacher.getClasses()) ? null : teacher.getClasses().get(0);
						Executor.INSTANCE.execute(new JobAsyncTask<BaseRespBean>(new BizRunner_AddTeacher(teacher, classs),
								new IUICallback.Adapter<BaseRespBean>()
								{
									@Override
									public void onPostExecute(BaseRespBean result)
									{
										Toast.makeText(TeacherSearchActivity.this, "Ìí¼Ó³É¹¦", Toast.LENGTH_SHORT).show();
										Intent intent = new Intent(TeacherSearchActivity.this, MyTeacherActivity.class);
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(intent);
										finish();
									}
								}));
						return;
					}
					EfeiApplication app = (EfeiApplication) getApplication();
					app.addTemporary(Constants.TMP_TEACHER, teacher);
					EfeiApplication.switchToActivity(ClassSelectorActivity.class);
				}
			});
		}
	}
}
