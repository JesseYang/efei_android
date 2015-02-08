package com.efei.android.module.settings.feedback;

import android.os.Bundle;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
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

public class FeedbackActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_feedback);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("意见反馈");
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuItem item = menu.add("完成").setOnMenuItemClickListener(new OnMenuItemClickListener()
		{

			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				EditText et = (EditText) findViewById(R.id.actv_feedback);
				Editable feedback = et.getText();
				if (TextUtils.isBlank(feedback))
					Toast.makeText(FeedbackActivity.this, "请填写建议！", Toast.LENGTH_SHORT).show();
				else
				{
					Executor.INSTANCE.execute(new JobAsyncTask<BaseRespBean>(new BizRunner_Feedback(feedback.toString()),
							new IUICallback.Adapter<BaseRespBean>()
							{
								@Override
								public void onPostExecute(BaseRespBean result)
								{
									Toast.makeText(FeedbackActivity.this, "感谢您的反馈！", Toast.LENGTH_SHORT).show();
									finish();
								}
							}));
				}
				return true;
			}
		});
		MenuCompat.setShowAsAction(item, MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
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

	private static final class BizRunner_Feedback implements IBusinessCallback<BaseRespBean>
	{
		private String feedback;

		public BizRunner_Feedback(String feedback)
		{
			this.feedback = feedback;
		}

		@Override
		public BaseRespBean onBusinessLogic(IJob job) throws Exception
		{
			return ISettingService.Factory.getService().post0student$feedbacks(feedback);
		}
	}
}
