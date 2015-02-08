package com.efei.android.module.settings.teacher;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.android.module.MainActivity;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.Subject;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.common_data.Teacher;
import com.efei.lib.android.bean.net.common_data.Teacher.Classs;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.utils.CollectionUtils;

public class ConfirmAddTeacherActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_add_teacher);

		getSupportActionBar().setTitle("添加教师");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		EfeiApplication app = (EfeiApplication) getApplication();
		boolean saveSingleQue = getIntent().getBooleanExtra(Constants.KEY_FOR_SAVE_QUE, false);
		if (saveSingleQue)
			setupBySingleQueSave(app);
		boolean saveQues = getIntent().getBooleanExtra(Constants.KEY_FOR_SAVE_QUE_LIST, false);
		if (saveQues)
			setupByQuesSave(app);
	}

	private void setupByQuesSave(final EfeiApplication app)
	{
		final List<Teacher> teachers = app.removeTemporary(Constants.TMP_TEACHER_LIST);
		if (CollectionUtils.isEmpty(teachers))
		{
			finish();
			Intent intent = new Intent(ConfirmAddTeacherActivity.this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return;
		}

		final Teacher teacher = teachers.remove(0);
		TextView tvInfo = (TextView) findViewById(R.id.tv_teacher_info);
		// 北京市第一中学 数学 张三丰
		tvInfo.setText("");
		tvInfo.append(teacher.getSchool() + " ");
		tvInfo.append(Subject.getSubjectByIndex(teacher.getSubject()).name + " ");
		tvInfo.append(teacher.getName());

		findViewById(R.id.tvAdd).setOnClickListener(new OnClickListener()
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
									Toast.makeText(ConfirmAddTeacherActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
									finish();
									if (CollectionUtils.isEmpty(teachers))
									{
										Intent intent = new Intent(ConfirmAddTeacherActivity.this, MainActivity.class);
										intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(intent);
									} else
									{
										app.addTemporary(Constants.TMP_QUE_LIST, teachers);
										Intent intent = new Intent(ConfirmAddTeacherActivity.this,
												ConfirmAddTeacherActivity.class);
										intent.putExtra(Constants.KEY_FOR_SAVE_QUE_LIST, true);
										startActivity(intent);
									}
								}
							}));
					return;
				}
				app.addTemporary(Constants.TMP_TEACHER, teacher);
				Intent intent = new Intent(ConfirmAddTeacherActivity.this, ClassSelectorActivity.class);
				intent.putExtra(ClassSelectorActivity.KEY_WHO_START_ME, ConfirmAddTeacherActivity.class.getSimpleName());
				EfeiApplication.switchToActivity(ClassSelectorActivity.class);
			}
		});
	}

	private void setupBySingleQueSave(EfeiApplication app)
	{
		final Teacher teacher = app.removeTemporary(Constants.TMP_TEACHER);

		TextView tvInfo = (TextView) findViewById(R.id.tv_teacher_info);
		// 北京市第一中学 数学 张三丰
		tvInfo.setText("");
		tvInfo.append(teacher.getSchool() + " ");
		tvInfo.append(Subject.getSubjectByIndex(teacher.getSubject()).name + " ");
		tvInfo.append(teacher.getName());

		findViewById(R.id.tvAdd).setOnClickListener(new OnClickListener()
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
									Toast.makeText(ConfirmAddTeacherActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(ConfirmAddTeacherActivity.this, MainActivity.class);
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
				Intent intent = new Intent(ConfirmAddTeacherActivity.this, ClassSelectorActivity.class);
				intent.putExtra(ClassSelectorActivity.KEY_WHO_START_ME, ConfirmAddTeacherActivity.class.getSimpleName());
				EfeiApplication.switchToActivity(ClassSelectorActivity.class);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (android.R.id.home == item.getItemId())
		{
			finish();
			Intent intent = new Intent(ConfirmAddTeacherActivity.this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
