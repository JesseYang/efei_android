package com.efei.android.module.settings.me;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.efei.android.R;
import com.efei.android.module.Constants;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.common.EfeiApplication;
import com.efei.lib.android.utils.TextUtils;

final class Email_FillFragment extends Fragment
{
	private String email;
	private EditText editNewEmail;

	Email_FillFragment(String email)
	{
		this.email = email;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		menu.clear();
		String sp = new String("下一步");
		MenuItem item = menu.add(sp);
		item.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				final CharSequence email = editNewEmail.getText();
				if (TextUtils.isBlank(email))
				{
					Toast.makeText(getActivity(), "邮箱不能为空！", Toast.LENGTH_SHORT).show();
					return true;
				}
				Executor.INSTANCE.execute(new JobAsyncTask<BaseRespBean>(new BizRunner_ModifyEmail(email.toString()),
						new IUICallback.Adapter<BaseRespBean>()
						{
							@SuppressLint("NewApi") @Override
							public void onPostExecute(BaseRespBean result)
							{
								if (Build.VERSION.SDK_INT >= 17 && getActivity().isDestroyed())
									return;
								EfeiApplication app = (EfeiApplication) getActivity().getApplication();
								app.addTemporary(Constants.KEY_EMAIL, email.toString());
								
								Toast.makeText(getActivity(), "请查收邮件！", Toast.LENGTH_SHORT).show();
								getActivity().getSupportFragmentManager().beginTransaction()
										.replace(R.id.fl_fragment_container, new Email_FinalFragment(email.toString()))
										.commitAllowingStateLoss();
							}
						}));
				return true;
			}
		});
		MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		View view = View.inflate(getActivity(), R.layout.fragment_me_email_fill, null);
		TextView tvPrompt = (TextView) view.findViewById(R.id.tv_prompt);
		if (TextUtils.isBlank(email))
		{
			view.findViewById(R.id.iv_email).setVisibility(View.VISIBLE);
			tvPrompt.setText("请设置一个您用来登录的邮箱地址：");
		} else
		{
			view.findViewById(R.id.iv_email).setVisibility(View.GONE);
			tvPrompt.setText("您当前的邮箱地址为：" + email);
		}
		editNewEmail = (EditText) view.findViewById(R.id.actv_email);
		return view;
	}
}