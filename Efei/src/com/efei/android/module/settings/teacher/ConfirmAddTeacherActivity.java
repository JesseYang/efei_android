package com.efei.android.module.settings.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.bean.net.common_data.Teacher;
import com.efei.lib.android.bean.net.common_data.Teacher.Classs;
import com.efei.lib.android.common.EfeiApplication;

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
		final Teacher teacher = app.removeTemporary(Constants.TMP_TEACHER);
		final Classs classs = app.removeTemporary(Constants.TMP_CLASS);

		findViewById(R.id.tvAdd).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Executor.INSTANCE.execute(new JobAsyncTask<BaseRespBean>(new BizRunner_AddTeacher(teacher, classs),
						new IUICallback.Adapter<BaseRespBean>()
						{
							@Override
							public void onPostExecute(BaseRespBean result)
							{
								Toast.makeText(ConfirmAddTeacherActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(ConfirmAddTeacherActivity.this, MyTeacherActivity.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
							}
						}));
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
}
