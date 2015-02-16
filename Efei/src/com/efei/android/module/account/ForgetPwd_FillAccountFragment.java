package com.efei.android.module.account;

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
import android.widget.Toast;

import com.efei.android.R;
import com.efei.lib.android.async.Executor;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.utils.TextUtils;

final class ForgetPwd_FillAccountFragment extends Fragment
{
	private EditText editAccount;

	ForgetPwd_FillAccountFragment()
	{
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
				final CharSequence account = editAccount.getText();
				if (TextUtils.isBlank(account))
				{
					Toast.makeText(getActivity(), "手机号或邮箱不能为空！", Toast.LENGTH_SHORT).show();
					return true;
				}
				Executor.INSTANCE.execute(new JobAsyncTask<BaseRespBean>(new BizRunner_SendAccountForPwd(account.toString()),
						new IUICallback.Adapter<BaseRespBean>()
						{
							@Override
							public void onPostExecute(BaseRespBean result)
							{
								if (account.toString().contains("@"))
								{
									getActivity().getSupportFragmentManager()
											.beginTransaction()
											.replace(R.id.fl_fragment_container,
													new ForgetPwd_EmailSendedFragment(account.toString()))
											.commitAllowingStateLoss();
								} else
								{
									getActivity().getSupportFragmentManager()
											.beginTransaction()
											.replace(R.id.fl_fragment_container,
													new ForgetPwd_MobileVerfiyCodeFragment(account
															.toString())).commitAllowingStateLoss();
								}
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
		View view = View.inflate(getActivity(), R.layout.fragment_forget_pwd_fill_info, null);
		editAccount = (EditText) view.findViewById(R.id.actv_email_mobile);
		return view;
	}
}