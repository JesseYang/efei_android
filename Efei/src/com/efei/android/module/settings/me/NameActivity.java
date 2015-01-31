package com.efei.android.module.settings.me;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.efei.android.R;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.biz_remote_interface.ISettingService;
import com.efei.lib.android.utils.TextUtils;

public class NameActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_me_name);
		getSupportActionBar().setTitle("姓名设置");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (android.R.id.home == item.getItemId())
			finish();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		String sp = new String("完成");
		MenuItem item = menu.add(sp);
		item.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{

			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				EditText etName = (EditText) findViewById(R.id.actv_name);
				if (TextUtils.isBlank(etName.getText()))
				{
					Toast.makeText(NameActivity.this, "姓名不能为空！", Toast.LENGTH_SHORT).show();
					return true;
				}
				Executor.INSTANCE.execute(new JobAsyncTask<BaseRespBean>(new BizRunner_ChangeName(etName.getText().toString()),
						new IUICallback.Adapter<BaseRespBean>()
						{
							@Override
							public void onPostExecute(BaseRespBean result)
							{
								finish();
							}
						}));
				return true;
			}
		});
		MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		return super.onCreateOptionsMenu(menu);
	}

	private static class BizRunner_ChangeName implements IBusinessCallback<BaseRespBean>
	{
		private String name;

		public BizRunner_ChangeName(String name)
		{
			this.name = name;
		}

		@Override
		public BaseRespBean onBusinessLogic(IJob job) throws Exception
		{
			return ISettingService.Factory.getService().put0student$students$rename(name);
		}
	}

}
