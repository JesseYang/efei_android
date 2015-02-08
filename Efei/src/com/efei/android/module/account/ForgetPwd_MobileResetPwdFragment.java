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
import com.efei.lib.android.async.IBusinessCallback;
import com.efei.lib.android.async.IJob;
import com.efei.lib.android.async.IUICallback;
import com.efei.lib.android.async.JobAsyncTask;
import com.efei.lib.android.bean.net.BaseRespBean;
import com.efei.lib.android.biz_remote_interface.IAccountService;
import com.efei.lib.android.utils.TextUtils;

final class ForgetPwd_MobileResetPwdFragment extends Fragment
{
	private EditText editNewPwd;
	private String resetToken;

	ForgetPwd_MobileResetPwdFragment(String resetToken)
	{
		this.resetToken = resetToken;
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
		String sp = new String("完成");
		MenuItem item = menu.add(sp);
		item.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				final CharSequence newPwd = editNewPwd.getText();
				if (TextUtils.isBlank(newPwd))
				{
					Toast.makeText(getActivity(), "设置合法密码", Toast.LENGTH_SHORT).show();
					return true;
				}
				Executor.INSTANCE.execute(new JobAsyncTask<BaseRespBean>(new BizRunner_ResetPwd(newPwd.toString(), resetToken),
						new IUICallback.Adapter<BaseRespBean>()
						{
							@Override
							public void onPostExecute(BaseRespBean result)
							{
								getActivity().finish();
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
		View view = View.inflate(getActivity(), R.layout.fragment_forget_pwd_reset, null);
		editNewPwd = (EditText) view.findViewById(R.id.actv_pwd);
		return view;
	}

	private static final class BizRunner_ResetPwd implements IBusinessCallback<BaseRespBean>
	{

		private String password;
		private String reset_password_token;

		private BizRunner_ResetPwd(String password, String reset_password_token)
		{
			super();
			this.password = password;
			this.reset_password_token = reset_password_token;
		}

		@Override
		public BaseRespBean onBusinessLogic(IJob job) throws Exception
		{
			return IAccountService.Factory.getService().put0account$passwords(password, reset_password_token);
		}
	}
}